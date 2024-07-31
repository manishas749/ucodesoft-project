package com.example.listerpros.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.listerpros.activities.MainActivity
import com.example.listerpros.utils.Helper
import com.example.listerpros.R
import com.example.listerpros.constants.Constants.Companion.BEARER_TOKEN
import com.example.listerpros.databinding.FragmentLoginScreenBinding
import com.example.listerpros.model.login.ResponseLogin
import com.example.listerpros.preferences.LoginTokenManager
import com.example.listerpros.utils.NetworkCheck
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.viewmodel.LoginLogoutViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var networkCheck: NetworkCheck
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var loginKey: LoginTokenManager
    private lateinit var loginLogoutViewModel: LoginLogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        networkCheck = NetworkCheck(requireContext())
        loginLogoutViewModel = ViewModelProvider(this)[LoginLogoutViewModel::class.java]
        loginKey = LoginTokenManager(requireContext())
        progressBarDialog = ProgressBarDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showKeyboard =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        showKeyboard.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

        // onclick of login button validation set
        binding.loginButton.setOnClickListener()
        {
            Helper.hideKeyBoard(it)
            email = binding.emailAddressField.text.toString()
            password = binding.passwordField.text.toString()
            validateCredentials(email, password)             //validate credentials
        }

        // on click of forget password
        binding.forgotPassword.setOnClickListener()
        {
            val networkCheck = networkCheck.checkNetwork()
            if (networkCheck) {
                view.let {
                    findNavController().navigate(R.id.action_loginScreenFragment_to_forgetPasswordFragment)
                }
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.noInternet),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        progressBarDialog.hideProgressBar()
        loginObserver()                   // Observer for Login

    }


    // to validate login and password
    private fun validateCredentials(emailAddress: String, password: String) {
        binding.emailAddress.error = null
        binding.password.error = null

        if (TextUtils.isEmpty(emailAddress) && TextUtils.isEmpty(password)) {
            Toast.makeText(
                context,
                resources.getString(R.string.provideEmailPassword),
                Toast.LENGTH_SHORT
            ).show()
            binding.emailAddress.error = resources.getString(R.string.enterEmailAddress)
            binding.password.error = resources.getString(R.string.enterPassword)
        } else if (TextUtils.isEmpty(emailAddress)) {
            binding.emailAddress.error = resources.getString(R.string.enterEmailAddress)
        } else if (TextUtils.isEmpty(password)) {
            binding.password.error = resources.getString(R.string.enterPassword)
        } else if (!Helper.isValidEmail(emailAddress) || !Helper.isValidEmailFormat(emailAddress)) {
            binding.emailAddress.error = resources.getString(R.string.invalidEmail)
        } else if (!TextUtils.isEmpty(password) && password.length <= 5) {
            binding.password.error = resources.getString(R.string.shortPassword)
        } else {
            binding.emailAddress.error = null
            binding.password.error = null
            val networkCheck = networkCheck.checkNetwork()
            if (networkCheck) {
                userLogin()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.noInternet),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    // Login Response set to viewModel
    private fun userLogin() {
        progressBarDialog.showProgressBar()
        loginLogoutViewModel.loginResponse(email, password)

    }

    // Observer for Login
    private fun loginObserver() {
        loginLogoutViewModel.responseLogin.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>,
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()?.data?.token.toString()
                        BEARER_TOKEN = token
                        loginKey.saveToken(token)
                        progressBarDialog.hideProgressBar()
                        view?.let {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                            Toast.makeText(
                                context,
                                resources.getString(R.string.loginSuccessful),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        progressBarDialog.hideProgressBar()
                        Toast.makeText(
                            context,
                            resources.getString(R.string.invalidEmailPassword),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    progressBarDialog.hideProgressBar()
                    Toast.makeText(
                        context,
                        resources.getString(R.string.networkFailed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        })

    }
}