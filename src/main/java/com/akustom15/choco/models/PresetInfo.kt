package com.akustom15.choco.models

import com.google.gson.annotations.SerializedName

/**
 * Representa la información de un preset de KWGT/KLWP
 * Basado en la estructura JSON encontrada en los archivos .kwgt/.klwp
 */
data class PresetInfo(
    @SerializedName("archive")
    val archive: String = "",
    
    @SerializedName("author")
    val author: String = "",
    
    @SerializedName("description")
    val description: String = "",
    
    @SerializedName("email")
    val email: String = "",
    
    @SerializedName("features")
    val features: String = "",
    
    @SerializedName("pflags")
    val pflags: Int = 0,
    
    @SerializedName("hash")
    val hash: String? = null,
    
    @SerializedName("height")
    val height: Int = 0,
    
    @SerializedName("id")
    val id: String = "",
    
    @SerializedName("locked")
    val locked: Boolean = false,
    
    @SerializedName("release")
    val release: Long = 0,
    
    @SerializedName("ts")
    val timestamp: Long = 0,
    
    @SerializedName("title")
    val title: String = "",
    
    @SerializedName("version")
    val version: Int = 1,
    
    @SerializedName("width")
    val width: Int = 0,
    
    @SerializedName("xscreens")
    val xScreens: Int = 0,
    
    @SerializedName("yscreens")
    val yScreens: Int = 0
)
