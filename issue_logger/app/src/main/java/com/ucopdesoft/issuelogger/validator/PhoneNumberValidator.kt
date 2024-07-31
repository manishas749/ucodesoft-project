package com.ucopdesoft.issuelogger.validator

import java.util.regex.Matcher
import java.util.regex.Pattern

class PhoneNumberValidator {
    companion object {
        /**
         * validation for phone number
         */
        fun isValidPhone(phone: String): Boolean {
            val expression = "^([0-9+]|\\(\\d{1,3}\\))[0-9\\-. ]{3,15}$"
            val inputString: CharSequence = phone
            val pattern: Pattern = Pattern.compile(expression)
            val matcher: Matcher = pattern.matcher(inputString)
            return matcher.matches()
        }

    }
}