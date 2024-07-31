package com.example.triviaquiz;

public class StudentData {


    static int count=0;//will get memory when instance is created
    StudentData()
    {
        count++;
        System.out.println(count);
    }
    public static void main(String args[])
    {
        StudentData c1=new StudentData();
        StudentData c2=new StudentData();
        StudentData c3=new StudentData();
    }
}


