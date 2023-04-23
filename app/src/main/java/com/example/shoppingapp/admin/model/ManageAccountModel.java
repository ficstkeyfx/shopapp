package com.example.shoppingapp.admin.model;

public class ManageAccountModel
{
    String email;
    String name;
    String url_img;

    public ManageAccountModel(String email, String name, String url_img) {
        this.email = email;
        this.name = name;
        this.url_img = url_img;

    }

    public ManageAccountModel(String email, String name) {
        this.email = email;
        this.name = name;
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
