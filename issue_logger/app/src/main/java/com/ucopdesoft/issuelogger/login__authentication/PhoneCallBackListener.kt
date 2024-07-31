package com.ucopdesoft.issuelogger.login__authentication

import com.google.firebase.auth.PhoneAuthProvider

interface PhoneCallBackListener {

    fun onOtpVerifyCompleted()
    fun onOtpVerifyFailed(message: String)
    fun onVerificationCodeDetected(otpCode: String)
    fun onVerificationFailed(message: String)
    fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?)
}