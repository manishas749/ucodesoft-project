package com.logs

import android.util.Log
import com.logs.fileprinter.Printer
import com.logs.formatter.message.json.JsonFormatter
import com.logs.formatter.message.`object`.ObjectFormatter
import com.logs.formatter.message.xml.XmlFormatter
import com.logs.formatter.stacktrace.DefaultStackTraceFormatter
import com.logs.utils.StackTraceUtil

/**
 * A logger is used to do the real logging work, can use multiple log printers to print the log.
 */
class Logger {

    /**
     * Object of LogConfig class
     */
    private var logConfig: LogConfig? = null

    /**
     * Log printer used to print the logs.
     */
    private var printer: Printer? = null

    /**
     * Logger class constructor with logConfig and printer params
     * @param logConfig
     * @param printer
     */
    constructor(logConfig: LogConfig, printer: Printer) {
        this.logConfig = logConfig
        this.printer = printer
    }

    /**
     * Logger class constructor with builder class type param.
     * @param builder
     */
    constructor(builder: Builder) {
        val logConfigBuilder: LogConfig.Builder? = null

        if (builder.logLevel != 0) {
            logConfigBuilder?.logLevel(builder.logLevel)
        }

        if (builder.tag != null) {
            logConfigBuilder?.tag(builder.tag!!)
        }

        if (builder.jsonFormatter != null) {
            logConfigBuilder?.jsonFormatter(builder.jsonFormatter!!)
        }

        if (builder.xmlFormatter != null) {
            logConfigBuilder?.xmlFormatter(builder.xmlFormatter!!)
        }

        if (builder.printer != null) {
            printer = builder.printer
        }
    }

    class Builder {
        /**
         * Logs below of which would not be printed.
         */
        var logLevel = 0

        /**
         * The tag string when [Logger] log.
         */
        var tag: String? = null

        /**
         * The JSON formatter when log a JSON string.
         */
        var jsonFormatter: JsonFormatter? = null

        /**
         * The XML formatter when [Logger] log a XML string.
         */
        var xmlFormatter: XmlFormatter? = null

        /**
         * Printer used to print the log.
         */
        var printer: Printer? = null

        /**
         * Set the log level, the logs below of which would not be printed.
         * @param logLevel integer
         * @return Builder class instance
         */
        fun logLevel(logLevel: Int): Builder {
            this.logLevel = logLevel
            return this
        }

        /**
         * Set the tag string when log.
         * @param tag string
         * @return Builder class instance
         */
        fun tag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        /**
         * Set the JSON formatter when log a JSON string.
         * @param jsonFormatter
         * @return Builder class instance
         */
        fun jsonFormatter(jsonFormatter: JsonFormatter?): Builder {
            this.jsonFormatter = jsonFormatter
            return this
        }

        /**
         * Set the XML formatter when log a XML string.
         * @param xmlFormatter
         * @return Builder class instance
         */
        fun xmlFormatter(xmlFormatter: XmlFormatter?): Builder {
            this.xmlFormatter = xmlFormatter
            return this
        }

        /**
         * Set the printer for print log in file.
         * @param printers
         * @return Builder class instance
         */
        fun printers(printers: Printer?): Builder {
            this.printer = printers
            return this
        }

        /**
         * For build the object of Logger.
         * @return Logger
         */
        fun build(): Logger {
            return Logger(this)
        }


    }


    /*package*/
    /**
     * For print in log file.
     * @param logLevel integer
     * @param tag string
     * @param msg string
     */
    private fun println(logLevel: Int, tag: String?, msg: String?) {
        /*   if (logLevel < logConfig!!.logLevel) {
               return
           }  */
        printInternal(logLevel, tag ?: "", msg ?: "")
    }

    /**
     * Print log with VERBOS tag
     * @param tag string
     * @param msg string
     */
    fun v(tag: String, msg: String) {
        println(LogLevel.VERBOS, tag, msg)
    }

    /**
     * Print log with DEBUG tag
     * @param tag string
     * @param msg string
     */
    fun d(tag: String, msg: String) {
        println(LogLevel.DEBUG, tag, msg)
    }

    /**
     * Print log with INFO tag
     * @param tag string
     * @param msg string
     */
    fun i(tag: String, msg: String) {
        println(LogLevel.INFO, tag, msg)
    }

    /**
     * Print log with ERROR tag
     * @param tag string
     * @param msg string
     */
    fun e(tag: String, msg: String) {
        println(LogLevel.ERROR, tag, msg)
    }

    /**
     * Print log with WARN tag
     * @param tag string
     * @param msg string
     */
    fun w(tag: String, msg: String) {
        println(LogLevel.WARN, tag, msg)
    }

    /**
     * Print log with custom file.
     * @param fileName string
     * @param tag string
     * @param msg string
     */
    fun customLog(fileName: String, tag: String, msg: String) {
        val stackTrace = StackTraceUtil().getCroppedRealStackTrace(Throwable().stackTrace, "kk", 5)
        val stackTraceString = DefaultStackTraceFormatter().format(stackTrace)
        val string = msg + stackTraceString
        printer!!.customLog(fileName, LogLevel.getLevelName(LogLevel.DEFAULT_LOG), tag, string)
    }

    /**
     * Log a Json string
     * @param tag string
     * @param json string
     */
    fun json(tag: String?, json: String?) {
        if (LogLevel.DEBUG < logConfig!!.logLevel) {
            return
        }
        printInternal(LogLevel.DEBUG, tag ?: "", logConfig!!.jsonFormatter.format(json!!))
    }

    /**
     * Log a XML string
     * @param tag string
     * @param xml string
     */
    fun xml(tag: String?, xml: String?) {
        if (LogLevel.DEBUG < logConfig!!.logLevel) {
            return
        }
        printInternal(LogLevel.DEBUG, tag ?: "", logConfig!!.xmlFormatter.format(xml!!))
    }


    /**
     * Print object in log file
     * @param logLevel integer
     * @param tag string
     * @param `object` any type of class
     */
    private fun <T> println(logLevel: Int, tag: String, `object`: T?) {
        if (logLevel < logConfig!!.logLevel) {
            return
        }
        val objectString: String = if (`object` != null) {
            val objectFormatter: ObjectFormatter<in T> =
                logConfig!!.getObjectFormatter(`object`) as ObjectFormatter<in T>
            objectFormatter.format(`object`)
        } else {
            "null"
        }
        printInternal(logLevel, tag, objectString)
    }

    /**
     * Print a log in a new line internally.
     * @param logLevel integer
     * @param tag string
     * @param msg string
     */
    private fun printInternal(logLevel: Int, tag: String, msg: String) {

        val stackTrace = StackTraceUtil().getCroppedRealStackTrace(Throwable().stackTrace, "kk", 5)
        val stackTraceString = DefaultStackTraceFormatter().format(stackTrace)

        val string = msg + stackTraceString
       // printer!!.println(LogLevel.getShortLevelName(logLevel), tag, string)
        printer!!.printInLogFile(LogLevel.getShortLevelName(logLevel), tag, string)

    }


}