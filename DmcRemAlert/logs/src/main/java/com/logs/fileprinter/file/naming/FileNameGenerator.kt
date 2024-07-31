package com.logs.fileprinter.file.naming

/**
 * Generates names for log files.
 */
interface FileNameGenerator {

   /**
    * Check the generated file name will change or not.
    */
   fun isFileNameChangeable(): Boolean

   /**
    * Generate file name for specified log level and timestamp.
    * @param logLevel for log level
    * @param timeStamp for date timeStamp
    * @return string
    */
   fun generateFileName(logLevel: Int, timeStamp: Long): String

}