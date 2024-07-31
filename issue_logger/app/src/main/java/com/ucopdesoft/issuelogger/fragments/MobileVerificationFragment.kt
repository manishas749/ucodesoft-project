package com.ucopdesoft.issuelogger.fragments


import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.core.Context
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.FragmentMobileVerificationBinding
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import com.ucopdesoft.issuelogger.viewmodels.SendOtpViewModel


class MobileVerificationFragment : Fragment() {

    private lateinit var viewBinding: FragmentMobileVerificationBinding
    private lateinit var viewModel: SendOtpViewModel
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mobile_verification,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[SendOtpViewModel::class.java]

        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
            val wic = WindowInsetsControllerCompat(this, decorView)
            wic.isAppearanceLightStatusBars = true
            navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        }

        viewModel.requestPermission(requireContext())
        viewModel.setUpPasscodeFunctionality(
            viewBinding.passcodeEt1,
            viewBinding.passcodeEt2,
            viewBinding.passcodeEt3,
            viewBinding.passcodeEt4,
            viewBinding.passcodeEt5,
            viewBinding.passcodeEt6
        )

        viewBinding.viewModel = viewModel

        viewBinding.lifecycleOwner = this
        viewModel.countDownTime.observe(viewLifecycleOwner) {
            if (it == resources.getString(R.string.resendCode)) {
                viewBinding.resendCode.apply {
                    text = it
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.app_default))
                    setOnClickListener {
                        viewModel.resendOtp(phoneNumber, requireActivity())
                    }
                }
            } else {
                viewBinding.resendCode.text = resources.getString(R.string.resendCode).plus(": $it")
            }
        }

        viewBinding.signup.setOnClickListener{
            findNavController().navigate(R.id.action_mobileVerificationFragment_to_signingUpFragment2)

        }
        viewBinding.backPress.setOnClickListener {
            viewBinding.layoutLogin.visibility = View.VISIBLE
            viewBinding.layoutOtpverification.visibility = View.GONE
        }

        viewModel.checkOtpDetected.observe(viewLifecycleOwner) {
            if (it) {
                viewBinding.numberTv.text = phoneNumber
               /* viewBinding.editBtnTv.setOnClickListener {
                    viewBinding.inputMobile.text = null
                    phoneNumber = ""
                    viewBinding.layoutLogin.visibility = View.VISIBLE
                    viewBinding.layoutOtpverification.visibility = View.GONE
                    viewBinding.processingLayout.visibility = View.GONE
                }*/

                viewBinding.passcodeEt1.requestFocus()

                viewBinding.processingLayout.visibility = View.GONE
                viewBinding.layoutOtpverification.visibility = View.VISIBLE
                viewBinding.layoutLogin.visibility = View.GONE
            }
        }

        viewModel.internetCheckMessage.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.pleasecheckinternet),
                Toast.LENGTH_SHORT
            ).show()
        }

        return viewBinding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.buttonGetOtp.setOnClickListener {
            if (viewBinding.inputMobile.text!!.isNotEmpty())
            {
                phoneNumber =
                    "+91" + viewBinding.inputMobile.text.toString()
                viewModel.setMobileNumber(phoneNumber)
                viewModel.validateMobileNumber(requireActivity())
                viewBinding.layoutLogin.visibility = View.GONE
                viewBinding.layoutOtpverification.visibility = View.GONE
                viewBinding.processingLayout.visibility = View.VISIBLE
                hideKeyboard()
            }
            else
            {
                viewBinding.inputMobile.error = "Please Enter Mobile Number"
            }

        }

        viewBinding.verify.setOnClickListener {
            viewBinding.processingTv.text = resources.getString(R.string.verifying_otp)
            viewBinding.layoutLogin.visibility = View.GONE
            viewBinding.layoutOtpverification.visibility = View.GONE
            viewBinding.processingLayout.visibility = View.VISIBLE
            verifyOtp()
        }

        viewModel.message.observe(viewLifecycleOwner) {
            if (it == resources.getString(R.string.otpverificationfailed)) {
                viewBinding.processingLayout.visibility = View.GONE
                viewBinding.layoutLogin.visibility = View.VISIBLE
            }
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.otpVerified.observe(viewLifecycleOwner) {
            if (it.first) {
                UserDetailsPreference(requireContext()).saveToken(it.second)
                findNavController().navigate(R.id.action_mobileVerificationFragment_to_mainFragment)
            } else {
                viewBinding.processingLayout.visibility = View.GONE
                viewBinding.layoutLogin.visibility = View.GONE
                viewBinding.layoutOtpverification.visibility = View.VISIBLE
                viewBinding.passcodeEt6.requestFocus()
            }
        }
    }

    private fun verifyOtp() {
        val otpText = viewBinding.passcodeEt1.text.toString()
            .plus(viewBinding.passcodeEt2.text)
            .plus(viewBinding.passcodeEt3.text)
            .plus(viewBinding.passcodeEt4.text)
            .plus(viewBinding.passcodeEt5.text)
            .plus(viewBinding.passcodeEt6.text)
        viewModel.setOtpText(otpText)
        viewModel.verifyOtpCode(otpText)
    }
}


fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}

@BindingAdapter("app:errorText")
fun setErrorTxt(view: EditText, errorMessage: String) {
    if (errorMessage.isEmpty()) view.error = null
    else view.error = errorMessage
}