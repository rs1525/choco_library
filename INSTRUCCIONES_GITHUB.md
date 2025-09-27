# 🚀 Instrucciones para Subir Choco a GitHub

## 📋 Pasos a Seguir:

### 1. Crear Repositorio en GitHub

1. Ve a: **https://github.com/new**
2. Llena los datos:
   - **Repository name**: `choco_library`
   - **Description**: `🍫 Librería moderna para proveedores de widgets y wallpapers KWGT/KLWP en Android`
   - **Visibility**: ✅ Public
   - **❌ NO marques**: "Add a README file"
   - **❌ NO marques**: "Add .gitignore" 
   - **❌ NO marques**: "Choose a license"
3. Haz click en **"Create repository"**

### 2. Subir el Código

Después de crear el repositorio, ejecuta estos comandos en PowerShell:

```powershell
# Navegar al directorio del proyecto
cd "C:\Users\Rainerc\Desktop\libreria_apps\choco_libreria"

# Conectar con el repositorio remoto (cambia 'tu-usuario' por tu usuario de GitHub)
git remote add origin https://github.com/tu-usuario/choco.git

# Subir el código
git push -u origin main
```

### 3. Verificar que se subió correctamente

- Ve a tu repositorio en GitHub
- Deberías ver todos los archivos de la librería
- El README.md debería mostrarse automáticamente

## 🧪 Cómo Probar la Librería

### Opción 1: Usando JitPack (Recomendado)

Una vez que esté en GitHub, puedes usar JitPack para probarla:

1. Ve a: **https://jitpack.io**
2. Pega la URL de tu repositorio: `https://github.com/tu-usuario/choco`
3. Haz click en "Get it"
4. Copia la dependencia que te genere

Ejemplo de uso en un proyecto Android:

```kotlin
// En build.gradle.kts (Module: app)
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.tu-usuario:choco:main-SNAPSHOT")
}
```

### Opción 2: Módulo Local

Puedes crear una app de prueba en el mismo proyecto:

```kotlin
// En settings.gradle.kts
include(":app", ":choco")

// En build.gradle.kts de la app
dependencies {
    implementation(project(":choco"))
}
```

## 📱 App de Prueba Rápida

Aquí tienes el código mínimo para probar Choco:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val choco = Choco.getInstance(this)
        
        // Probar carga de widgets
        choco.loadWidgets { widgets ->
            Log.d("Choco", "Cargados ${widgets.size} widgets")
            widgets.forEach { widget ->
                Log.d("Choco", "Widget: ${widget.title}")
            }
        }
        
        // Probar carga de wallpapers
        choco.loadWallpapers { wallpapers ->
            Log.d("Choco", "Cargados ${wallpapers.size} wallpapers")
        }
    }
}
```

## 🎯 Archivos de Prueba

Para probar completamente, necesitas agregar algunos archivos .kwgt y .klwp en:
- `app/src/main/assets/widgets/` (archivos .kwgt)
- `app/src/main/assets/wallpapers/` (archivos .klwp)

## ✅ Checklist de Verificación

- [ ] Repositorio creado en GitHub
- [ ] Código subido correctamente
- [ ] README.md visible en GitHub
- [ ] JitPack puede acceder al repositorio
- [ ] App de prueba creada
- [ ] Archivos de prueba agregados
- [ ] Librería funciona correctamente

## 🆘 Si Tienes Problemas

1. **Error "Repository not found"**: Asegúrate de haber creado el repositorio en GitHub primero
2. **Error de autenticación**: Configura tu token de GitHub o usa GitHub Desktop
3. **JitPack no funciona**: Espera unos minutos, JitPack tarda en indexar repositorios nuevos

---

¡Una vez que esté en GitHub, tu librería Choco estará disponible para que cualquier desarrollador la use! 🎉
