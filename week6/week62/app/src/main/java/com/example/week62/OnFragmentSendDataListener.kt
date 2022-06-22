package com.example.week62

/**
 * Интерфес для остановки/ запуска и сброса
 */
interface OnFragmentSendDataListener {

    /**
     * Изменеие статуса генерации
     *
     * @return новый статус
     */
    fun changeGenerateStatus():Boolean

    /**
     * Сброс числа
     */
    fun resetNumber()
}