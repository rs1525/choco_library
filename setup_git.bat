@echo off
echo 🍫 Configurando repositorio Git para Choco...
echo.

REM Inicializar repositorio Git
echo 📁 Inicializando repositorio Git...
git init

REM Configurar usuario (cambiar por tus datos)
echo 👤 Configurando usuario Git...
git config user.name "AKustom15"
git config user.email "akustom15@gmail.com"

REM Agregar todos los archivos
echo 📄 Agregando archivos al repositorio...
git add .

REM Hacer commit inicial
echo 💾 Realizando commit inicial...
git commit -m "🎉 Initial commit: Choco library v1.0.0

✨ Features:
- Complete KWGT/KLWP provider implementation
- Modern Kotlin architecture with coroutines
- Built-in UI adapters and components
- Comprehensive search and filtering
- Automatic ZIP and JSON handling
- Preview image loading
- Robust error handling
- Complete documentation
- Example application included

🛠️ Technical:
- Android API 21+ support
- Kotlin 1.8+ compatibility
- Material Design UI
- ProGuard configuration
- CI/CD with GitHub Actions
- Maven publishing ready"

REM Configurar rama principal
echo 🌿 Configurando rama principal...
git branch -M main

echo.
echo ✅ Repositorio Git configurado correctamente!
echo.
echo 📋 Próximos pasos:
echo 1. Crear repositorio en GitHub: https://github.com/new
echo 2. Ejecutar: git remote add origin https://github.com/akustom15/choco.git
echo 3. Ejecutar: git push -u origin main
echo.
echo 🚀 ¡Tu librería Choco está lista para ser compartida!

pause
