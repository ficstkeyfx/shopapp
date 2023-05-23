package com.example.shoppingapp.admin.model;

import java.io.Serializable;

public class CSKH_Model implements Serializable {
    String ava, name, msg, time, ID;
    int type;
    public CSKH_Model()
    {
        this.type = 0;
    }

    public CSKH_Model(String ava, String name, String msg, String time)
    {
        this.ava = ava;
        this.name = name;
        this.msg = msg;
        this.time = time;
        this.type = 0;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }
}
