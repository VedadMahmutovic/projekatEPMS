package com.vedadmahmutovic.model;

public class User {
    private int id;
    private String ime;
    private String role;
    private String password;

    public User(int id, String ime, String role, String password) {
        this.id = id;
        this.ime = ime;
        this.role = role;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, ime='%s', role='%s', password='%s'}", id, ime, role, password);
    }
}

