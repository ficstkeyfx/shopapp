package com.example.shoppingapp.user.models;

import java.util.Date;

public class VoucherModel
{
    int discount, minimum, quantity;
    String expiration_date;
    long ID;

    public VoucherModel()
    {

    }

    public VoucherModel(int discount,int minimum, int quantity, String expiration_date)
    {
        this.discount = discount;
        this.minimum = minimum;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        ID = 0;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public int getDiscount() {
        return discount;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}

