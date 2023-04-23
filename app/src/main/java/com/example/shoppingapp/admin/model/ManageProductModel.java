package com.example.shoppingapp.admin.model;

import android.net.Uri;

public class ManageProductModel
{
    String name, type;
    long ID;
    Uri img;

    public ManageProductModel(String name, String type, Uri img)
    {
        this.name = name;
        this.type = type;
        this.img = img;
        ID = 0;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    public void setType(String type) {
        this.type = type;
    }

}