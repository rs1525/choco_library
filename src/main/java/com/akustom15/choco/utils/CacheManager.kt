package com.akustom15.choco.utils

import android.content.Context
import com.akustom15.choco.exceptions.CacheFullException
import java.io.File

/**
 * Gestor de caché para la librería Choco
 * 
 * Maneja el almacenamiento temporal de archivos extraídos y vistas previas
 */
class CacheManager private constructor(private val context: Context) {
    
    companion object {
        @Volatile
        private var INSTANCE: CacheManager? = null
        
        fun getInstance(context: Context): CacheManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CacheManager(context.applicationContext).also { INSTANCE = it }
            }
        }
        
        private const val CACHE_DIR_NAME = "choco_cache"
        private const val MAX_CACHE_SIZE_MB = 100L
        private const val MAX_CACHE_SIZE_BYTES = MAX_CACHE_SIZE_MB * 1024 * 1024
    }
    
    private val cacheDir: File by lazy {
        File(context.cacheDir, CACHE_DIR_NAME).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }
    
    /**
     * Obtiene el directorio de caché
     */
    fun getCacheDir(): File = cacheDir
    
    /**
     * Obtiene un archivo temporal en el caché
     * 
     * @param fileName Nombre del archivo
     * @return Archivo en el directorio de caché
     */
    fun getTempFile(fileName: String): File {
        return File(cacheDir, fileName)
    }
    
    /**
     * Obtiene un directorio temporal en el caché
     * 
     * @param dirName Nombre del directorio
     * @return Directorio en el caché
     */
    fun getTempDir(dirName: String): File {
        return File(cacheDir, dirName).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }
    
    /**
     * Verifica si hay suficiente espacio en el caché
     * 
     * @param requiredBytes Bytes requeridos
     * @return True si hay suficiente espacio
     */
    fun hasEnoughSpace(requiredBytes: Long): Boolean {
        val currentSize = getCacheSize()
        return (currentSize + requiredBytes) <= MAX_CACHE_SIZE_BYTES
    }
    
    /**
     * Obtiene el tamaño actual del caché en bytes
     * 
     * @return Tamaño del caché en bytes
     */
    fun getCacheSize(): Long {
        return calculateDirectorySize(cacheDir)
    }
    
    /**
     * Obtiene el tamaño actual del caché en MB
     * 
     * @return Tamaño del caché en MB
     */
    fun getCacheSizeMB(): Long {
        return getCacheSize() / (1024 * 1024)
    }
    
    /**
     * Limpia el caché si es necesario para hacer espacio
     * 
     * @param requiredBytes Bytes que se necesitan liberar
     * @throws CacheFullException Si no se puede liberar suficiente espacio
     */
    fun ensureSpace(requiredBytes: Long) {
        if (!hasEnoughSpace(requiredBytes)) {
            cleanOldFiles()
            
            if (!hasEnoughSpace(requiredBytes)) {
                val currentSize = getCacheSizeMB()
                val requiredMB = requiredBytes / (1024 * 1024)
                throw CacheFullException(requiredMB, MAX_CACHE_SIZE_MB - currentSize)
            }
        }
    }
    
    /**
     * Limpia archivos antiguos del caché
     */
    fun cleanOldFiles() {
        ChocoLogger.d("Limpiando archivos antiguos del caché")
        
        val files = cacheDir.listFiles() ?: return
        val sortedFiles = files.sortedBy { it.lastModified() }
        
        var currentSize = getCacheSize()
        val targetSize = MAX_CACHE_SIZE_BYTES * 0.8 // Limpiar hasta el 80% del límite
        
        for (file in sortedFiles) {
            if (currentSize <= targetSize) break
            
            val fileSize = if (file.isDirectory) {
                calculateDirectorySize(file)
            } else {
                file.length()
            }
            
            if (file.deleteRecursively()) {
                currentSize -= fileSize
                ChocoLogger.d("Eliminado del caché: ${file.name}")
            }
        }
        
        ChocoLogger.logCache("clean", "Tamaño después de limpieza: ${getCacheSizeMB()}MB")
    }
    
    /**
     * Limpia todo el caché
     */
    fun clearAll() {
        ChocoLogger.d("Limpiando todo el caché")
        
        val files = cacheDir.listFiles() ?: return
        var deletedCount = 0
        
        for (file in files) {
            if (file.deleteRecursively()) {
                deletedCount++
            }
        }
        
        ChocoLogger.logCache("clear_all", "Eliminados $deletedCount elementos")
    }
    
    /**
     * Verifica si un archivo existe en el caché
     * 
     * @param fileName Nombre del archivo
     * @return True si el archivo existe
     */
    fun exists(fileName: String): Boolean {
        return getTempFile(fileName).exists()
    }
    
    /**
     * Elimina un archivo específico del caché
     * 
     * @param fileName Nombre del archivo a eliminar
     * @return True si se eliminó correctamente
     */
    fun delete(fileName: String): Boolean {
        val file = getTempFile(fileName)
        return if (file.exists()) {
            val deleted = file.deleteRecursively()
            if (deleted) {
                ChocoLogger.logCache("delete", fileName)
            }
            deleted
        } else {
            false
        }
    }
    
    /**
     * Obtiene información del caché
     * 
     * @return Map con información del caché
     */
    fun getCacheInfo(): Map<String, Any> {
        val files = cacheDir.listFiles() ?: emptyArray()
        
        return mapOf(
            "cache_size_mb" to getCacheSizeMB(),
            "max_size_mb" to MAX_CACHE_SIZE_MB,
            "file_count" to files.size,
            "directory_count" to files.count { it.isDirectory },
            "cache_usage_percent" to ((getCacheSize().toDouble() / MAX_CACHE_SIZE_BYTES) * 100).toInt()
        )
    }
    
    /**
     * Calcula el tamaño de un directorio recursivamente
     * 
     * @param directory Directorio a calcular
     * @return Tamaño en bytes
     */
    private fun calculateDirectorySize(directory: File): Long {
        var size = 0L
        
        if (directory.exists()) {
            directory.walkTopDown().forEach { file ->
                if (file.isFile) {
                    size += file.length()
                }
            }
        }
        
        return size
    }
    
    /**
     * Obtiene estadísticas detalladas del caché
     * 
     * @return String con estadísticas formateadas
     */
    fun getCacheStats(): String {
        val info = getCacheInfo()
        return """
            📊 Estadísticas del Caché Choco:
            • Tamaño actual: ${info["cache_size_mb"]}MB / ${info["max_size_mb"]}MB
            • Uso: ${info["cache_usage_percent"]}%
            • Archivos: ${info["file_count"]}
            • Directorios: ${info["directory_count"]}
            • Ubicación: ${cacheDir.absolutePath}
        """.trimIndent()
    }
}
