package com.example.triviaquiz;

import android.icu.text.UFormat;

import java.util.ArrayList;

public class test {

    
    
    public static void main(String args[])
    {
        int[] a = {10,50,20,10,60,24};
        ArrayList<Integer> ab = new ArrayList<>();

        for (int i=1;i<a.length-1;i++)
        {
            if (a[i]>a[i-1] && a[i]>a[i+1])
            {
                ab.add(a[i]);


            }
            
        }

        System.out.println(ab);
    }


}
