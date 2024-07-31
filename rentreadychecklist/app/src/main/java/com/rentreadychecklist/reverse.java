package com.rentreadychecklist;

public class reverse {

    public static void main(String args[])
    {
        int [] a = {30,40,30,50,40,60};
        int lengthA = a.length;
        int [] b = new int[lengthA];
        int lengthB = lengthA;
        for (int i=0;i<lengthA;i++)
        {
            b[lengthB-1]= a[i];
            lengthB= lengthB-1;

        }
        System.out.println(b);

    }

}
