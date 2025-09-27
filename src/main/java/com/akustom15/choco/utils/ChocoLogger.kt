package com.akustom15.choco.utils

import android.util.Log

/**
 * Logger personalizado para la librería Choco
 * 
 * Proporciona logging consistente con niveles configurables
 */
object ChocoLogger {
    
    private const val TAG = "Choco"
    private var isDebugEnabled = false
    
    /**
     * Habilita o deshabilita el logging de debug
     * 
     * @param enabled True para habilitar debug logging
     */
    fun setDebugEnabled(enabled: Boolean) {
        isDebugEnabled = enabled
    }
    
    /**
     * Log de debug (solo se muestra si debug está habilitado)
     * 
     * @param message Mensaje a loggear
     * @param tag Tag opcional (por defecto usa "Choco")
     */
    fun d(message: String, tag: String = TAG) {
        if (isDebugEnabled) {
            Log.d(tag, message)
        }
    }
    
    /**
     * Log de información
     * 
     * @param message Mensaje a loggear
     * @param tag Tag opcional (por defecto usa "Choco")
     */
    fun i(message: String, tag: String = TAG) {
        Log.i(tag, message)
    }
    
    /**
     * Log de advertencia
     * 
     * @param message Mensaje a loggear
     * @param tag Tag opcional (por defecto usa "Choco")
     */
    fun w(message: String, tag: String = TAG) {
        Log.w(tag, message)
    }
    
    /**
     * Log de advertencia con excepción
     * 
     * @param message Mensaje a loggear
     * @param throwable Excepción asociada
     * @param tag Tag opcional (por defecto usa "Choco")
     */
    fun w(message: String, throwable: Throwable, tag: String = TAG) {
        Log.w(tag, message, throwable)
    }
    
    /**
     * Log de error
     * 
     * @param message Mensaje a loggear
     * @param tag Tag opcional (por defecto usa "Choco")
     */
    fun e(message: String, tag: String = TAG) {
        Log.e(tag, message)
    }
    
    /**
     * Log de error con excepción
     * 
     * @param message Mensaje a loggear
     * @param throwable Excepción asociada
     * @param tag Tag opcional (por defecto usa "Choco")
     */
    fun e(message: String, throwable: Throwable, tag: String = TAG) {
        Log.e(tag, message, throwable)
    }
    
    /**
     * Log de información sobre carga de widgets
     * 
     * @param count Número de widgets cargados
     * @param timeMs Tiempo de carga en milisegundos
     */
    fun logWidgetLoad(count: Int, timeMs: Long) {
        i("Cargados $count widgets en ${timeMs}ms")
    }
    
    /**
     * Log de información sobre carga de wallpapers
     * 
     * @param count Número de wallpapers cargados
     * @param timeMs Tiempo de carga en milisegundos
     */
    fun logWallpaperLoad(count: Int, timeMs: Long) {
        i("Cargados $count wallpapers en ${timeMs}ms")
    }
    
    /**
     * Log de error en carga de archivo
     * 
     * @param fileName Nombre del archivo
     * @param error Descripción del error
     */
    fun logFileLoadError(fileName: String, error: String) {
        e("Error cargando archivo '$fileName': $error")
    }
    
    /**
     * Log de información sobre búsqueda
     * 
     * @param query Consulta de búsqueda
     * @param results Número de resultados
     * @param timeMs Tiempo de búsqueda en milisegundos
     */
    fun logSearch(query: String, results: Int, timeMs: Long) {
        d("Búsqueda '$query': $results resultados en ${timeMs}ms")
    }
    
    /**
     * Log de información sobre caché
     * 
     * @param action Acción realizada (hit, miss, clear, etc.)
     * @param details Detalles adicionales
     */
    fun logCache(action: String, details: String = "") {
        d("Cache $action${if (details.isNotEmpty()) ": $details" else ""}")
    }
}
