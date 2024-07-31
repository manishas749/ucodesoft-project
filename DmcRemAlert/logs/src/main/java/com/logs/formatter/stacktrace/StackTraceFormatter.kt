package com.logs.formatter.stacktrace

import com.logs.formatter.Formatter

/**
 * The stack trace formatter used to format the stack trace when logging.
 */
interface StackTraceFormatter: Formatter<Array<StackTraceElement?>>