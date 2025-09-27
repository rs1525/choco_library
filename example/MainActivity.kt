package com.akustom15.choco.example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akustom15.choco.Choco
import com.akustom15.choco.models.ChocoWidget
import com.akustom15.choco.models.ChocoWallpaper
import com.akustom15.choco.ui.WidgetAdapter
import com.akustom15.choco.ui.WallpaperAdapter
import com.akustom15.choco.utils.ChocoLogger
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Ejemplo de actividad que demuestra el uso de la librería Choco
 * 
 * Esta actividad muestra cómo:
 * - Inicializar Choco
 * - Cargar widgets y wallpapers
 * - Implementar búsqueda
 * - Manejar clicks en elementos
 * - Usar los adapters incluidos
 */
class MainActivity : AppCompatActivity() {
    
    // Instancia de Choco
    private lateinit var choco: Choco
    
    // UI Components
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    
    // Adapters
    private lateinit var widgetAdapter: WidgetAdapter
    private lateinit var wallpaperAdapter: WallpaperAdapter
    
    // Data
    private var allWidgets: List<ChocoWidget> = emptyList()
    private var allWallpapers: List<ChocoWallpaper> = emptyList()
    private var currentTab = 0
    
    // Search
    private var searchJob: Job? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeChoco()
        setupUI()
        loadContent()
    }
    
    /**
     * Inicializa la librería Choco
     */
    private fun initializeChoco() {
        choco = Choco.getInstance(this)
        
        // Habilitar logging de debug en modo debug
        ChocoLogger.setDebugEnabled(BuildConfig.DEBUG)
        
        ChocoLogger.i("Choco inicializado correctamente")
    }
    
    /**
     * Configura la interfaz de usuario
     */
    private fun setupUI() {
        // Configurar TabLayout
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Widgets"))
        tabLayout.addTab(tabLayout.newTab().setText("Wallpapers"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                updateRecyclerView()
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Configurar adapters
        setupAdapters()
        
        // Configurar SearchView
        setupSearchView()
    }
    
    /**
     * Configura los adapters para widgets y wallpapers
     */
    private fun setupAdapters() {
        // Adapter para widgets
        widgetAdapter = WidgetAdapter(emptyList()) { widget ->
            onWidgetClick(widget)
        }
        
        // Adapter para wallpapers
        wallpaperAdapter = WallpaperAdapter(emptyList()) { wallpaper ->
            onWallpaperClick(wallpaper)
        }
        
        // Configurar adapter inicial
        recyclerView.adapter = widgetAdapter
    }
    
    /**
     * Configura el SearchView con debounce
     */
    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText ?: "")
                return true
            }
        })
    }
    
    /**
     * Carga el contenido inicial (widgets y wallpapers)
     */
    private fun loadContent() {
        ChocoLogger.i("Iniciando carga de contenido...")
        
        // Cargar widgets
        loadWidgets()
        
        // Cargar wallpapers
        loadWallpapers()
    }
    
    /**
     * Carga los widgets usando Choco
     */
    private fun loadWidgets() {
        val startTime = System.currentTimeMillis()
        
        choco.loadWidgets { widgets ->
            val loadTime = System.currentTimeMillis() - startTime
            ChocoLogger.logWidgetLoad(widgets.size, loadTime)
            
            allWidgets = widgets
            
            // Actualizar UI si estamos en la pestaña de widgets
            if (currentTab == 0) {
                widgetAdapter.updateWidgets(widgets)
            }
            
            showToast("Cargados ${widgets.size} widgets")
        }
    }
    
    /**
     * Carga los wallpapers usando Choco
     */
    private fun loadWallpapers() {
        val startTime = System.currentTimeMillis()
        
        choco.loadWallpapers { wallpapers ->
            val loadTime = System.currentTimeMillis() - startTime
            ChocoLogger.logWallpaperLoad(wallpapers.size, loadTime)
            
            allWallpapers = wallpapers
            
            // Actualizar UI si estamos en la pestaña de wallpapers
            if (currentTab == 1) {
                wallpaperAdapter.updateWallpapers(wallpapers)
            }
            
            showToast("Cargados ${wallpapers.size} wallpapers")
        }
    }
    
    /**
     * Actualiza el RecyclerView según la pestaña seleccionada
     */
    private fun updateRecyclerView() {
        when (currentTab) {
            0 -> {
                // Pestaña de widgets
                recyclerView.adapter = widgetAdapter
                widgetAdapter.updateWidgets(allWidgets)
                searchView.queryHint = "Buscar widgets..."
            }
            1 -> {
                // Pestaña de wallpapers
                recyclerView.adapter = wallpaperAdapter
                wallpaperAdapter.updateWallpapers(allWallpapers)
                searchView.queryHint = "Buscar wallpapers..."
            }
        }
        
        // Limpiar búsqueda al cambiar de pestaña
        searchView.setQuery("", false)
    }
    
    /**
     * Realiza búsqueda con debounce para evitar demasiadas consultas
     */
    private fun performSearch(query: String) {
        // Cancelar búsqueda anterior
        searchJob?.cancel()
        
        searchJob = lifecycleScope.launch {
            // Debounce de 300ms
            delay(300)
            
            val startTime = System.currentTimeMillis()
            
            when (currentTab) {
                0 -> {
                    // Buscar widgets
                    choco.searchWidgets(query) { results ->
                        val searchTime = System.currentTimeMillis() - startTime
                        ChocoLogger.logSearch(query, results.size, searchTime)
                        
                        widgetAdapter.updateWidgets(results)
                    }
                }
                1 -> {
                    // Buscar wallpapers
                    choco.searchWallpapers(query) { results ->
                        val searchTime = System.currentTimeMillis() - startTime
                        ChocoLogger.logSearch(query, results.size, searchTime)
                        
                        wallpaperAdapter.updateWallpapers(results)
                    }
                }
            }
        }
    }
    
    /**
     * Maneja el click en un widget
     */
    private fun onWidgetClick(widget: ChocoWidget) {
        ChocoLogger.i("Widget seleccionado: ${widget.title}")
        
        val message = """
            Widget: ${widget.title}
            Autor: ${widget.author}
            Tamaño: ${widget.getSizeString()}
            Versión: ${widget.version}
        """.trimIndent()
        
        showToast(message)
        
        // Aquí podrías implementar lógica adicional como:
        // - Mostrar detalles del widget
        // - Aplicar el widget
        // - Compartir el widget
        // - etc.
    }
    
    /**
     * Maneja el click en un wallpaper
     */
    private fun onWallpaperClick(wallpaper: ChocoWallpaper) {
        ChocoLogger.i("Wallpaper seleccionado: ${wallpaper.title}")
        
        val message = """
            Wallpaper: ${wallpaper.title}
            Autor: ${wallpaper.author}
            Resolución: ${wallpaper.getResolutionString()}
            Pantallas: ${wallpaper.screens}
        """.trimIndent()
        
        showToast(message)
        
        // Aquí podrías implementar lógica adicional como:
        // - Mostrar detalles del wallpaper
        // - Aplicar el wallpaper
        // - Compartir el wallpaper
        // - etc.
    }
    
    /**
     * Muestra un Toast con el mensaje especificado
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    /**
     * Ejemplo de cómo filtrar por categoría
     */
    private fun filterByCategory(category: String) {
        when (currentTab) {
            0 -> {
                choco.getWidgetsByCategory(category) { widgets ->
                    widgetAdapter.updateWidgets(widgets)
                    showToast("Mostrando widgets de categoría: $category")
                }
            }
            1 -> {
                choco.getWallpapersByCategory(category) { wallpapers ->
                    wallpaperAdapter.updateWallpapers(wallpapers)
                    showToast("Mostrando wallpapers de categoría: $category")
                }
            }
        }
    }
    
    /**
     * Ejemplo de carga síncrona (usar con cuidado)
     */
    private fun loadContentSync() {
        lifecycleScope.launch {
            try {
                // Cargar widgets de forma síncrona
                val widgets = choco.loadWidgetsSync()
                ChocoLogger.i("Cargados ${widgets.size} widgets síncronamente")
                
                // Cargar wallpapers de forma síncrona
                val wallpapers = choco.loadWallpapersSync()
                ChocoLogger.i("Cargados ${wallpapers.size} wallpapers síncronamente")
                
                // Actualizar UI en el hilo principal
                runOnUiThread {
                    allWidgets = widgets
                    allWallpapers = wallpapers
                    updateRecyclerView()
                }
                
            } catch (e: Exception) {
                ChocoLogger.e("Error cargando contenido síncronamente", e)
                runOnUiThread {
                    showToast("Error cargando contenido: ${e.message}")
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Cancelar trabajos pendientes
        searchJob?.cancel()
        
        // Limpiar caché si es necesario
        choco.clearCache()
        
        ChocoLogger.i("MainActivity destruida, recursos liberados")
    }
}
