package com.example.shoppingapp.admin.model;

public class ManageAccountModel
{
    String email;
    String name;
    String url_img;
    String lastOnline;

    public ManageAccountModel(String email, String name, String url_img, String lastOnline) {
        this.email = email;
        this.name = name;
        this.url_img = url_img;
        this.lastOnline = lastOnline;
    }

    public ManageAccountModel(String email, String name, String lastOnline) {
        this.email = email;

        this.lastOnline = lastOnline;

    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

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

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }
}
