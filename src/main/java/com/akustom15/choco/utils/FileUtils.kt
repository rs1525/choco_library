package com.akustom15.choco.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.akustom15.choco.models.PresetData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * Utilidades para manejo de archivos en la librería Choco
 */
object FileUtils {
    
    private val gson = Gson()
    
    /**
     * Extrae el contenido de un archivo .kwgt o .klwp (que son archivos ZIP)
     * 
     * @param file Archivo .kwgt o .klwp
     * @param outputDir Directorio donde extraer el contenido
     * @return True si la extracción fue exitosa
     */
    fun extractZipFile(file: File, outputDir: File): Boolean {
        return try {
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            
            ZipFile(file).use { zipFile ->
                zipFile.entries().asSequence().forEach { entry ->
                    val entryFile = File(outputDir, entry.name)
                    
                    if (entry.isDirectory) {
                        entryFile.mkdirs()
                    } else {
                        entryFile.parentFile?.mkdirs()
                        zipFile.getInputStream(entry).use { input ->
                            entryFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Lee el archivo preset.json de un directorio extraído
     * 
     * @param extractedDir Directorio donde se extrajo el contenido
     * @return PresetData o null si hay error
     */
    fun readPresetJson(extractedDir: File): PresetData? {
        val presetFile = File(extractedDir, "preset.json")
        if (!presetFile.exists()) {
            return null
        }
        
        return try {
            val jsonContent = presetFile.readText()
            gson.fromJson(jsonContent, PresetData::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Carga una imagen de vista previa desde un directorio extraído
     * 
     * @param extractedDir Directorio donde se extrajo el contenido
     * @param filename Nombre del archivo de imagen
     * @return Bitmap o null si no se puede cargar
     */
    fun loadPreviewImage(extractedDir: File, filename: String): Bitmap? {
        val imageFile = File(extractedDir, filename)
        if (!imageFile.exists()) {
            return null
        }
        
        return try {
            BitmapFactory.decodeFile(imageFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Obtiene la lista de archivos .kwgt desde assets/widgets
     * 
     * @param context Contexto de la aplicación
     * @return Lista de nombres de archivos .kwgt
     */
    fun getWidgetFilesFromAssets(context: Context): List<String> {
        return try {
            context.assets.list("widgets")?.filter { it.endsWith(".kwgt") } ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Obtiene la lista de archivos .klwp desde assets/wallpapers
     * 
     * @param context Contexto de la aplicación
     * @return Lista de nombres de archivos .klwp
     */
    fun getWallpaperFilesFromAssets(context: Context): List<String> {
        return try {
            context.assets.list("wallpapers")?.filter { it.endsWith(".klwp") } ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Copia un archivo desde assets a un directorio temporal
     * 
     * @param context Contexto de la aplicación
     * @param assetPath Ruta del archivo en assets
     * @param outputFile Archivo de destino
     * @return True si la copia fue exitosa
     */
    fun copyAssetToFile(context: Context, assetPath: String, outputFile: File): Boolean {
        return try {
            context.assets.open(assetPath).use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Limpia un directorio temporal
     * 
     * @param dir Directorio a limpiar
     */
    fun cleanTempDirectory(dir: File) {
        if (dir.exists() && dir.isDirectory) {
            dir.deleteRecursively()
        }
    }
    
    /**
     * Verifica si un archivo es un archivo válido de KWGT o KLWP
     * 
     * @param file Archivo a verificar
     * @return True si es válido
     */
    fun isValidKustomFile(file: File): Boolean {
        if (!file.exists() || !file.canRead()) {
            return false
        }
        
        val extension = file.extension.lowercase()
        if (extension != "kwgt" && extension != "klwp") {
            return false
        }
        
        // Verificar que es un archivo ZIP válido
        return try {
            ZipFile(file).use { zipFile ->
                zipFile.entries().asSequence().any { it.name == "preset.json" }
            }
        } catch (e: Exception) {
            false
        }
    }
}
