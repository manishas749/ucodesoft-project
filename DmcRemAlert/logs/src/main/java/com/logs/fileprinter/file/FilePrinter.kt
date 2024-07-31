package com.logs.fileprinter.file


import android.util.Log
import com.logs.Constants.Companion.FILE_PATH
import com.logs.Constants.Companion.VERSION_CODE
import com.logs.Constants.Companion.VERSION_NAME
import com.logs.Log.Companion.getBuildConfigConstant
import com.logs.LogLevel
import com.logs.fileprinter.Printer
import com.logs.fileprinter.file.naming.FileNameGenerator
import com.logs.fileprinter.file.writer.Writer
import com.logs.formatter.stacktrace.DefaultStackTraceFormatter
import com.logs.storage.StorageCheck
import com.logs.utils.StackTraceUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Use the Builder to construct a FilePrinter object for create a file with mobile device information.
 */
class FilePrinter internal constructor(builder: Builder) : Printer {

    /**
     * Folder path of log file.
     */
    private var folderPath: String = "FOLDER_PATH"

    /**
     * For the file name generator of log file.
     */
    private val fileNameGenerator: FileNameGenerator

    /**
     * To write log into log file.
     */
    private val writer: Writer

    /**
     * Initialize folderPath, fileNameGenerator and write for log file.
     */
    init {
        folderPath = builder.folderPath
        fileNameGenerator = builder.fileNameGenerator!!
        writer = builder.writer!!
    }


    /**
     * Builder for FilePrinter.
     */
    class Builder(var folderPath: String) {

        /**
         * For the file name generator of log file.
         */
        var fileNameGenerator: FileNameGenerator? = null

        /**
         * To write log into log file.
         */
        var writer: Writer? = null

        /**
         * Set the file name generator for log file.
         */
        fun fileNameGenerator(fileNameGenerator: FileNameGenerator): Builder {
            this.fileNameGenerator = fileNameGenerator
            return this
        }

        /**
         * Set the writer to write log into log file.
         */
        fun writer(writer: Writer?): Builder {
            writer!!.open(File(folderPath, "LogFile"))
            this.writer = writer
            return this
        }

        /**
         * Build configured FilePrinter object.
         */
        fun build(): FilePrinter {
            return FilePrinter(this)
        }

    }

    /**
     * The channel variable is used for queue the print log one by one.
     */
    private val channel = Channel<Boolean>(10000)

    /**
     * Println function used for print logLevel , tag and message in logfile.
     * @param logLevel string (WARN, VERBOSE, DEBUG, ERROR, INFO)
     * @param tag string
     * @param msg message (Throwable message or other string).
     */
    override fun println(logLevel: String, tag: String, msg: String) {
        val currentTime = getCurrentTime()
        val string = "\n$currentTime, $logLevel, $tag, $msg"
        val availMemory  = StorageCheck().getInternalMemoryInfo()
        if (availMemory > 100){
            doPrintln(string)
        }
    }

    /**
     * PrintRuntimeTrace function used for print runtime error message.
     * @param msg message (Throwable message or other string).
     * @param stackStraceElement Array of StackTraceElement.
     */
    fun printRuntimeTrace(msg: String, stackStraceElement: Array<StackTraceElement>) {
        val stackTrace = StackTraceUtil().getCroppedRealStackTrace(stackStraceElement, "kk", 5)
        val stackTraceString = DefaultStackTraceFormatter().format(stackTrace)

        val string = msg + stackTraceString
        val currentTime = getCurrentTime()
        val stringTrace = "\n$currentTime, Exception , Runtime Exception, $string"

        writer.open(File(folderPath, "LogFile"))
        writer.appendLog(stringTrace)

    }

    override fun printInLogFile(logLevel: String, tag: String, msg: String){
        writer.open(File(folderPath, "LogFile"))
        println(logLevel, tag, msg)
    }

    /**
     * CustomLog function used for print message with fileName.
     * @param msg message (Throwable message or other string).
     * @param logLevel
     * @param tag
     * @param fileName
     */
    override fun customLog(fileName: String, logLevel: String, tag: String, msg: String) {

        if (writer.createNewFile(File(FILE_PATH, fileName))){
            writer.open(File(FILE_PATH, fileName))
            writer.appendLog(getBuildConfigConstant(VERSION_NAME, VERSION_CODE))
        }else{
            writer.open(File(FILE_PATH, fileName))
        }

        println(logLevel, tag, msg)
    }


    /**
     * DoPrintln function used for print log using Coroutine MainScope.
     * @param string log message.
     */
    private fun doPrintln(string: String) {
        MainScope().launch {
            channel.send(writer.appendLog(string))

        }

    }

    /**
     * This function will return current time.
     * @return string
     */
    private fun getCurrentTime(): String {
        val time = System.currentTimeMillis()
        return SimpleDateFormat("dd/MM/yyy HH:mm:ss:SSS 'T'z", Locale.getDefault()).format(time)
    }
}