package com.logs.formatter.message.`object`

import android.os.Bundle
import com.logs.utils.ObjectToStringUtil

/**
 * Format an Bundle object to a string.
 */
class BundleFormatter : ObjectFormatter<Bundle> {

    /**
     * Format Bundle object to a string.
     * @param data bundle
     * @return string
     */
    override fun format(data: Bundle): String {
        return ObjectToStringUtil().bundleToString(data)
    }
}