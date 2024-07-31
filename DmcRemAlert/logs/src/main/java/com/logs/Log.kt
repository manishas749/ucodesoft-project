package com.logs

import android.content.Context
import android.util.Log
import com.logs.Constants.Companion.FILE_PATH
import com.logs.Constants.Companion.VERSION_CODE
import com.logs.Constants.Companion.VERSION_NAME
import com.logs.fileprinter.file.FilePrinter
import com.logs.fileprinter.file.naming.DateFileNameGenerator
import com.logs.fileprinter.file.path.FileDirectory
import com.logs.fileprinter.file.writer.SimpleWriter
import com.logs.formatter.message.json.DefaultJsonFormatter
import com.logs.formatter.message.xml.DefaultXmlFormatter
import java.io.File

/**
 * This class is used for initialize object of logger class , so we can use this for print logs.
 */
class Log {

    companion object {

        private var logger: Logger? = null
        fun init(): Logger {
            if (logger == null) {
                val jsonFormatter = DefaultJsonFormatter()
                val xmlFormatter = DefaultXmlFormatter()
                val logConfigBuilder =
                    LogConfig.Builder().logLevel(1).tag("TAG").enableStackTrace(5)
                        .jsonFormatter(jsonFormatter).xmlFormatter(xmlFormatter)
                val logConfig = LogConfig(logConfigBuilder)
                logger = Logger.Builder().printers(getFilePrinter())
                    .logLevel(logConfig.logLevel)
                    .tag(logConfig.tag)
                    .jsonFormatter(logConfig.jsonFormatter)
                    .xmlFormatter(logConfig.xmlFormatter)
                    .build()
                return logger!!
            }

            return logger!!
        }


        /**
         * Create log file.
         * @param versionCode integer
         * @param versionName string
         * @param context
         */
        fun createFilePrinter(versionName: String, versionCode: Int, context: Context) {
            FILE_PATH = FileDirectory(context).getFilePath()
            VERSION_NAME = versionName
            VERSION_CODE = versionCode
            customFileCreate(versionName, versionCode)
        }

        /**
         * Create file with device information header.
         * @param versionCode integer
         * @param versionName string
         */
        private fun customFileCreate(versionName: String, versionCode: Int) {
            FilePrinter.Builder(
                FILE_PATH
            ).fileNameGenerator(DateFileNameGenerator())
                .writer(object : SimpleWriter() {
                    @Override
                    override fun onNewFileCreated(file: File) {
                        super.onNewFileCreated(file)
                        val header = getBuildConfigConstant(versionName, versionCode)
                        appendLog(header)
                    }
                })
                .build()
        }

        /**
         * Get config constant.
         * @param versionName string
         * @param versionCode integer
         * @return string
         */
        fun getBuildConfigConstant(versionName: String, versionCode: Int): String {
            return BuildConfigConstant.getDeviceInformation(
                versionName,
                versionCode
            )

        }

        /**
         * Get file printer object
         * @return FilePrinter
         */
        private fun getFilePrinter(): FilePrinter {

            return FilePrinter.Builder(
                FILE_PATH
            ).fileNameGenerator(DateFileNameGenerator())
                .writer(SimpleWriter())
                .build()
        }
    }


}