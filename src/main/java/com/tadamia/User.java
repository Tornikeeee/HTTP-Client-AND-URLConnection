package com.tadamia;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        User u = (User) obj;
        return this.username.equals(u.getUsername()) && this.password.equals(u.getPassword());
    }
}
