package com.akustom15.choco

import android.content.Context
import android.graphics.Bitmap
import com.akustom15.choco.models.ChocoWallpaper
import com.akustom15.choco.models.ChocoWidget
import com.akustom15.choco.utils.FileUtils
import kotlinx.coroutines.*
import java.io.File

/**
 * Clase principal de la librería Choco
 * 
 * Esta clase proporciona métodos para cargar y gestionar widgets y wallpapers
 * para KWGT y KLWP de manera sencilla y eficiente.
 * 
 * Ejemplo de uso:
 * ```kotlin
 * val choco = Choco.getInstance(context)
 * 
 * // Cargar widgets
 * choco.loadWidgets { widgets ->
 *     // Usar los widgets cargados
 * }
 * 
 * // Cargar wallpapers
 * choco.loadWallpapers { wallpapers ->
 *     // Usar los wallpapers cargados
 * }
 * ```
 */
class Choco private constructor(private val context: Context) {
    
    companion object {
        @Volatile
        private var INSTANCE: Choco? = null
        
        /**
         * Obtiene la instancia singleton de Choco
         * 
         * @param context Contexto de la aplicación
         * @return Instancia de Choco
         */
        fun getInstance(context: Context): Choco {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Choco(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var cachedWidgets: List<ChocoWidget>? = null
    private var cachedWallpapers: List<ChocoWallpaper>? = null
    
    /**
     * Carga todos los widgets disponibles de forma asíncrona
     * 
     * @param onComplete Callback que se ejecuta cuando se completa la carga
     */
    fun loadWidgets(onComplete: (List<ChocoWidget>) -> Unit) {
        // Si ya están en caché, devolver inmediatamente
        cachedWidgets?.let { 
            onComplete(it)
            return
        }
        
        scope.launch {
            val widgets = loadWidgetsInternal()
            cachedWidgets = widgets
            
            withContext(Dispatchers.Main) {
                onComplete(widgets)
            }
        }
    }
    
    /**
     * Carga todos los wallpapers disponibles de forma asíncrona
     * 
     * @param onComplete Callback que se ejecuta cuando se completa la carga
     */
    fun loadWallpapers(onComplete: (List<ChocoWallpaper>) -> Unit) {
        // Si ya están en caché, devolver inmediatamente
        cachedWallpapers?.let { 
            onComplete(it)
            return
        }
        
        scope.launch {
            val wallpapers = loadWallpapersInternal()
            cachedWallpapers = wallpapers
            
            withContext(Dispatchers.Main) {
                onComplete(wallpapers)
            }
        }
    }
    
    /**
     * Carga widgets de forma síncrona (usar con cuidado en hilo principal)
     * 
     * @return Lista de widgets
     */
    suspend fun loadWidgetsSync(): List<ChocoWidget> {
        return cachedWidgets ?: loadWidgetsInternal().also { cachedWidgets = it }
    }
    
    /**
     * Carga wallpapers de forma síncrona (usar con cuidado en hilo principal)
     * 
     * @return Lista de wallpapers
     */
    suspend fun loadWallpapersSync(): List<ChocoWallpaper> {
        return cachedWallpapers ?: loadWallpapersInternal().also { cachedWallpapers = it }
    }
    
    /**
     * Busca widgets por título o descripción
     * 
     * @param query Texto a buscar
     * @param onComplete Callback con los resultados
     */
    fun searchWidgets(query: String, onComplete: (List<ChocoWidget>) -> Unit) {
        loadWidgets { widgets ->
            val filtered = widgets.filter { widget ->
                widget.title.contains(query, ignoreCase = true) ||
                widget.description.contains(query, ignoreCase = true) ||
                widget.author.contains(query, ignoreCase = true)
            }
            onComplete(filtered)
        }
    }
    
    /**
     * Busca wallpapers por título o descripción
     * 
     * @param query Texto a buscar
     * @param onComplete Callback con los resultados
     */
    fun searchWallpapers(query: String, onComplete: (List<ChocoWallpaper>) -> Unit) {
        loadWallpapers { wallpapers ->
            val filtered = wallpapers.filter { wallpaper ->
                wallpaper.title.contains(query, ignoreCase = true) ||
                wallpaper.description.contains(query, ignoreCase = true) ||
                wallpaper.author.contains(query, ignoreCase = true)
            }
            onComplete(filtered)
        }
    }
    
    /**
     * Filtra widgets por categoría
     * 
     * @param category Categoría a filtrar
     * @param onComplete Callback con los resultados
     */
    fun getWidgetsByCategory(category: String, onComplete: (List<ChocoWidget>) -> Unit) {
        loadWidgets { widgets ->
            val filtered = widgets.filter { it.category.equals(category, ignoreCase = true) }
            onComplete(filtered)
        }
    }
    
    /**
     * Filtra wallpapers por categoría
     * 
     * @param category Categoría a filtrar
     * @param onComplete Callback con los resultados
     */
    fun getWallpapersByCategory(category: String, onComplete: (List<ChocoWallpaper>) -> Unit) {
        loadWallpapers { wallpapers ->
            val filtered = wallpapers.filter { it.category.equals(category, ignoreCase = true) }
            onComplete(filtered)
        }
    }
    
    /**
     * Limpia la caché de widgets y wallpapers
     */
    fun clearCache() {
        cachedWidgets = null
        cachedWallpapers = null
    }
    
    /**
     * Libera recursos
     */
    fun release() {
        scope.cancel()
        clearCache()
        INSTANCE = null
    }
    
    // Métodos internos
    
    private suspend fun loadWidgetsInternal(): List<ChocoWidget> {
        val widgets = mutableListOf<ChocoWidget>()
        val widgetFiles = FileUtils.getWidgetFilesFromAssets(context)
        val tempDir = File(context.cacheDir, "choco_temp")
        
        for (filename in widgetFiles) {
            try {
                val tempFile = File(tempDir, filename)
                if (FileUtils.copyAssetToFile(context, "widgets/$filename", tempFile)) {
                    val extractDir = File(tempDir, "${filename}_extracted")
                    
                    if (FileUtils.extractZipFile(tempFile, extractDir)) {
                        val presetData = FileUtils.readPresetJson(extractDir)
                        presetData?.let { preset ->
                            val widget = preset.toChocoWidget(tempFile).copy(
                                previewPortrait = FileUtils.loadPreviewImage(extractDir, "preset_thumb_portrait.jpg"),
                                previewLandscape = FileUtils.loadPreviewImage(extractDir, "preset_thumb_landscape.jpg")
                            )
                            widgets.add(widget)
                        }
                    }
                    
                    FileUtils.cleanTempDirectory(extractDir)
                }
                tempFile.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        FileUtils.cleanTempDirectory(tempDir)
        return widgets
    }
    
    private suspend fun loadWallpapersInternal(): List<ChocoWallpaper> {
        val wallpapers = mutableListOf<ChocoWallpaper>()
        val wallpaperFiles = FileUtils.getWallpaperFilesFromAssets(context)
        val tempDir = File(context.cacheDir, "choco_temp")
        
        for (filename in wallpaperFiles) {
            try {
                val tempFile = File(tempDir, filename)
                if (FileUtils.copyAssetToFile(context, "wallpapers/$filename", tempFile)) {
                    val extractDir = File(tempDir, "${filename}_extracted")
                    
                    if (FileUtils.extractZipFile(tempFile, extractDir)) {
                        val presetData = FileUtils.readPresetJson(extractDir)
                        presetData?.let { preset ->
                            val wallpaper = preset.toChocoWallpaper(tempFile).copy(
                                previewPortrait = FileUtils.loadPreviewImage(extractDir, "preset_thumb_portrait.jpg"),
                                previewLandscape = FileUtils.loadPreviewImage(extractDir, "preset_thumb_landscape.jpg")
                            )
                            wallpapers.add(wallpaper)
                        }
                    }
                    
                    FileUtils.cleanTempDirectory(extractDir)
                }
                tempFile.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        FileUtils.cleanTempDirectory(tempDir)
        return wallpapers
    }
}
