package io.devmartynov.spiice.utils.text

/**
 * Форматирует список строк в строку с разделителем
 * @param list список строк
 * @return форматированную строку
 */
fun beautifyListString(list: List<String>): String {
    return list.joinToString(separator = "\n")
}

private val NOTES_ENDING = listOf("заметок", "заметка", "заметки")

/**
 * Склоняет слово взявисимости от переданного кол-ва сущностию
 *
 * Пример: getWordDeclensionByNumber(3, NOTES_ENDING) -> заметки
 * Пример: getWordDeclensionByNumber(1, NOTES_ENDING) -> заметка
 * Пример: getWordDeclensionByNumber(0, NOTES_ENDING) -> заметок
 */
fun getWordDeclensionByNumber(number: Int, words: List<String> = NOTES_ENDING): String = when {
    number in 11..14 -> words[0]
    number % 10 == 1 && number % 100 == 1 -> words[1]
    number % 10 in 2..4 -> words[2]
    else -> words[0]
}