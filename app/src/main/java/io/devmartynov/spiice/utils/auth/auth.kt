package io.devmartynov.spiice.utils.auth

import java.util.UUID

/**
 * Хэширует пароль
 * @param password пароль который нужно захэшировать
 * @return захэшированный пароль
 */
fun getHashedPassword(password: String): String {
    return "00 $password 11"
}

/**
 * Генерирует токен авторизации
 * @return токен
 */
fun generateToken(): String {
    return UUID.randomUUID().toString()
}