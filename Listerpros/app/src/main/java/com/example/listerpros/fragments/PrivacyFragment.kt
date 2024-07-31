package com.example.listerpros.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.listerpros.R
import com.example.listerpros.databinding.FragmentPrivacyBinding

class PrivacyFragment : Fragment() {

    private lateinit var binding: FragmentPrivacyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPrivacyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updatePassword.setOnClickListener {
            updatePassword()             // to change password after matching the old password
        }

        //on back press
        binding.backPress.setOnClickListener {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    // to change password after matching the old password
    private fun updatePassword() {
        val oldPassword = binding.currentPasswordET.text
        val newPassword = binding.newPasswordET.text
        val confirmPassword = binding.confirmPasswordEt.text

        binding.currentPassword.error = null
        binding.newPassword.error = null
        binding.confirmPassword.error = null

        if (oldPassword.toString() == resources.getString(R.string.oldPassword)) {
            if (isValidPassword(newPassword.toString())) {
                if (newPassword.toString() == confirmPassword.toString()) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.passwordMatched),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.newPassword.error = resources.getString(R.string.notMatched)
                    binding.confirmPassword.error = resources.getString(R.string.notMatched)
                }
            } else {
                binding.newPassword.error = resources.getString(R.string.passwordLength)
            }
        } else {
            binding.currentPassword.error = resources.getString(R.string.currentPasswordIncorrect)
        }
    }

    // validation for password
    fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.firstOrNull { it.isDigit() } == null) return false
        if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return false
        if (password.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) return false

        return true
    }

}
