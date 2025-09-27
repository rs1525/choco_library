package com.akustom15.choco.utils

/**
 * Constantes utilizadas en la librería Choco
 */
object ChocoConstants {
    
    // Directorios de assets
    const val WIDGETS_ASSETS_DIR = "widgets"
    const val WALLPAPERS_ASSETS_DIR = "wallpapers"
    
    // Extensiones de archivos
    const val KWGT_EXTENSION = ".kwgt"
    const val KLWP_EXTENSION = ".klwp"
    const val JSON_EXTENSION = ".json"
    
    // Nombres de archivos dentro de los paquetes
    const val PRESET_JSON_FILE = "preset.json"
    const val PREVIEW_PORTRAIT_FILE = "preset_thumb_portrait.jpg"
    const val PREVIEW_LANDSCAPE_FILE = "preset_thumb_landscape.jpg"
    
    // Authorities para los providers
    const val KWGT_PROVIDER_SUFFIX = ".kwgt"
    const val KLWP_PROVIDER_SUFFIX = ".klwp"
    
    // Paths para los providers
    const val WIDGETS_PATH = "widgets"
    const val WALLPAPERS_PATH = "wallpapers"
    
    // MIME types
    const val MIME_TYPE_ZIP = "application/zip"
    const val MIME_TYPE_WIDGET_DIR = "vnd.android.cursor.dir/vnd.kustom.widget"
    const val MIME_TYPE_WALLPAPER_DIR = "vnd.android.cursor.dir/vnd.kustom.wallpaper"
    
    // Kustom features
    const val FEATURE_ANALOG_CLOCK = "ANALOG_CLOCK"
    const val FEATURE_DIGITAL_CLOCK = "DIGITAL_CLOCK"
    const val FEATURE_WEATHER = "WEATHER"
    const val FEATURE_MUSIC = "MUSIC"
    const val FEATURE_BATTERY = "BATTERY"
    const val FEATURE_CALENDAR = "CALENDAR"
    const val FEATURE_SYSTEM_INFO = "SYSTEM_INFO"
    
    // Categorías predefinidas
    const val CATEGORY_GENERAL = "General"
    const val CATEGORY_CLOCK = "Reloj"
    const val CATEGORY_WEATHER = "Clima"
    const val CATEGORY_MUSIC = "Música"
    const val CATEGORY_SYSTEM = "Sistema"
    const val CATEGORY_CALENDAR = "Calendario"
    const val CATEGORY_BATTERY = "Batería"
    
    // Tamaños de widget comunes (en celdas)
    const val WIDGET_SIZE_1x1 = "1x1"
    const val WIDGET_SIZE_2x1 = "2x1"
    const val WIDGET_SIZE_2x2 = "2x2"
    const val WIDGET_SIZE_4x1 = "4x1"
    const val WIDGET_SIZE_4x2 = "4x2"
    const val WIDGET_SIZE_4x4 = "4x4"
    
    // Resoluciones de pantalla comunes
    const val RESOLUTION_HD = "1280x720"
    const val RESOLUTION_FHD = "1920x1080"
    const val RESOLUTION_QHD = "2560x1440"
    const val RESOLUTION_4K = "3840x2160"
    
    // Configuración de caché
    const val CACHE_DIR_NAME = "choco_cache"
    const val TEMP_DIR_NAME = "choco_temp"
    const val MAX_CACHE_SIZE_MB = 100L
    
    // Configuración de carga
    const val MAX_CONCURRENT_LOADS = 3
    const val LOAD_TIMEOUT_MS = 30000L
    
    // Versión de la librería
    const val LIBRARY_VERSION = "1.0.0"
    const val LIBRARY_NAME = "Choco"
    const val LIBRARY_PACKAGE = "com.akustom15.choco"
    
    // Intents de Kustom
    const val KUSTOM_PACK_ACTION = "org.kustom.provider.KUSTOM_PACK"
    const val KUSTOM_PACK_TYPE_META = "org.kustom.provider.KUSTOM_PACK_TYPE"
    const val KUSTOM_PACK_TYPE_KWGT = "KWGT"
    const val KUSTOM_PACK_TYPE_KLWP = "KLWP"
    
    // Configuración de vista previa
    const val PREVIEW_MAX_WIDTH = 400
    const val PREVIEW_MAX_HEIGHT = 600
    const val PREVIEW_QUALITY = 85
    
    // Configuración de búsqueda
    const val MIN_SEARCH_QUERY_LENGTH = 2
    const val MAX_SEARCH_RESULTS = 100
}
