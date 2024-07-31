package com.ucopdesoft.issuelogger;


import java.util.Arrays;

public class StringReverse {

    public static void main(String[] args) {
        String str = "Great responsibility";
        char a[] = str.toCharArray();
        int count =0;

        for (int i = 0;i<a.length;i++)
        {
            count = 1;
            for (int j=i+1;j<a.length;j++)
            {
                if (a[i]==a[j] &&  a[j]!=' ')
                {
                      count++;
                      a[j] ='0';

                }
            }
            if (count>1 && a[i]!='0')
            {
                System.out.println(a[i]);
            }
        }





    }
}
