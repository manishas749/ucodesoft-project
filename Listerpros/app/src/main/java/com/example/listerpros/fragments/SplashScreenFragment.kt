package com.example.listerpros.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.listerpros.R
import com.example.listerpros.activities.LoginActivity
import com.example.listerpros.activities.MainActivity
import com.example.listerpros.constants.Constants.Companion.BEARER_TOKEN
import com.example.listerpros.preferences.LoginTokenManager



class SplashScreenFragment : Fragment() {

    private lateinit var loginKey: LoginTokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view= inflater.inflate(R.layout.fragment_splash_screen, container, false)
        loginKey = LoginTokenManager(requireContext())
        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        splashHandler()
    }



    private fun openMainActivity(){
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }

    private fun openLoginActivity(){
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun splashHandler(){
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (loginKey.getToken()?.isNotEmpty()==true){
                    BEARER_TOKEN = loginKey.getToken().toString()
                    openMainActivity()
                }else{
                    openLoginActivity()
                }

            },3000
        )
    }

}
