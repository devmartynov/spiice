package io.devmartynov.spiice

interface ValidationResult {
    fun hasErrors(): Boolean
    fun getErrors(): List<String>
}