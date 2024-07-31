package com.logs.fileprinter.file.writer

import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * For initialize work to new file and add file header.
 */
open class SimpleWriter : Writer() {

    /**
     * Name of log file.
     */
    private var logFileName: String? = null

    /**
     * Log file.
     */
    private var logFile: File? = null

    /**
     *Buffer Writer for write log in file.
     */
    private var bufferedWriter: BufferedWriter? = null


    /**
     * Open a specific log file for writing, if doesn't active, then create it.
     * @param file
     * @return boolean
     */
    override fun open(file: File?): Boolean {
        logFileName = file!!.name
        logFile = file

        var isNewFile = false

        /**
         * Create log file if not exists.
         */
        if (!logFile!!.exists()) {
            isNewFile = try {
                val parent = logFile!!.parentFile
                if (parent != null) {
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                }
                logFile!!.createNewFile()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                close()
                return false
            }
        }

        /**
         * Create buffered writer.
         */
        try {
            bufferedWriter = BufferedWriter(FileWriter(logFile, true))
            if (isNewFile) {
                onNewFileCreated(logFile!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            close()
            return false
        }
        return true
    }

    /**
     * Function for create new log file.
     * @param file
     * @return boolean if file created then true, otherwise false.
     */
    override fun createNewFile(file: File?): Boolean{
        logFile = file
        var isNewFile = false
        if (!logFile!!.exists()) {
            isNewFile = try {
                val parent = logFile!!.parentFile
                if (parent != null) {
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                }
                logFile!!.createNewFile()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                close()
                return false
            }
        }

        return isNewFile
    }

    /**
     * Function for check buffer write is not null and file exits or not.
     * @return boolean
     */
    override fun isOpened(): Boolean {
        return bufferedWriter != null && logFile!!.exists()
    }

    /**
     * For get open file.
     * @return File
     */
    override fun getOpenedFile(): File {
        return logFile!!
    }

    /**
     * For get open file name.
     * @return string
     */
    override fun getOpenedFileName(): String? {
        return logFileName
    }

    /**
     * For write the log in LogFile.
     * @param log string log message.
     * @return boolean
     */
    override fun appendLog(log: String): Boolean {
        try {
            bufferedWriter!!.write(log)
            bufferedWriter!!.newLine()
            bufferedWriter!!.flush()
            return true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * If the log file is successfully closed then true, otherwise false.
     * @return boolean
     */
    override fun close(): Boolean {
        if (bufferedWriter != null) {
            try {
                bufferedWriter!!.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        bufferedWriter = null
        logFileName = null
        logFile = null
        return true
    }

    /**
     * We can do some initialization work to the new file, such as calling appendLog.
     * @param file
     */
    @Override
    open fun onNewFileCreated(file: File){}

}