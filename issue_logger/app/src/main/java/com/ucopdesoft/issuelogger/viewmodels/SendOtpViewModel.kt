package com.ucopdesoft.issuelogger.viewmodels

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.login__authentication.PhoneCallBackListener
import com.ucopdesoft.issuelogger.repositories.MobileValidationRepository
import com.ucopdesoft.issuelogger.utils.NetworkCheck
import com.ucopdesoft.issuelogger.utils.NetworkStatus
import kotlinx.coroutines.launch

class SendOtpViewModel(private val app: Application) : AndroidViewModel(app),
    PhoneCallBackListener {


    private val statusMessage = MutableLiveData<String>()
    private var auth: FirebaseAuth = Firebase.auth
    private var connectionLiveData = NetworkCheck(app.applicationContext)
    private var etList: ArrayList<EditText> = arrayListOf()
    private var etPos = 0


    init {
        auth = FirebaseAuth.getInstance()
    }


    val message: LiveData<String>
        get() = statusMessage
    private var timer: CountDownTimer? = null

    private val _internetCheckMessage = MutableLiveData<Boolean>()
    val internetCheckMessage: LiveData<Boolean> = _internetCheckMessage


    private val _countDownTime = MutableLiveData<String>()
    val countDownTime: LiveData<String> = _countDownTime

    private val _otp = MutableLiveData<String>()
    val otp: LiveData<String> = _otp

    private val _mobileNumber = MutableLiveData<String>()
    val mobileNumber: MutableLiveData<String> = _mobileNumber


    private val _checkOtpDetected = MutableLiveData<Boolean>()
    val checkOtpDetected: LiveData<Boolean> = _checkOtpDetected


    private val _otpVerified = MutableLiveData<Pair<Boolean, String>>()
    val otpVerified: LiveData<Pair<Boolean, String>> = _otpVerified
    private val repository: MobileValidationRepository =
        MobileValidationRepository(app.applicationContext)

    private val _phoneNumberErrorMessage = MutableLiveData("")
    val phoneNumberErrorMessage: LiveData<String>
        get() = _phoneNumberErrorMessage

    init {
        repository.setPhoneCallbacksListener(this)
    }

    /**
     * this function is for the validation of phone number and once validation is successful it will send
     * otp using firebase verification
     */
    private fun validatePhoneNumber(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            repository.passMobileNumber(phoneNumber, _phoneNumberErrorMessage, activity)
        }
    }

    /**
     * This function will start the timer once the otp has been send
     */

    private fun startCountDown() {
        val startTime = 60
        timer?.cancel()
        timer = object : CountDownTimer(startTime * 1000.toLong(), 1000) {
            override fun onTick(p0: Long) {
                _countDownTime.value = (p0 / 1000).toInt().toString()
            }

            override fun onFinish() {
                _countDownTime.value = app.getString(R.string.resendCode)
            }
        }
        timer?.start()

    }

    /**
     * This is to set otp value from the OTP textBox
     */

    fun setMobileNumber(Number: String) {
        _mobileNumber.value = Number
    }

    fun setOtpText(otp: String) {
        _otp.value = otp
    }


    /**
     * Once the timer is stopped and if we click on resend otp it will resent the OTP
     */
    fun resendOtp(phoneNo: String, activity: Activity) {
        viewModelScope.launch {
            repository.resendOtpCode(phoneNo, activity)
        }
    }

    /**
     *  it is to verify OTP if it is correct
     */
    fun verifyOtpCode(otpCode: String) {
        repository.verifyOtpCode(otpCode = otpCode)
    }

    /**
     * Will show the status message once the otp verified successful
     */
    override fun onOtpVerifyCompleted() {
        statusMessage.value = app.getString(R.string.otpverifiedsuccessfully)
        _otpVerified.value = Pair(true, auth.uid!!)
    }

    /**
     * Will show the status message once the otp verified failed
     */
    override fun onOtpVerifyFailed(message: String) {
        statusMessage.value = app.getString(R.string.otpverificationfailed)
        _otpVerified.value = Pair(false, "")
    }

    /**
     * Will save the otp value
     */
    override fun onVerificationCodeDetected(otpCode: String) {
        _otp.value = otpCode
    }

    /**
     * Will show the status message once the otp verified failed
     */
    override fun onVerificationFailed(message: String) {
        statusMessage.value = app.getString(R.string.otpverificationfailed)
    }

    /**
     * this function will start once the code has been start and then timer will start
     */
    override fun onCodeSent(
        verificationId: String?,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        startCountDown()
        _checkOtpDetected.value = true
    }

    fun validateMobileNumber(activity: Activity) {
        if (connectionLiveData.isOnline() == NetworkStatus.CONNECTED) {
            _mobileNumber.value?.let {
                _checkOtpDetected.postValue(false)
                validatePhoneNumber(it, activity)

            }
        } else {
            _internetCheckMessage.value = true
        }
    }

    fun setUpPasscodeFunctionality(
        passcodeEt1: EditText, passcodeEt2: EditText, passcodeEt3: EditText,
        passcodeEt4: EditText, passcodeEt5: EditText, passcodeEt6: EditText
    ) {
        etList = arrayListOf(
            passcodeEt1,
            passcodeEt2,
            passcodeEt3,
            passcodeEt4,
            passcodeEt5,
            passcodeEt6
        )

        etList.forEach {
            it.addTextChangedListener(OtpWatcher(it))
            it.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_DEL) onKeyListener()
                if (keyCode == KeyEvent.ACTION_DOWN) {
                    val otpText = passcodeEt1.text.toString()
                        .plus(passcodeEt2.text)
                        .plus(passcodeEt3.text)
                        .plus(passcodeEt4.text)
                        .plus(passcodeEt5.text)
                        .plus(passcodeEt6.text)
                    setOtpText(otpText)
                    verifyOtpCode(otpText)
                }
                false
            }
        }
    }

    private fun onKeyListener() {
        try {
            val edtView: EditText = etList[etPos]
            if (edtView.text.trim().isEmpty() && etPos > 0) {
                etPos -= 1
                etList[etPos].requestFocus()
            } else edtView.requestFocus()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    inner class OtpWatcher(private val v: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            when (v.id) {
                R.id.passcodeEt1 -> changeFocus(text, 0, 1)

                R.id.passcodeEt2 -> changeFocus(text, 1, 2)

                R.id.passcodeEt3 -> changeFocus(text, 2, 3)

                R.id.passcodeEt4 -> changeFocus(text, 3, 4)

                R.id.passcodeEt5 -> changeFocus(text, 4, 5)

                R.id.passcodeEt6 -> changeFocus(text, 5, 5)

                else -> {
                    if (text.isEmpty()) etList[5].requestFocus()
                }
            }
        }
    }

    private fun changeFocus(text: String, previous: Int, next: Int) {
        etPos = next - 1
        etList[if (text.isEmpty()) previous else next].requestFocus()
    }


    fun requestPermission(context: android.content.Context) {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        }
    }

}



