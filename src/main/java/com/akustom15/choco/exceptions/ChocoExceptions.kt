package com.akustom15.choco.exceptions

/**
 * Excepción base para la librería Choco
 */
open class ChocoException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Excepción lanzada cuando no se puede cargar un archivo
 */
class FileLoadException(
    fileName: String,
    cause: Throwable? = null
) : ChocoException("No se pudo cargar el archivo: $fileName", cause)

/**
 * Excepción lanzada cuando un archivo no es válido
 */
class InvalidFileException(
    fileName: String,
    reason: String = "Formato inválido"
) : ChocoException("Archivo inválido '$fileName': $reason")

/**
 * Excepción lanzada cuando falla la extracción de un archivo ZIP
 */
class ExtractionException(
    fileName: String,
    cause: Throwable? = null
) : ChocoException("Error al extraer archivo: $fileName", cause)

/**
 * Excepción lanzada cuando falla el parsing del JSON
 */
class JsonParseException(
    fileName: String,
    cause: Throwable? = null
) : ChocoException("Error al procesar JSON en archivo: $fileName", cause)

/**
 * Excepción lanzada cuando no se encuentra un archivo requerido
 */
class RequiredFileNotFoundException(
    fileName: String,
    containerFile: String
) : ChocoException("Archivo requerido '$fileName' no encontrado en '$containerFile'")

/**
 * Excepción lanzada cuando falla la carga de vista previa
 */
class PreviewLoadException(
    fileName: String,
    cause: Throwable? = null
) : ChocoException("Error al cargar vista previa: $fileName", cause)

/**
 * Excepción lanzada cuando el provider no está configurado correctamente
 */
class ProviderConfigurationException(
    providerType: String,
    reason: String
) : ChocoException("Error de configuración del provider $providerType: $reason")

/**
 * Excepción lanzada cuando se excede el tiempo límite de una operación
 */
class TimeoutException(
    operation: String,
    timeoutMs: Long
) : ChocoException("Timeout en operación '$operation' después de ${timeoutMs}ms")

/**
 * Excepción lanzada cuando no hay suficiente espacio en caché
 */
class CacheFullException(
    requiredSpace: Long,
    availableSpace: Long
) : ChocoException("Cache lleno: se requieren ${requiredSpace}MB, disponibles ${availableSpace}MB")

/**
 * Excepción lanzada cuando una operación no está soportada
 */
class UnsupportedOperationException(
    operation: String
) : ChocoException("Operación no soportada: $operation")

/**
 * Excepción lanzada cuando hay un error de inicialización
 */
class InitializationException(
    component: String,
    cause: Throwable? = null
) : ChocoException("Error al inicializar $component", cause)
