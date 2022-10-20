package io.devmartynov.spiice

private const val NAME_MIN_LENGTH = 2
private const val NAME_MAX_LENGTH = 20
private const val PASSWORD_MIN_LENGTH = 10
private const val PASSWORD_MAX_LENGTH = 25
private const val PASSWORD_MIN_CHAR_COUNT = 5
private val digitRegex = "[0-9]+".toRegex()
private val letterRegex = "[a-zA-z]+".toRegex()
private val onlyLetterRegex = "^\\p{L}+\$".toRegex()

/**
 * Валидирует значение.
 * Правила валидации определяются атрибутом поля.
 * @param value значение для валидации
 * @param attribute атрибут поля
 * @return результат валидации
 */
fun validate(value: String, attribute: AuthAttributes): ValidationResult = when (attribute) {
    AuthAttributes.EMAIL -> validateEmail(value)
    AuthAttributes.PASSWORD -> validatePassword(value)
    AuthAttributes.FIRST_NAME -> validateName(value)
    AuthAttributes.LAST_NAME -> validateName(value)
}

/**
 * Валидирует E-mail.
 * @param email значение для валидации
 * @return результат валидации
 */
private fun validateEmail(email: String): ValidationResult {
    return object : ValidationResult {
        val hasErrors = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        override fun hasErrors(): Boolean {
            return hasErrors
        }

        override fun getErrors(): List<String> {
            return if (hasErrors) listOf("Некорректный E-mail") else emptyList()
        }
    }
}

/**
 * Валидирует пароль.
 * @param password значение для валидации
 * @return результат валидации
 */
private fun validatePassword(password: String): ValidationResult {
    val errors = arrayListOf<String>()

    if (password.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH) {
        errors.add("Поле должно содержать минимум $PASSWORD_MIN_LENGTH символ и максимум  $PASSWORD_MAX_LENGTH символов")
    }

    if (!password.contains(digitRegex)) {
        errors.add("Поле должно содержать хотя бы одну цифру")
    }

    if (password
            .filter { char -> letterRegex.matches(char.toString()) }
            .length < PASSWORD_MIN_CHAR_COUNT
        ) {
        errors.add("Поле должно содержать хотя бы $PASSWORD_MIN_CHAR_COUNT букв")
    }

    return object : ValidationResult {
        override fun hasErrors(): Boolean {
            return errors.isNotEmpty()
        }

        override fun getErrors(): List<String> {
            return errors
        }
    }
}

/**
 * Валидирует имя/фамилию/отчество.
 * @param name значение для валидации
 * @return результат валидации
 */
private fun validateName(name: String): ValidationResult {
    val errors = arrayListOf<String>()

    if (name.length !in NAME_MIN_LENGTH..NAME_MAX_LENGTH) {
        errors.add("Поле должно содержать минимум $NAME_MIN_LENGTH символа и максимум  $NAME_MAX_LENGTH символов")
    }

    if (!name.contains(onlyLetterRegex)) {
        errors.add("Поле должно содержать только буквы")
    }

    return object : ValidationResult {
        override fun hasErrors(): Boolean {
            return errors.isNotEmpty()
        }

        override fun getErrors(): List<String> {
            return errors
        }
    }
}