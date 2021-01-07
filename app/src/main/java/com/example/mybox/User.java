package com.example.mybox;

import androidx.annotation.Keep;

@Keep
public class User {
    String name;
    String pass;
    String login;
   int r;
   int g;
   int b;

    public User() {
    }

    public User(String name, String pass, String login, int r, int g, int b) {
        this.name = name;
        this.pass = pass;
        this.login = login;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
