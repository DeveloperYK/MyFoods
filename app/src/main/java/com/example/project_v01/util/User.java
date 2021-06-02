package com.example.project_v01.util;

import android.app.Application;

public class User extends Application { // singleton class that can be used anywhere in our application

    private String firstName;
    private String lastName;
    private String userId;
    private static User instance;

    public static User getInstance() {
        if(instance == null) {
              instance = new User();

        }
        return instance;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
