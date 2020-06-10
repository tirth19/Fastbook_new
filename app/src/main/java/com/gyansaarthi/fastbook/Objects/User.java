package com.gyansaarthi.fastbook.Objects;

import java.util.List;

public class User {

    public String username;
    public String email;
    public List<Achievement> achievementList;
    public int last_login, streak_length;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String username, String email, List<Achievement> achievementList, int lastLogin, int streakLength) {
        this.username = username;
        this.email = email;
        this.achievementList = achievementList;
        this.last_login = lastLogin;
        this.streak_length = streakLength;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Achievement> getAchievementList() {
        return achievementList;
    }

    public void setAchievementList(List<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    public int getLast_login() {
        return last_login;
    }

    public void setLast_login(int last_login) {
        this.last_login = last_login;
    }

    public int getStreak_length() {
        return streak_length;
    }

    public void setStreak_length(int streak_length) {
        this.streak_length = streak_length;
    }
}