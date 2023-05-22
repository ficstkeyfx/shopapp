package com.example.shoppingapp.user.models;

public class ChatModel
{
    String time, avatar, name, msg;
    int type;

    public ChatModel()
    {
        this.type = 0;
    }
    public ChatModel(String time, String avatar, String name, String msg)
    {
        this.time = time;
        this.avatar = avatar;
        this.name = name;
        this.msg = msg;
        this.type  = 0;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
