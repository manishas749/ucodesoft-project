package com.logs.fileprinter.file.naming

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Generate file name according to the timestamp, different dates will lead to different file names.
 */
class DateFileNameGenerator : FileNameGenerator {

    /**
     * Variable for local date format (yyyy-MM-dd).
     */
    var localDateFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy-MM-dd", Locale.US)
            }
        }

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
        val simpleDateFormat: SimpleDateFormat = localDateFormat.get() as SimpleDateFormat
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.format(timeStamp)
    }

}