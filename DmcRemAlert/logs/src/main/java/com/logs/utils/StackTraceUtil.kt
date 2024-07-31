package com.logs.utils

import android.util.Log

/**
 * Utility related with stack trace.
 */
class StackTraceUtil {

    /**
     * Get the real stack trace and then crop it with a max depth.
     * @param stackTrace array of stack trace element.
     * @param stackTraceOrigin string
     * @param maxDepth integer
     * @return array of stack trace element
     */
    fun getCroppedRealStackTrace(
        stackTrace: Array<StackTraceElement>,
        stackTraceOrigin: String,
        maxDepth: Int
    ): Array<StackTraceElement?> {
        return getCropStackTrace(
            getRealStackTrace(
                stackTrace,
                stackTraceOrigin
            ), maxDepth
        )
    }


    /**
     * Get the real stack trace, all elements that come from XLog library would be dropped.
     * @param stackTrace array of stack trace element.
     * @param stackTraceOrigin string
     * @return array of stack trace element.
     */
    private fun getRealStackTrace(
        stackTrace: Array<StackTraceElement>,
        stackTraceOrigin: String
    ): Array<StackTraceElement?> {
        var ignoreDepth = 0
        val allDepth = stackTrace.size
        var className: String
        for (i in allDepth - 1 downTo 0) {
            className = stackTrace[i].className
            Log.d("ClassName", className)
            if (className.startsWith("com.logs.Logger") || className.startsWith("com.logs.fileprinter") || className.startsWith(
                    "com.logs.LogRuntimeTrace"
                ) || stackTraceOrigin != null && className.startsWith(
                    stackTraceOrigin
                )
            ) {
                ignoreDepth = i + 1
                break
            }
        }
        val realDepth = allDepth - ignoreDepth
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth)
        return realStack
    }


    /**
     * Crop the stack trace with a max depth.
     * @param callStack array of stack trace element.
     * @param maxDepth integer
     * @return array of stack trace element.
     */
    private fun getCropStackTrace(
        callStack: Array<StackTraceElement?>,
        maxDepth: Int
    ): Array<StackTraceElement?> {
        var realDepth = callStack.size
        if (maxDepth > 0) {
            realDepth = maxDepth.coerceAtMost(realDepth)
        }
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(callStack, 0, realStack, 0, realDepth)
        return realStack
    }
}