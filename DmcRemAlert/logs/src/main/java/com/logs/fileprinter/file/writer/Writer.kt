package com.logs.fileprinter.file.writer

import java.io.File

/**
 * Writer is used to write log into log file.
 */
abstract class Writer {

    /**
     * Open a specific log file for writing, if doesn't active, then create it.
     */
    abstract fun open(file: File?): Boolean

    /**
     * If log file is opened then true, otherwise false.
     */
    abstract fun isOpened(): Boolean

    /**
     * Get the opened log file.
     */
    abstract fun getOpenedFile(): File

    /**
     * Get the name of opened log file.
     */
    abstract fun getOpenedFileName(): String?

    /**
     * Append the log to the end of the log file.
     */
    abstract fun appendLog(log: String): Boolean

    /**
     * If the log file is successfully closed then true, otherwise false.
     */
    abstract fun close(): Boolean

    /**
     * Function for create new log file.
     */
    abstract fun createNewFile(file: File?): Boolean
}