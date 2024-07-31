package com.ucopdesoft.issuelogger.repositories

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.login__authentication.MobileValidationHandler
import com.ucopdesoft.issuelogger.login__authentication.PhoneCallBackListener
import com.ucopdesoft.issuelogger.models.User
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_NAME
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_NUMBER
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_PROFILE_PIC
import com.ucopdesoft.issuelogger.utils.Tables
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import com.ucopdesoft.issuelogger.validator.PhoneNumberValidator
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Repository class for the Mobile OTP verification
 */

class MobileValidationRepository(private val context: Context) : MobileValidationHandler {

    private var auth: FirebaseAuth = Firebase.auth

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var dataBase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var userDetailsPreference = UserDetailsPreference(context)


    private lateinit var phoneCallbacksListener: PhoneCallBackListener
    lateinit var verificationID: String

    override fun setPhoneCallbacksListener(listener: PhoneCallBackListener) {
        this.phoneCallbacksListener = listener
    }

    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                val otpCode = credential.smsCode

                if (!otpCode.isNullOrEmpty()) {
                    phoneCallbacksListener.onVerificationCodeDetected(otpCode = otpCode)
                }
            }

            override fun onVerificationFailed(firebaseException: FirebaseException) {
                when (firebaseException) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        phoneCallbacksListener.onVerificationFailed(context.getString(R.string.wrongNumber))
                    }

                    is FirebaseTooManyRequestsException -> {
                        phoneCallbacksListener.onVerificationFailed(context.getString(R.string.tooManyAttempt))
                    }

                    is FirebaseNetworkException -> {
                        phoneCallbacksListener.onVerificationFailed(context.getString(R.string.error_no_network))

                        val errorText = removeSurroundingString(firebaseException.message)
                        phoneCallbacksListener.onVerificationFailed(errorText)
                    }

                    else -> {
                        phoneCallbacksListener.onVerificationFailed(firebaseException.message ?: "")
                        phoneCallbacksListener.onVerificationFailed(context.getString(R.string.unknownError))
                    }
                }
            }

            override fun onCodeSent(
                verificationId: String,
                _resendToken: PhoneAuthProvider.ForceResendingToken,
            ) {
                super.onCodeSent(verificationId, _resendToken)
                verificationID = verificationId
                resendToken = _resendToken

                phoneCallbacksListener.onCodeSent(verificationId, resendToken)
            }
        }


    override suspend fun passMobileNumber(
        phoneNumber: String,
        phoneNumberErrorMessage: MutableLiveData<String>,
        activity: Activity
    ) {
        phoneNumberErrorMessage.value = ""
        if (phoneNumber.isBlank()) {
            phoneNumberErrorMessage.value =
                context.getString(R.string.enterPhoneNumber)
        } else if (!PhoneNumberValidator.isValidPhone(phoneNumber)) {
            phoneNumberErrorMessage.value =
                context.getString(R.string.notAValidPhoneNumber)

        } else {

            sendOtpToPhone(phoneNumber, activity) // to send otp on the phone number entered
        }
    }

    /**
     * function to send otp once the mobile number is correct
     */
    override suspend fun sendOtpToPhone(
        phoneNumber: String,
        activity: Activity,
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * function to verify otp once entered in the textBox whether it is correct or not
     */

    override fun verifyOtpCode(otpCode: String) {
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationID, otpCode))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                phoneCallbacksListener.onOtpVerifyCompleted()

                val id = it.result.user?.uid
                Log.d("SAVE_USER", "signInWithPhoneAuthCredential: ${it.result.user?.uid}")
                if (id != null) {
                    try {
                        userDetailsPreference.saveToken(id)
                        dataBase.reference.child(Tables.USER.tableName).child(id).get()
                            .addOnCompleteListener { user ->

                                val userMap = user.result.value as Map<*, *>?

                                val phoneNumber = auth.currentUser?.phoneNumber.toString()

                                val addUser = if (userMap != null) {

                                    val userName = userMap[USER_NAME] ?: "User${
                                        phoneNumber.subSequence(
                                            phoneNumber.length - 4,
                                            phoneNumber.length
                                        )
                                    }"
                                    val userNumber = userMap[USER_NUMBER] ?: phoneNumber
                                    val userProfilePic = userMap[USER_PROFILE_PIC] ?: ""
                                    User(
                                        userName.toString(),
                                        userNumber.toString(),
                                        userProfilePic.toString()
                                    )
                                } else {
                                    User(
                                        "User${
                                            phoneNumber.subSequence(
                                                phoneNumber.length - 4,
                                                phoneNumber.length
                                            )
                                        }",
                                        phoneNumber,
                                        ""
                                    )
                                }

                                dataBase.reference.child(Tables.USER.tableName).child(id)
                                    .setValue(addUser)

                            }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                when (it.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        phoneCallbacksListener.onOtpVerifyFailed(context.getString(R.string.wrongOtp))
                    }
                }
            }
        }
    }


    /**
     * function to resent otp once timer is stopped and no OTP received
     */
    override suspend fun resendOtpCode(phoneNumber: String, activity: Activity) {
        val resendCodeOptionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        resendCodeOptionsBuilder.setForceResendingToken(resendToken)
        PhoneAuthProvider.verifyPhoneNumber(resendCodeOptionsBuilder.build())
    }

    override suspend fun isUserVerified(): Boolean = auth.currentUser != null

    override suspend fun setVerificationId(verificationId: String) {
        verificationID = verificationId
    }


    fun removeSurroundingString(str: String?): String {
        var start: Int? = null
        var end: Int? = null


        str!!.forEachIndexed { index, c ->
            if (c.toString() == "(") {
                start = index - 1
            }
            if (c.toString() == ")") {
                end = index + 1
            }
        }

        return if (start != null && end != null) {
            str.removeRange(start!!, end!!)
        } else {
            str
        }
    }
}