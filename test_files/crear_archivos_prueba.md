# 🧪 Archivos de Prueba para Choco

Para probar completamente la librería Choco, necesitas algunos archivos .kwgt y .klwp de ejemplo.

## 📁 Estructura Requerida

```
tu_app/
├── src/main/assets/
│   ├── widgets/          # Archivos .kwgt aquí
│   │   ├── ejemplo1.kwgt
│   │   ├── ejemplo2.kwgt
│   │   └── ...
│   └── wallpapers/       # Archivos .klwp aquí
│       ├── ejemplo1.klwp
│       ├── ejemplo2.klwp
│       └── ...
```

## 🎯 Dónde Conseguir Archivos de Prueba

### Opción 1: Usar el archivo que ya tienes
Tienes un archivo en: `C:\Users\Rainerc\Desktop\libreria_apps\json_de_widgets\LNX_003.zip`

1. Renómbralo a `LNX_003.kwgt`
2. Cópialo a `assets/widgets/` de tu app de prueba

### Opción 2: Crear archivos de prueba simples

Puedes crear archivos .kwgt/.klwp básicos usando KWGT/KLWP:

1. Abre KWGT o KLWP
2. Crea un widget/wallpaper simple
3. Expórtalo
4. Copia el archivo a assets/

### Opción 3: Descargar ejemplos

Puedes descargar widgets/wallpapers gratuitos de:
- Google Play Store (apps de widgets KWGT)
- Comunidades de Kustom
- GitHub (proyectos open source)

## 🔧 Script para Crear Estructura

```bash
# En tu proyecto Android
mkdir -p app/src/main/assets/widgets
mkdir -p app/src/main/assets/wallpapers

# Copiar archivo de ejemplo
cp "C:\Users\Rainerc\Desktop\libreria_apps\json_de_widgets\LNX_003.zip" app/src/main/assets/widgets/LNX_003.kwgt
```

## ✅ Verificación

Una vez que tengas los archivos, la librería debería:
1. Detectar automáticamente los archivos
2. Extraer la información JSON
3. Cargar las vistas previas
4. Mostrarlos en la interfaz

## 🎮 Prueba Rápida

```kotlin
// En tu MainActivity
val choco = Choco.getInstance(this)

choco.loadWidgets { widgets ->
    if (widgets.isEmpty()) {
        Log.w("Choco", "No se encontraron widgets. Verifica que estén en assets/widgets/")
    } else {
        Log.i("Choco", "¡Éxito! Encontrados ${widgets.size} widgets")
        widgets.forEach { widget ->
            Log.d("Choco", "- ${widget.title} por ${widget.author}")
        }
    }
}
```

---

¡Con estos archivos de prueba podrás verificar que Choco funciona perfectamente! 🚀
