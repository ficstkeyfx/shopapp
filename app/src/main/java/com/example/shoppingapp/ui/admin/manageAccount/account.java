package com.example.shoppingapp.ui.admin.manageAccount;

public class account
{
    String email;
    long ID;
    public account(String email, long ID)
    {
        this.email = email;
        this.ID = ID;
    }

    public account(String email)
    {
        this.email = email;
        this.ID = 0;
    }
}
