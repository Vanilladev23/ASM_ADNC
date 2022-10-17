package com.example.asm_adnc.models;

import java.util.ArrayList;

public class ListUser {
    private String title;
    private ArrayList<User> data;

    public ListUser(String title, ArrayList<User> data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }
}
