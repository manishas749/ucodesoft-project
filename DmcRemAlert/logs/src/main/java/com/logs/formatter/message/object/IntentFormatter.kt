package com.logs.formatter.message.`object`

import android.content.Intent
import com.logs.utils.ObjectToStringUtil

/**
 * Format an Intent object to a string.
 */
class IntentFormatter : ObjectFormatter<Intent> {

    /**
     * Format an Intent object to a string.
     * @param data intent
     * @return string
     */
    override fun format(data: Intent): String {
        return ObjectToStringUtil().intentToString(data)
    }
}