package com.ucopdesoft.issuelogger.login__authentication

import android.app.Activity
import androidx.lifecycle.MutableLiveData

interface MobileValidationHandler {

    suspend fun passMobileNumber(
        phoneNumber: String,
        phoneNumberErrorMessage: MutableLiveData<String>,
        activity: Activity
    )

    suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)

    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)

    suspend fun isUserVerified(): Boolean

    suspend fun setVerificationId(verificationId: String)

    fun verifyOtpCode(otpCode: String)

    fun setPhoneCallbacksListener(listener : PhoneCallBackListener)

}