package com.akustom15.choco.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * Representa la estructura completa de un preset de KWGT/KLWP
 */
data class PresetData(
    @SerializedName("preset_info")
    val presetInfo: PresetInfo,
    
    @SerializedName("preset_root")
    val presetRoot: JsonObject
) {
    /**
     * Convierte PresetData a ChocoWidget
     */
    fun toChocoWidget(kwgtFile: java.io.File): ChocoWidget {
        return ChocoWidget(
            id = presetInfo.id,
            title = presetInfo.title,
            description = presetInfo.description,
            author = presetInfo.author,
            email = presetInfo.email,
            version = presetInfo.version,
            width = presetInfo.width,
            height = presetInfo.height,
            kwgtFile = kwgtFile,
            features = presetInfo.features
        )
    }
    
    /**
     * Convierte PresetData a ChocoWallpaper
     */
    fun toChocoWallpaper(klwpFile: java.io.File): ChocoWallpaper {
        return ChocoWallpaper(
            id = presetInfo.id,
            title = presetInfo.title,
            description = presetInfo.description,
            author = presetInfo.author,
            email = presetInfo.email,
            version = presetInfo.version,
            width = presetInfo.width,
            height = presetInfo.height,
            klwpFile = klwpFile,
            features = presetInfo.features,
            screens = maxOf(presetInfo.xScreens, presetInfo.yScreens, 1)
        )
    }
}
