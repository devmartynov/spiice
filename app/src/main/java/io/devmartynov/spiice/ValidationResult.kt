package io.devmartynov.spiice

/**
 * Результат валидации.
 */
interface ValidationResult {
    /**
     * Есть ли ошибки валидации?
     * @return true если ошибки есть, иначе false
     */
    fun hasErrors(): Boolean
    /**
     * Ошибки валидации
     * @return если ошибки есть, то список ошибок, иначе пустой список
     */
    fun getErrors(): List<String>
}