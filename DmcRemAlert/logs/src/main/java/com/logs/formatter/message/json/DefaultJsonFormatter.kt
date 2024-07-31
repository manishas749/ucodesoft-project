package com.logs.formatter.message.json

import com.logs.Constants
import org.json.JSONArray
import org.json.JSONObject

/**
 * Simply format the JSON using JSONObject.
 */
class DefaultJsonFormatter : JsonFormatter {

    private val jsonIndent = 4

    /**
     * Format the JSON string.
     * @param data string
     * @return string
     */
    override fun format(data: String): String {
        var formattedString: String = Constants.EMPTY_STRING

        if (data.trim().isEmpty()) {
            return formattedString
        }

        try {
            formattedString = if (data.startsWith("{")) {
                val jsonObject = JSONObject(data)
                jsonObject.toString(jsonIndent)
            } else if (data.startsWith("[")) {
                val jsonArray = JSONArray(data)
                jsonArray.toString(jsonIndent)
            } else {
                return data
            }
        } catch (e: Exception) {
            return data
        }

        return formattedString

    }
}