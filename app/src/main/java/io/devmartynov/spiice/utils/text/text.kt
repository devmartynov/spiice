package io.devmartynov.spiice.utils.text

/**
 * Форматирует список строк в строку с разделителем
 * @param list список строк
 * @return форматированную строку
 */
fun beautifyListString(list: List<String>): String {
    return list.joinToString(separator = "\n")
}