package com.logs.fileprinter.file.naming

/**
 * Generate a file name that is changeless.
 */
class ChangelessFileNameGenerator internal constructor(private val fileName: String): FileNameGenerator {

    /**
     * Check if file name changeable or not.
     * @return boolean
     */
    override fun isFileNameChangeable(): Boolean {
        return false
    }

    /**
     * Generate a file name which represent a date.
     *
     * @param logLevel for log level
     * @param timeStamp for date timeStamp
     * @return string
     */
    override fun generateFileName(logLevel: Int, timeStamp: Long): String {
        return fileName
    }
}