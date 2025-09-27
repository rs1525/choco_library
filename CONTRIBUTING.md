# Guía de Contribución

¡Gracias por tu interés en contribuir a Choco! 🍫

Esta guía te ayudará a entender cómo puedes contribuir al proyecto de manera efectiva.

## 📋 Tabla de Contenidos

- [Código de Conducta](#código-de-conducta)
- [¿Cómo puedo contribuir?](#cómo-puedo-contribuir)
- [Reportar Bugs](#reportar-bugs)
- [Sugerir Mejoras](#sugerir-mejoras)
- [Contribuir con Código](#contribuir-con-código)
- [Estilo de Código](#estilo-de-código)
- [Proceso de Pull Request](#proceso-de-pull-request)
- [Configuración del Entorno de Desarrollo](#configuración-del-entorno-de-desarrollo)

## 📜 Código de Conducta

Este proyecto adhiere al [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). Al participar, se espera que mantengas este código. Por favor reporta comportamientos inaceptables a akustom15@gmail.com.

## 🤝 ¿Cómo puedo contribuir?

### Reportar Bugs

Los bugs se rastrean como [GitHub issues](https://github.com/akustom15/choco/issues). Antes de crear un bug report:

1. **Verifica** que no sea un duplicado buscando en issues existentes
2. **Determina** en qué repositorio debería reportarse el bug
3. **Recopila** información sobre el bug

#### ¿Cómo escribir un buen bug report?

Explica el problema e incluye detalles adicionales para ayudar a los mantenedores a reproducir el problema:

- **Usa un título claro y descriptivo**
- **Describe los pasos exactos para reproducir el problema**
- **Proporciona ejemplos específicos**
- **Describe el comportamiento observado y el esperado**
- **Incluye capturas de pantalla si es posible**
- **Especifica la versión de Choco que estás usando**
- **Especifica el nombre y versión del OS**

### Sugerir Mejoras

Las mejoras también se rastrean como [GitHub issues](https://github.com/akustom15/choco/issues). Antes de crear una sugerencia:

1. **Verifica** que no exista ya una sugerencia similar
2. **Determina** si la mejora está relacionada con el core de la librería

#### ¿Cómo escribir una buena sugerencia de mejora?

- **Usa un título claro y descriptivo**
- **Proporciona una descripción paso a paso de la mejora sugerida**
- **Proporciona ejemplos específicos**
- **Describe el comportamiento actual y explica qué comportamiento esperabas**
- **Explica por qué esta mejora sería útil**
- **Especifica la versión de Choco que estás usando**

## 💻 Contribuir con Código

### Configuración del Entorno de Desarrollo

1. **Fork** el repositorio en GitHub
2. **Clona** tu fork localmente:
   ```bash
   git clone https://github.com/tu-usuario/choco.git
   cd choco
   ```
3. **Configura** el repositorio upstream:
   ```bash
   git remote add upstream https://github.com/akustom15/choco.git
   ```
4. **Crea** una rama para tu feature:
   ```bash
   git checkout -b feature/nombre-de-tu-feature
   ```

### Requisitos del Entorno

- **Android Studio** Arctic Fox o superior
- **JDK** 11 o superior
- **Android SDK** con API 21+
- **Kotlin** 1.8+

### Estructura del Proyecto

```
choco/
├── src/main/java/com/akustom15/choco/
│   ├── Choco.kt                    # Clase principal
│   ├── models/                     # Modelos de datos
│   ├── providers/                  # Content Providers
│   ├── ui/                         # Componentes de UI
│   ├── utils/                      # Utilidades
│   └── exceptions/                 # Excepciones personalizadas
├── src/main/res/                   # Recursos Android
├── src/test/                       # Tests unitarios
└── src/androidTest/                # Tests de instrumentación
```

## 🎨 Estilo de Código

### Kotlin

Seguimos las [convenciones de código de Kotlin](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// ✅ Correcto
class ChocoWidget(
    val id: String,
    val title: String,
    val description: String
) {
    fun getSizeString(): String {
        return "${width}x${height}"
    }
}

// ❌ Incorrecto
class chocoWidget(val id:String,val title:String,val description:String){
    fun getSizeString():String{
        return "${width}x${height}"
    }
}
```

### Documentación

- **Documenta** todas las clases y funciones públicas
- **Usa** KDoc para la documentación
- **Incluye** ejemplos cuando sea apropiado

```kotlin
/**
 * Representa un widget de KWGT en la librería Choco
 * 
 * @param id Identificador único del widget
 * @param title Título del widget
 * @param description Descripción del widget
 * 
 * Ejemplo de uso:
 * ```kotlin
 * val widget = ChocoWidget(
 *     id = "widget-001",
 *     title = "Mi Widget",
 *     description = "Un widget increíble"
 * )
 * ```
 */
data class ChocoWidget(
    val id: String,
    val title: String,
    val description: String
)
```

### Naming Conventions

- **Clases**: PascalCase (`ChocoWidget`)
- **Funciones**: camelCase (`loadWidgets`)
- **Variables**: camelCase (`widgetList`)
- **Constantes**: UPPER_SNAKE_CASE (`MAX_WIDGETS`)
- **Packages**: lowercase (`com.akustom15.choco`)

## 🔄 Proceso de Pull Request

1. **Asegúrate** de que tu código sigue el estilo establecido
2. **Actualiza** la documentación si es necesario
3. **Agrega** tests para nueva funcionalidad
4. **Asegúrate** de que todos los tests pasen
5. **Actualiza** el CHANGELOG.md
6. **Crea** el Pull Request con una descripción clara

### Template de Pull Request

```markdown
## Descripción
Breve descripción de los cambios realizados.

## Tipo de cambio
- [ ] Bug fix (cambio que no rompe funcionalidad existente y arregla un issue)
- [ ] Nueva feature (cambio que no rompe funcionalidad existente y agrega funcionalidad)
- [ ] Breaking change (fix o feature que causaría que funcionalidad existente no funcione como se espera)
- [ ] Cambio de documentación

## ¿Cómo se ha probado?
Describe las pruebas que realizaste para verificar tus cambios.

## Checklist:
- [ ] Mi código sigue el estilo de este proyecto
- [ ] He realizado una auto-revisión de mi código
- [ ] He comentado mi código, particularmente en áreas difíciles de entender
- [ ] He realizado los cambios correspondientes a la documentación
- [ ] Mis cambios no generan nuevas advertencias
- [ ] He agregado tests que prueban que mi fix es efectivo o que mi feature funciona
- [ ] Tests unitarios nuevos y existentes pasan localmente con mis cambios
```

## 🧪 Testing

### Tests Unitarios

```kotlin
@Test
fun `loadWidgets should return list of widgets`() {
    // Given
    val choco = Choco.getInstance(context)
    
    // When
    var result: List<ChocoWidget>? = null
    choco.loadWidgets { widgets ->
        result = widgets
    }
    
    // Then
    assertNotNull(result)
    assertTrue(result!!.isNotEmpty())
}
```

### Tests de Instrumentación

```kotlin
@Test
fun testProviderQuery() {
    val uri = Uri.parse("content://com.example.app.kwgt/widgets")
    val cursor = contentResolver.query(uri, null, null, null, null)
    
    assertNotNull(cursor)
    assertTrue(cursor!!.count > 0)
    cursor.close()
}
```

## 📝 Commit Messages

Usa el formato [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>[scope opcional]: <descripción>

[cuerpo opcional]

[footer opcional]
```

### Tipos

- `feat`: Nueva feature
- `fix`: Bug fix
- `docs`: Cambios en documentación
- `style`: Cambios de formato (no afectan el código)
- `refactor`: Refactoring de código
- `test`: Agregar o modificar tests
- `chore`: Cambios en build process o herramientas auxiliares

### Ejemplos

```
feat(widgets): add search functionality to widget loader

fix(providers): resolve null pointer exception in KwgtProvider

docs(readme): update installation instructions

style(ui): format adapter classes according to style guide

refactor(utils): extract file operations to separate utility class

test(models): add unit tests for ChocoWidget class

chore(build): update gradle dependencies
```

## 🏷️ Versionado

Este proyecto usa [Semantic Versioning](https://semver.org/). Las versiones siguen el formato `MAJOR.MINOR.PATCH`:

- **MAJOR**: Cambios incompatibles en la API
- **MINOR**: Funcionalidad agregada de manera compatible
- **PATCH**: Bug fixes compatibles

## 📞 ¿Necesitas Ayuda?

- 📧 **Email**: akustom15@gmail.com
- 💬 **Issues**: [GitHub Issues](https://github.com/akustom15/choco/issues)
- 📖 **Documentación**: [README.md](README.md)

## 🙏 Reconocimientos

¡Gracias a todos los contribuidores que hacen posible este proyecto!

---

¡Esperamos tus contribuciones! 🚀
