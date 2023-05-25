package com.example.shoppingapp.admin.activities.manageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.adapter.AccountListViewAdapter;
import com.example.shoppingapp.admin.model.ManageAccountModel;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageAccountActivity extends AppCompatActivity {

    ListView lstView;
    FirebaseDatabase database;
    ProgressBar progressBar;
    FirebaseAuth auth;
    ImageView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_manage_account);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        goBack = findViewById(R.id.goBack);
        lstView = findViewById(R.id.view_account);
        auth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();


        DatabaseReference emailRef = database.getReference().child("Users");

        emailRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ManageAccountModel> lst = new ArrayList<>();
                AccountListViewAdapter accountListViewAdapter;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot.getKey());
                    System.out.println(snapshot.child("email").getValue());
                    String email = snapshot.child("email").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String lastOnline = "";
                    if (snapshot.child("lastOnline").exists() && snapshot.child("lastOnline").getValue() != null )
                    {
                         lastOnline = snapshot.child("lastOnline").getValue().toString();
                    }
                    else
                        lastOnline = "";
                    System.out.println(lastOnline);
                    if(snapshot.child("avatar").getValue()!=null) {
                        String url = snapshot.child("avatar").getValue().toString();
                        lst.add(new ManageAccountModel(email, name, url, lastOnline));
                    }
                    else lst.add(new ManageAccountModel(email,name, lastOnline));
                }

                accountListViewAdapter = new AccountListViewAdapter(lst);
                lstView.setAdapter(accountListViewAdapter);

//                for (int i = 0; i < lst.size(); i++) {
//                    System.out.println(lst.get(i).getEmail());
//                }
                progressBar.setVisibility(View.GONE);
                lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ManageAccountModel acc = (ManageAccountModel) accountListViewAdapter.getItem(i);
                        Toast.makeText(ManageAccountActivity.this, acc.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ManageAccountActivity.this, MenuAdminActivity.class));
            }
        });
    }
}

