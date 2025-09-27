# 🍫 Choco - Librería para Proveedores KWGT/KLWP

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/akustom15/choco)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

**Choco** es una librería moderna y fácil de usar para Android que permite crear aplicaciones proveedoras de widgets y wallpapers para **KWGT** (Kustom Widget Maker) y **KLWP** (Kustom Live Wallpaper).

## ✨ Características

- 🚀 **Fácil de integrar**: Solo unas pocas líneas de código
- 📱 **Moderna**: Desarrollada con Kotlin y las mejores prácticas
- 🎨 **Interfaz incluida**: Adapters y ViewHolders listos para usar
- 🔍 **Búsqueda integrada**: Sistema de búsqueda y filtrado
- 📦 **Gestión automática**: Manejo automático de archivos ZIP y JSON
- 🖼️ **Vista previa**: Carga automática de imágenes de vista previa
- ⚡ **Asíncrona**: Operaciones no bloqueantes con Coroutines
- 🛡️ **Robusta**: Manejo completo de errores y excepciones
- 📚 **Bien documentada**: Documentación completa y ejemplos

## 📋 Requisitos

- Android API 21+ (Android 5.0)
- Kotlin 1.8+
- KWGT y/o KLWP instalados en el dispositivo

## 🚀 Instalación

### Gradle (Recomendado)

Agrega esto a tu archivo `build.gradle` del módulo:

```kotlin
dependencies {
    implementation 'com.akustom15:choco:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.akustom15</groupId>
    <artifactId>choco</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 📁 Estructura del Proyecto

Antes de usar Choco, organiza tus archivos de la siguiente manera:

```
app/
├── src/main/assets/
│   ├── widgets/          # Archivos .kwgt aquí
│   │   ├── widget1.kwgt
│   │   ├── widget2.kwgt
│   │   └── ...
│   └── wallpapers/       # Archivos .klwp aquí
│       ├── wallpaper1.klwp
│       ├── wallpaper2.klwp
│       └── ...
```

## 🔧 Configuración

### 1. Permisos en AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 2. Providers en AndroidManifest.xml

```xml
<application>
    <!-- Provider para KWGT -->
    <provider
        android:name="com.akustom15.choco.providers.KwgtProvider"
        android:authorities="${applicationId}.kwgt"
        android:exported="true"
        android:grantUriPermissions="true">
        <intent-filter>
            <action android:name="org.kustom.provider.KUSTOM_PACK" />
            <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>
        <meta-data
            android:name="org.kustom.provider.KUSTOM_PACK_TYPE"
            android:value="KWGT" />
    </provider>

    <!-- Provider para KLWP -->
    <provider
        android:name="com.akustom15.choco.providers.KlwpProvider"
        android:authorities="${applicationId}.klwp"
        android:exported="true"
        android:grantUriPermissions="true">
        <intent-filter>
            <action android:name="org.kustom.provider.KUSTOM_PACK" />
            <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>
        <meta-data
            android:name="org.kustom.provider.KUSTOM_PACK_TYPE"
            android:value="KLWP" />
    </provider>
</application>
```

## 💻 Uso Básico

### Inicialización

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var choco: Choco
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Inicializar Choco
        choco = Choco.getInstance(this)
        
        // Habilitar logging de debug (opcional)
        ChocoLogger.setDebugEnabled(BuildConfig.DEBUG)
    }
}
```

### Cargar Widgets

```kotlin
// Cargar widgets de forma asíncrona
choco.loadWidgets { widgets ->
    // Usar los widgets cargados
    widgets.forEach { widget ->
        println("Widget: ${widget.title} por ${widget.author}")
        println("Tamaño: ${widget.getSizeString()}")
        println("Descripción: ${widget.description}")
    }
}

// O de forma síncrona (en un hilo de fondo)
lifecycleScope.launch {
    val widgets = choco.loadWidgetsSync()
    // Procesar widgets...
}
```

### Cargar Wallpapers

```kotlin
// Cargar wallpapers de forma asíncrona
choco.loadWallpapers { wallpapers ->
    // Usar los wallpapers cargados
    wallpapers.forEach { wallpaper ->
        println("Wallpaper: ${wallpaper.title}")
        println("Resolución: ${wallpaper.getResolutionString()}")
        println("Pantallas: ${wallpaper.screens}")
    }
}
```

