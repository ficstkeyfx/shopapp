package com.example.shoppingapp.user.models;

public class CommentModel
{
    String avatar, name, date, comment;
    float rating;
    public CommentModel()
    {

    }

    public CommentModel(String avatar, String name, String date, String comment, float rating)
    {
        this.avatar = avatar;
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDate() {
        return date;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
