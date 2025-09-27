package com.akustom15.choco.models

import android.graphics.Bitmap
import java.io.File

/**
 * Representa un wallpaper de KLWP en la librería Choco
 * 
 * @param id Identificador único del wallpaper
 * @param title Título del wallpaper
 * @param description Descripción del wallpaper
 * @param author Autor del wallpaper
 * @param email Email del autor
 * @param version Versión del wallpaper
 * @param width Ancho del wallpaper en píxeles
 * @param height Alto del wallpaper en píxeles
 * @param previewPortrait Imagen de vista previa en orientación vertical
 * @param previewLandscape Imagen de vista previa en orientación horizontal
 * @param klwpFile Archivo .klwp del wallpaper
 * @param features Características del wallpaper
 * @param screens Número de pantallas que soporta
 */
data class ChocoWallpaper(
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
    val klwpFile: File,
    val features: String = "",
    val category: String = "General",
    val screens: Int = 1
) {
    /**
     * Obtiene la resolución del wallpaper como string
     */
    fun getResolutionString(): String {
        return "${width}x${height}"
    }
    
    /**
     * Verifica si el wallpaper tiene vista previa
     */
    fun hasPreview(): Boolean {
        return previewPortrait != null || previewLandscape != null
    }
    
    /**
     * Verifica si es un wallpaper multi-pantalla
     */
    fun isMultiScreen(): Boolean {
        return screens > 1
    }
}