### Búsqueda

```kotlin
// Buscar widgets
choco.searchWidgets("reloj") { results ->
    println("Encontrados ${results.size} widgets con 'reloj'")
}

// Buscar wallpapers
choco.searchWallpapers("minimal") { results ->
    println("Encontrados ${results.size} wallpapers con 'minimal'")
}
```

### Filtrar por Categoría

```kotlin
// Widgets por categoría
choco.getWidgetsByCategory("Reloj") { clockWidgets ->
    // Mostrar solo widgets de reloj
}

// Wallpapers por categoría
choco.getWallpapersByCategory("Minimal") { minimalWallpapers ->
    // Mostrar solo wallpapers minimalistas
}
```

## 🎨 Interfaz de Usuario

Choco incluye adapters listos para usar con RecyclerView:

### Adapter para Widgets

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var widgetAdapter: WidgetAdapter
    private lateinit var recyclerView: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupRecyclerView()
        loadWidgets()
    }
    
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        widgetAdapter = WidgetAdapter(emptyList()) { widget ->
            // Manejar click en widget
            Toast.makeText(this, "Seleccionado: ${widget.title}", Toast.LENGTH_SHORT).show()
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = widgetAdapter
        }
    }
    
    private fun loadWidgets() {
        choco.loadWidgets { widgets ->
            widgetAdapter.updateWidgets(widgets)
        }
    }
}
```

### Adapter para Wallpapers

```kotlin
private fun setupWallpaperRecyclerView() {
    val wallpaperAdapter = WallpaperAdapter(emptyList()) { wallpaper ->
        // Manejar click en wallpaper
        Toast.makeText(this, "Seleccionado: ${wallpaper.title}", Toast.LENGTH_SHORT).show()
    }
    
    recyclerView.adapter = wallpaperAdapter
    
    choco.loadWallpapers { wallpapers ->
        wallpaperAdapter.updateWallpapers(wallpapers)
    }
}
```

## 🔍 Búsqueda Avanzada

```kotlin
class SearchActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var widgetAdapter: WidgetAdapter
    private var allWidgets: List<ChocoWidget> = emptyList()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        
        setupSearchView()
        loadAllWidgets()
    }
    
    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                widgetAdapter.filter(newText ?: "", allWidgets)
                return true
            }
        })
    }
    
    private fun loadAllWidgets() {
        choco.loadWidgets { widgets ->
            allWidgets = widgets
            widgetAdapter.updateWidgets(widgets)
        }
    }
}
```

## 📊 Modelos de Datos

### ChocoWidget

```kotlin
data class ChocoWidget(
    val id: String,                    // ID único
    val title: String,                 // Título del widget
    val description: String,           // Descripción
    val author: String,                // Autor
    val email: String,                 // Email del autor
    val version: Int,                  // Versión
    val width: Int,                    // Ancho en píxeles
    val height: Int,                   // Alto en píxeles
    val previewPortrait: Bitmap?,      // Vista previa vertical
    val previewLandscape: Bitmap?,     // Vista previa horizontal
    val kwgtFile: File,                // Archivo .kwgt
    val features: String,              // Características
    val category: String               // Categoría
)
```

### ChocoWallpaper

```kotlin
data class ChocoWallpaper(
    val id: String,                    // ID único
    val title: String,                 // Título del wallpaper
    val description: String,           // Descripción
    val author: String,                // Autor
    val email: String,                 // Email del autor
    val version: Int,                  // Versión
    val width: Int,                    // Ancho en píxeles
    val height: Int,                   // Alto en píxeles
    val previewPortrait: Bitmap?,      // Vista previa vertical
    val previewLandscape: Bitmap?,     // Vista previa horizontal
    val klwpFile: File,                // Archivo .klwp
    val features: String,              // Características
    val category: String,              // Categoría
    val screens: Int                   // Número de pantallas
)
```

## 🛠️ Utilidades

### ChocoLogger

```kotlin
// Habilitar logging de debug
ChocoLogger.setDebugEnabled(true)

