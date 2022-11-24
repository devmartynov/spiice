package io.devmartynov.spiice.utils.asyncOperationState

/**
 * Состояние выполнения асинхронной операции
 */
sealed class AsyncOperationState {
    /**
     * Простой - ничего не выполняется
     */
    object Idle : AsyncOperationState()

    /**
     * Загрузка
     */
    object Loading : AsyncOperationState()

    /**
     * Успешнное выполнение операции
     * @param data результать выполнения асинхронной операции
     */
    class Success(val data: Any): AsyncOperationState()

    /**
     * Выполнение операции с ошибкой
     * @param error ошибка
     */
    class Failure(val error: java.lang.Error): AsyncOperationState()
}