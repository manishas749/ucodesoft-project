package com.example.triviaquiz;

/**
 * class to save registered userdata
 */
public  class SaveUserData {

    String firstName;
    String lastName;
    String userName;

    String dateOfBirth;
    String emailId;
    String password;


    public SaveUserData(String firstName,String lastName,String userName,String dateOfBirth,String emailId,
                        String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dateOfBirth = dateOfBirth;
        this.emailId = emailId;
        this.password = password;

    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public  String getUserName()
    {
        return userName;

    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public String getEmailId()
    {
        return emailId;
    }

    public String getPassword()
    {
        return password;
    }
}
