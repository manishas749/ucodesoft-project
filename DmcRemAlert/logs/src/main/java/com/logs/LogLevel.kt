package com.logs

import android.util.Log.VERBOSE

/**
 * This class is used for all the logs which is with a log level smaller
 * than the setup one would not be printed.
 */
class LogLevel {

    companion object {

        const val VERBOS = 2

        const val DEBUG = 3

        const val INFO = 4

        const val WARN = 5

        const val ERROR = 6

        const val DEFAULT_LOG = 7

        const val ALL = Int.MIN_VALUE

        const val NONE = Int.MAX_VALUE

        /**
         * Get a name representing the specified log level.
         * @param logLevel integer
         * @return string level name
         */
        fun getLevelName(logLevel: Int): String {

           val levelName = when (logLevel) {
                VERBOS -> "VERBOSE"
                DEBUG -> "DEBUG"
                INFO -> "INFO"
                WARN -> "WARN"
                ERROR -> "ERROR"
                DEFAULT_LOG -> "DEFAULT_LOG"
                else -> {
                    if (logLevel < VERBOSE) {
                        "VERBOSE-" + (VERBOSE - logLevel)
                    } else {
                        "ERROR+" + (logLevel - ERROR)
                    }
                }
            }

            return levelName
        }

        /**
         * Get a short name representing the specified log level.
         * @param logLevel integer
         * @return string short name.
         */
        fun getShortLevelName(logLevel: Int): String {

            val levelName: String = when (logLevel) {
                VERBOSE -> "V"
                DEBUG -> "D"
                INFO -> "I"
                WARN -> "W"
                ERROR -> "E"
                else -> if (logLevel < VERBOSE) {
                    "V-" + (VERBOSE - logLevel)
                } else {
                    "E+" + (logLevel - ERROR)
                }
            }
            return levelName
        }

    }
}