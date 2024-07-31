package com.logs.formatter.stacktrace

import com.logs.Constants

/**
 * Formatted stack trace looks like:
 * <br>	├ com.log.MainClass.sampleMethodC(SampleClassC.java:198)
 * <br>	├ com.log.MainClass.sampleMethodB(SampleClassB.java:101)
 * <br>	└ com.log.MainClass.sampleMethodA(SampleClassA.java:51)
 */
class DefaultStackTraceFormatter: StackTraceFormatter {

    /**
     * Formatted stack trace with use of StackTraceElement.
     * @param data array of stack trace element.
     * @return string
     */
    override fun format(data: Array<StackTraceElement?>): String {
        val sb = StringBuilder(256)
        return if (data.isEmpty()) {
           return Constants.EMPTY_STRING
        } else if (data.size == 1) {
            "\t─ " + data[0].toString()
        } else {
            var i = 0
            val n: Int = data.size
            while (i < n) {
                if (i != n - 1) {
                    sb.append("\n├ ")
                    sb.append(data[i].toString())
                  //  sb.append(SystemCompat.lineSeparator)
                } else {
                    sb.append("\n└ ")
                    sb.append(data[i].toString())
                }
                i++
            }
            sb.toString()
        }
    }
}