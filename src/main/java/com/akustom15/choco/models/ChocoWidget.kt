package com.akustom15.choco.models

import android.graphics.Bitmap
import java.io.File

/**
 * Representa un widget de KWGT en la librería Choco
 * 
 * @param id Identificador único del widget
 * @param title Título del widget
 * @param description Descripción del widget
 * @param author Autor del widget
 * @param email Email del autor
 * @param version Versión del widget
 * @param width Ancho del widget en píxeles
 * @param height Alto del widget en píxeles
 * @param previewPortrait Imagen de vista previa en orientación vertical
 * @param previewLandscape Imagen de vista previa en orientación horizontal
 * @param kwgtFile Archivo .kwgt del widget
 * @param features Características del widget (ej: ANALOG_CLOCK, DIGITAL_CLOCK, etc.)
 */
data class ChocoWidget(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val email: String,
    val version: Int,
    val width: Int,
    val height: Int,
    val previewPortrait: Bitmap? = null,
    val previewLandscape: Bitmap? = null,
    val kwgtFile: File,
    val features: String = "",
    val category: String = "General"
) {
    /**
     * Obtiene el tamaño del widget como string (ej: "2x2", "4x1")
     */
    fun getSizeString(): String {
        val cellWidth = (width + 70) / 140 // Aproximación de celdas
        val cellHeight = (height + 70) / 140
        return "${cellWidth}x${cellHeight}"
    }
    
    /**
     * Verifica si el widget tiene vista previa
     */
    fun hasPreview(): Boolean {
        return previewPortrait != null || previewLandscape != null
    }
}
