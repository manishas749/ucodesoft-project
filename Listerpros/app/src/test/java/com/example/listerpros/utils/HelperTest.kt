package com.example.listerpros.utils

import org.junit.Assert.*
import org.junit.Test

class HelperTest
{
    @Test
    fun emailCheck()
    {
        val result=Helper.isValidEmailFormat("manis")
        assertEquals(false,result)
    }

    @Test
    fun emailCheck1()
    {
        val result=Helper.isValidEmailFormat("")
        assertEquals(false,result)
    }

    @Test
    fun emailCheck2()
    {
        val result=Helper.isValidEmailFormat("manishas749@gmail.com")
        assertEquals(true,result)
    }


    @Test
    fun emailCheck3()
    {
        val result=Helper.isValidEmailFormat("manishas749@gmail.c")
        assertEquals(false,result)
    }

}