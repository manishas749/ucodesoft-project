package com.example.triviaquiz;


/**
 * class to save user name and his score
  */
public class SaveUserGameScore {
    String UserName;
    Integer Score;

    public SaveUserGameScore(String UserName,Integer Score)
    {
        this.UserName = UserName;
        this.Score = Score;
    }

    public String getUserName()
    {
        return UserName;
    }

    public Integer getScore()
    {
        return Score;
    }
}
