package com.example.listerpros.fragments


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listerpros.R
import com.example.listerpros.utils.Helper
import com.example.listerpros.databinding.FragmentProfileSettingsBinding
import com.example.listerpros.model.editprofile.ResponseEditProfile
import com.example.listerpros.model.getmyprofile.ResponseGetProfile
import com.example.listerpros.utils.NetworkCheck
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.viewmodel.ProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var networkCheck: NetworkCheck
    private lateinit var progressDialog: ProgressBarDialog
    private var firstName = ""
    private var lastName = ""
    private var emailAddress = ""
    private var cellPhone = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)

        progressDialog = context?.let { ProgressBarDialog(it) }!!
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.getProfile()
        networkCheck = NetworkCheck(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog.showProgressBar()

        binding.updateProfile.setOnClickListener {
            Helper.hideKeyBoard(it)
            profileSettingsValidation(binding.firstNameEdit.text?.trim().toString(),
            binding.lastNameEdit.text?.trim().toString(),
            binding.emailAddressEdit.text?.trim().toString(),
            binding.cellPhoneEdit.text?.trim().toString())          // validation for profile settings
        }

        binding.backPress.setOnClickListener {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        getProfileObserver()
        editProfileObserver()
    }

    // Validation for Profile Settings
    fun profileSettingsValidation(firstName: String, lastName: String, emailAddress: String, cellPhone: String): Boolean {
       /* val firstName = binding.firstNameEdit.text?.trim()
        val lastName = binding.lastNameEdit.text?.trim()
        val emailAddress = binding.emailAddressEdit.text?.trim()
        val cellPhone = binding.cellPhoneEdit.text?.trim()  */

    //    binding.firstName.error = null
    //    binding.lastName.error = null
    //    binding.emailAddress.error = null
    //    binding.cellPhone.error = null

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && emailAddress.isNotEmpty() && cellPhone.isNotEmpty()
        ) {
            if (!isValidFirstName(firstName)) {
                binding.firstName.error = resources.getString(R.string.capitalFirstName)
                return false
            } else if (!isValidLastName(lastName)) {
                binding.lastName.error = resources.getString(R.string.capitalLastName)
                return false
            } else if (!Helper.isValidEmail(emailAddress) || !Helper.isValidEmailFormat(
                    emailAddress
                ) || emailAddress.contains(" ")
            ) {
                binding.emailAddress.error = resources.getString(R.string.invalidEmail)
                return false
            } else if (!Patterns.PHONE.matcher(cellPhone)
                    .matches() || cellPhone.contains(" ") ||
                !isValidUSPhoneNumberFormat(cellPhone)
            ) {
                binding.cellPhone.error = resources.getString(R.string.invalidPhone)
                return false
            } else {
                val networkCheck = networkCheck.checkNetwork()
                if (networkCheck) {
                    updateProfile()
                    progressDialog.showProgressBar()
                    return true;

                } else {
                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
                    progressDialog.hideProgressBar()

                }
                Toast.makeText(
                    context,
                    resources.getString(R.string.settingsValid),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } else {
            if (firstName.isEmpty()) {
                binding.firstName.error = resources.getString(R.string.enterFirstName)
                return false
            }
            if (lastName.isEmpty()) {
                binding.lastName.error = resources.getString(R.string.enterLastName)
                return false
            }
            if (emailAddress.isEmpty()) {
                binding.emailAddress.error = resources.getString(R.string.enterEmailAddress)
                return false
            }
            if (cellPhone.isEmpty()) {
                binding.cellPhone.error = resources.getString(R.string.enterCellPhoneNumber)
                return false
            }
        }

        return false;
    }

    private fun updateProfile() {
        val firstName = binding.firstNameEdit.text
        val lastName = binding.lastNameEdit.text
        val emailAddress = binding.emailAddressEdit.text
        val cellPhone = binding.cellPhoneEdit.text

        viewModel.editProfile(
            firstName.toString(), lastName.toString(),
            emailAddress.toString(), cellPhone.toString()
        )
    }

    // Observer for get Profile Settings
    private fun getProfileObserver() {
        viewModel.responseGetProfile.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<ResponseGetProfile> {
                override fun onResponse(
                    call: Call<ResponseGetProfile>,
                    response: Response<ResponseGetProfile>
                ) {
                    if (response.isSuccessful) {
                        firstName = response.body()?.data?.myProfile?.first_name.toString()
                        lastName = response.body()?.data?.myProfile?.last_name.toString()
                        emailAddress = response.body()?.data?.myProfile?.email.toString()
                        cellPhone = response.body()?.data?.myProfile?.cell_phone.toString()
                        setProfileSettings()
                        progressDialog.hideProgressBar()

                    } else {
                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.hideProgressBar()
                    }
                }

                override fun onFailure(call: Call<ResponseGetProfile>, t: Throwable) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.noInternet),
                        Toast.LENGTH_SHORT
                    ).show()

                    progressDialog.hideProgressBar()
                }

            })
        })
    }

    // Observer for update the profile settings
    private fun editProfileObserver() {
        val call = viewModel.responseEditProfile.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<ResponseEditProfile> {
                override fun onResponse(
                    call: Call<ResponseEditProfile>,
                    response: Response<ResponseEditProfile>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.hideProgressBar()
                    } else {
                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.hideProgressBar()
                    }
                }

                override fun onFailure(call: Call<ResponseEditProfile>, t: Throwable) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.networkFailed),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.hideProgressBar()
                }

            })
        })
    }


    fun setProfileSettings() {
        binding.firstNameEdit.setText(firstName)
        binding.lastNameEdit.setText(lastName)
        binding.emailAddressEdit.setText(emailAddress)
        binding.cellPhoneEdit.setText(cellPhone)

    }

    // For check US Phone Number is valid or not
    fun isValidUSPhoneNumberFormat(cellPhone: String): Boolean {
        return Pattern.compile(
            "^[0-9]{3}[0-9]{3}[0-9]{4}\$"
        ).matcher(cellPhone).matches()
    }

    fun isValidFirstName(firstName : String) : Boolean{
        return Pattern.compile("[A-Z][a-z]*").matcher(firstName).matches()
    }

    fun isValidLastName(lastName: String) : Boolean{
        return Pattern.compile("[A-Z][a-z]*").matcher(lastName).matches()
    }

}