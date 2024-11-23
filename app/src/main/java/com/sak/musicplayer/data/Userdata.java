package com.sak.musicplayer.data;

public class Userdata {
    String email;
    String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Userdata(String email, String name, String username) {
        this.email = email;
        this.name = name;
        this.username = username;
    }

    String username;

    public Userdata() {
    }
}
