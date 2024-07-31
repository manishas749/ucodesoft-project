package com.example.datastoragesql;

import java.util.ArrayList;
import java.util.Arrays;

public class jsjdhjhd {

    public static void main(String args[]) {
        int cnt0 = 0, cnt1 = 0, cnt2 = 0;
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(0);
        arr.add(2);
        arr.add(1);
        arr.add(2);
        arr.add(0);
        arr.add(1);


        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == 0) cnt0++;
            else if (arr.get(i) == 1) cnt1++;
            else cnt2++;

        }
        for (int i = 0; i < cnt0; i++) {
            arr.set(i, 0);
        }

        for (int i = cnt0; i < cnt0 + cnt1; i++) arr.set(i, 1); // replacing 1's
        for (int i = cnt0 + cnt1; i < arr.size(); i++) arr.set(i, 2); // replacing 2's


        for (int i = 0; i < arr.size(); i++) {
            System.out.print(arr.get(i) + " ");
        }


    }
}
