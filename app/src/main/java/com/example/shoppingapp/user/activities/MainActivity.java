package com.example.shoppingapp.user.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        if(auth.getCurrentUser() != null){
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            if (auth.getCurrentUser().getEmail().equals("shopapp216@gmail.com"))
            {
                startActivity(new Intent(MainActivity.this, MenuAdminActivity.class));
            }
//
            else {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                String saveCurrentDate, saveCurrentTime;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy" , Locale.ENGLISH);
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                saveCurrentTime = currentTime.format(calendar.getTime());
                database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("lastOnline").setValue(saveCurrentTime + " " + saveCurrentDate);
            }

        }else startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }
}