package com.logs.formatter

/**
 * Formatter is used for format the data. It maybe string or maybe long and any other type of data.
 */
interface Formatter<T> {

    /**
     * For format json, xml, stackTrace, intent bundle into string.
     * @param data T type
     * @return string
     */
    fun format(data: T): String
}