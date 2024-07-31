package com.logs.sharefile

import android.content.Context

/**
 * This ShareFile used to send file via email.
 */
interface ShareFile {

    /**
     * Function used for send email.
     * @param context
     * @param provider string
     */
    fun sendEmail(context: Context, provider: String)
}