package io.devmartynov.spiice

fun buildErrorCauseMessage(error: String, cause: Error): String {
    return "$error : ${cause.message}"
}