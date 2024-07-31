package com.example.listerpros.fragments

import org.junit.Assert.*
import org.junit.Test

class PrivacyFragmentTest{

    @Test
    fun checkValidPassword(){
        val result  = PrivacyFragment().isValidPassword("Mukulsidhu123")
        assertEquals(true, result)
    }
}