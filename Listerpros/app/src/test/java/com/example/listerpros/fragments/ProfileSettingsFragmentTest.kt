package com.example.listerpros.fragments

import androidx.test.core.app.ActivityScenario
import org.junit.Assert.*
import org.junit.Test

class ProfileSettingsFragmentTest{



    @Test
    fun checkValidUSCellPhone(){
        val result = ProfileSettingsFragment().isValidUSPhoneNumberFormat("9999999999")
        assertEquals(true,result)
    }

    @Test
    fun checkValidFirstName(){
     val result = ProfileSettingsFragment().isValidFirstName("Mukul")
     assertEquals(true, result)
    }

    @Test
    fun checkValidLastName(){
        val result = ProfileSettingsFragment().isValidLastName("Sidhu")
        assertEquals(true,result)
    }


}