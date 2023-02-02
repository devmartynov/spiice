package io.devmartynov.spiice.utils

interface Callback : java.io.Serializable {
    fun invoke()
}