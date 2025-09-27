package com.akustom15.choco.utils

import com.akustom15.choco.models.PresetData
import java.io.File
import java.util.regex.Pattern

/**
 * Utilidades de validación para la librería Choco
 */
object ValidationUtils {
    
    // Patrones de validación
    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    
    private val UUID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    )
    
    private val SAFE_FILENAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]+$"
    )
    
    /**
     * Valida si un email tiene formato válido
     * 
     * @param email Email a validar
     * @return True si es válido
     */
    fun isValidEmail(email: String?): Boolean {
        return email != null && EMAIL_PATTERN.matcher(email).matches()
    }
    
    /**
     * Valida si un UUID tiene formato válido
     * 
     * @param uuid UUID a validar
     * @return True si es válido
     */
    fun isValidUUID(uuid: String?): Boolean {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches()
    }
    
    /**
     * Valida si un nombre de archivo es seguro
     * 
     * @param filename Nombre de archivo a validar
     * @return True si es seguro
     */
    fun isSafeFilename(filename: String?): Boolean {
        return filename != null && 
               filename.isNotBlank() && 
               filename.length <= 255 &&
               SAFE_FILENAME_PATTERN.matcher(filename).matches()
    }
    
    /**
     * Valida si un archivo KWGT/KLWP es válido
     * 
     * @param file Archivo a validar
     * @return ValidationResult con el resultado
     */
    fun validateKustomFile(file: File): ValidationResult {
        // Verificar que el archivo existe
        if (!file.exists()) {
            return ValidationResult.error("El archivo no existe: ${file.name}")
        }
        
        // Verificar que se puede leer
        if (!file.canRead()) {
            return ValidationResult.error("No se puede leer el archivo: ${file.name}")
        }
        
        // Verificar extensión
        val extension = file.extension.lowercase()
        if (extension != "kwgt" && extension != "klwp") {
            return ValidationResult.error("Extensión de archivo no válida: $extension")
        }
        
        // Verificar tamaño (máximo 50MB)
        val maxSize = 50 * 1024 * 1024 // 50MB
        if (file.length() > maxSize) {
            return ValidationResult.error("Archivo demasiado grande: ${file.length() / (1024 * 1024)}MB")
        }
        
        // Verificar que es un archivo ZIP válido
        if (!FileUtils.isValidKustomFile(file)) {
            return ValidationResult.error("El archivo no es un archivo Kustom válido")
        }
        
        return ValidationResult.success("Archivo válido")
    }
    
    /**
     * Valida los datos de un preset
     * 
     * @param presetData Datos del preset a validar
     * @return ValidationResult con el resultado
     */
    fun validatePresetData(presetData: PresetData): ValidationResult {
        val info = presetData.presetInfo
        val errors = mutableListOf<String>()
        
        // Validar campos obligatorios
        if (info.id.isBlank()) {
            errors.add("ID del preset no puede estar vacío")
        } else if (!isValidUUID(info.id)) {
            errors.add("ID del preset debe ser un UUID válido")
        }
        
        if (info.title.isBlank()) {
            errors.add("Título del preset no puede estar vacío")
        } else if (info.title.length > 100) {
            errors.add("Título del preset demasiado largo (máximo 100 caracteres)")
        }
        
        if (info.author.isBlank()) {
            errors.add("Autor del preset no puede estar vacío")
        } else if (info.author.length > 50) {
            errors.add("Nombre del autor demasiado largo (máximo 50 caracteres)")
        }
        
        // Validar email si está presente
        if (info.email.isNotBlank() && !isValidEmail(info.email)) {
            errors.add("Email del autor no tiene formato válido")
        }
        
        // Validar dimensiones
        if (info.width <= 0 || info.height <= 0) {
            errors.add("Dimensiones del preset deben ser positivas")
        }
        
        if (info.width > 4000 || info.height > 4000) {
            errors.add("Dimensiones del preset demasiado grandes (máximo 4000px)")
        }
        
        // Validar versión
        if (info.version < 1) {
            errors.add("Versión del preset debe ser mayor a 0")
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.success("Preset válido")
        } else {
            ValidationResult.error(errors.joinToString("; "))
        }
    }
    
    /**
     * Valida una consulta de búsqueda
     * 
     * @param query Consulta a validar
     * @return ValidationResult con el resultado
     */
    fun validateSearchQuery(query: String?): ValidationResult {
        if (query == null) {
            return ValidationResult.error("Consulta de búsqueda no puede ser null")
        }
        
        if (query.isBlank()) {
            return ValidationResult.success("Consulta vacía válida")
        }
        
        if (query.length < ChocoConstants.MIN_SEARCH_QUERY_LENGTH) {
            return ValidationResult.error("Consulta demasiado corta (mínimo ${ChocoConstants.MIN_SEARCH_QUERY_LENGTH} caracteres)")
        }
        
        if (query.length > 100) {
            return ValidationResult.error("Consulta demasiado larga (máximo 100 caracteres)")
        }
        
        // Verificar caracteres peligrosos
        val dangerousChars = listOf("<", ">", "\"", "'", "&", ";", "|")
        for (char in dangerousChars) {
            if (query.contains(char)) {
                return ValidationResult.error("Consulta contiene caracteres no permitidos")
            }
        }
        
        return ValidationResult.success("Consulta válida")
    }
    
    /**
     * Valida un nombre de categoría
     * 
     * @param category Categoría a validar
     * @return ValidationResult con el resultado
     */
    fun validateCategory(category: String?): ValidationResult {
        if (category == null || category.isBlank()) {
            return ValidationResult.error("Categoría no puede estar vacía")
        }
        
        if (category.length > 30) {
            return ValidationResult.error("Nombre de categoría demasiado largo (máximo 30 caracteres)")
        }
        
        if (!category.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+$"))) {
            return ValidationResult.error("Categoría contiene caracteres no válidos")
        }
        
        return ValidationResult.success("Categoría válida")
    }
    
    /**
     * Sanitiza una cadena de texto para uso seguro
     * 
     * @param input Texto a sanitizar
     * @param maxLength Longitud máxima permitida
     * @return Texto sanitizado
     */
    fun sanitizeString(input: String?, maxLength: Int = 255): String {
        if (input == null) return ""
        
        return input
            .trim()
            .take(maxLength)
            .replace(Regex("[<>\"'&;|]"), "")
            .replace(Regex("\\s+"), " ")
    }
    
    /**
     * Valida un directorio de assets
     * 
     * @param context Contexto de la aplicación
     * @param assetPath Ruta del directorio en assets
     * @return ValidationResult con el resultado
     */
    fun validateAssetsDirectory(context: android.content.Context, assetPath: String): ValidationResult {
        return try {
            val files = context.assets.list(assetPath)
            if (files == null || files.isEmpty()) {
                ValidationResult.warning("Directorio de assets vacío: $assetPath")
            } else {
                ValidationResult.success("Directorio válido con ${files.size} archivos")
            }
        } catch (e: Exception) {
            ValidationResult.error("Error accediendo al directorio de assets: ${e.message}")
        }
    }
    
    /**
     * Resultado de una validación
     */
    data class ValidationResult(
        val isValid: Boolean,
        val isWarning: Boolean,
        val message: String
    ) {
        companion object {
            fun success(message: String) = ValidationResult(true, false, message)
            fun warning(message: String) = ValidationResult(true, true, message)
            fun error(message: String) = ValidationResult(false, false, message)
        }
        
        val isError: Boolean get() = !isValid
        val isSuccess: Boolean get() = isValid && !isWarning
    }
}
