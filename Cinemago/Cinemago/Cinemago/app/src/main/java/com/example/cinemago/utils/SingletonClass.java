package com.example.cinemago.utils;

import com.example.cinemago.models.User;

public class SingletonClass {

    public static SingletonClass instance = null;
    User user = null;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    synchronized public static SingletonClass getInstance()
    {
        if (instance == null)
        {
            instance = new SingletonClass();
            return instance;
        }
        else
            return instance;
    }

}
