package com.logs.utils


/**
 * Represent a single log going to be printed.
 */
class LogItem
/**
 * Constructor of LogItem
 * @param level int
 * @param tag string
 * @param msg string
 * @param classTag string
 */(
    /**
     * Level of the log.
     */
    var level: Int,
    /**
     * The tag, should not be null.
     */
    var tag: String,
    /**
     * The formatted message.
     */
    var message: String,
    /**
     * The class tag.
     */
    var classTag: String
)