// Diferentes niveles de log
ChocoLogger.d("Mensaje de debug")
ChocoLogger.i("Mensaje de información")
ChocoLogger.w("Mensaje de advertencia")
ChocoLogger.e("Mensaje de error")

// Logs específicos
ChocoLogger.logWidgetLoad(count = 10, timeMs = 1500)
ChocoLogger.logSearch(query = "reloj", results = 5, timeMs = 200)
```

### ChocoConstants

```kotlin
// Usar constantes predefinidas
val widgetsDir = ChocoConstants.WIDGETS_ASSETS_DIR
val kwgtExtension = ChocoConstants.KWGT_EXTENSION
val clockCategory = ChocoConstants.CATEGORY_CLOCK
```

## ⚠️ Manejo de Errores

Choco incluye excepciones específicas para diferentes tipos de errores:

```kotlin
try {
    val widgets = choco.loadWidgetsSync()
} catch (e: FileLoadException) {
    // Error cargando archivo
    ChocoLogger.e("Error cargando archivo", e)
} catch (e: JsonParseException) {
    // Error procesando JSON
    ChocoLogger.e("Error procesando JSON", e)
} catch (e: ExtractionException) {
    // Error extrayendo ZIP
    ChocoLogger.e("Error extrayendo archivo", e)
}
```

## 🎯 Mejores Prácticas

### 1. Gestión de Memoria

```kotlin
override fun onDestroy() {
    super.onDestroy()
    // Limpiar caché cuando sea necesario
    choco.clearCache()
    
    // Liberar recursos al cerrar la aplicación
    choco.release()
}
```

### 2. Carga Eficiente

```kotlin
// Cargar solo cuando sea necesario
if (widgets.isEmpty()) {
    choco.loadWidgets { loadedWidgets ->
        widgets = loadedWidgets
        updateUI()
    }
}
```

### 3. Búsqueda Optimizada

```kotlin
// Implementar debounce para búsqueda
private var searchJob: Job? = null

private fun performSearch(query: String) {
    searchJob?.cancel()
    searchJob = lifecycleScope.launch {
        delay(300) // Debounce de 300ms
        choco.searchWidgets(query) { results ->
            updateSearchResults(results)
        }
    }
}
```

## 📱 Ejemplo Completo

Aquí tienes un ejemplo completo de una aplicación que usa Choco:

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var choco: Choco
    private lateinit var widgetAdapter: WidgetAdapter
    private lateinit var wallpaperAdapter: WallpaperAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeChoco()
        setupUI()
        loadContent()
    }
    
    private fun initializeChoco() {
        choco = Choco.getInstance(this)
        ChocoLogger.setDebugEnabled(BuildConfig.DEBUG)
    }
    
    private fun setupUI() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        
        val fragments = listOf(
            WidgetsFragment(),
            WallpapersFragment()
        )
        
        val adapter = ViewPagerAdapter(this, fragments)
        viewPager.adapter = adapter
        
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Widgets"
                1 -> "Wallpapers"
                else -> ""
            }
        }.attach()
    }
    
    private fun loadContent() {
        // Cargar widgets
        choco.loadWidgets { widgets ->
            ChocoLogger.i("Cargados ${widgets.size} widgets")
            // Actualizar UI...
        }
        
        // Cargar wallpapers
        choco.loadWallpapers { wallpapers ->
            ChocoLogger.i("Cargados ${wallpapers.size} wallpapers")
            // Actualizar UI...
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        choco.clearCache()
    }
}
```

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**AKustom15**
- Email: akustom15@gmail.com
- GitHub: [@akustom15](https://github.com/akustom15)

## 🙏 Agradecimientos

- Al equipo de Kustom por crear KWGT y KLWP
- A la comunidad de Android por las librerías utilizadas
- A todos los contribuidores del proyecto

## 📞 Soporte

Si tienes problemas o preguntas:

1. Revisa la [documentación](README.md)
2. Busca en los [issues existentes](https://github.com/akustom15/choco/issues)
3. Crea un [nuevo issue](https://github.com/akustom15/choco/issues/new)
4. Contacta por email: akustom15@gmail.com

---

⭐ Si te gusta este proyecto, ¡dale una estrella en GitHub!
