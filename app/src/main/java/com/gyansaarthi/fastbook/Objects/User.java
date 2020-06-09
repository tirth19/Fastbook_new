package com.gyansaarthi.fastbook.Objects;

import java.util.List;

public class User {

    public String username;
    public String email;
    //List<Achievement> achievementList;
    //TODO: Add book object and add read booklist to the user

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}