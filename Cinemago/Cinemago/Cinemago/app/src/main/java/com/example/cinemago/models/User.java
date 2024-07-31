package com.example.cinemago.models;

public class User {
    public String uid, name, address, email;

    public User() {
    }

    public User(String uid, String name, String address, String email) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
