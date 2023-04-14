package com.example.shoppingapp.ui.admin.manageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.ui.admin.MenuAdminActivity;
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
    FirebaseAuth auth;
    ImageView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        goBack = findViewById(R.id.goBack);
        lstView = findViewById(R.id.lstView);
        auth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();


        DatabaseReference emailRef = database.getReference().child("Users");

        emailRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ArrayList<account> lst = new ArrayList<>();
                AccountListViewAdapter accountListViewAdapter;
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {

                    String email = snapshot.child("email").getValue().toString();
                    lst.add(new account(email));
                }

                accountListViewAdapter = new AccountListViewAdapter(lst);
                lstView.setAdapter(accountListViewAdapter);

                for (int i =0 ; i < lst.size(); i++)
                {
                    System.out.println(lst.get(i).email);
                }

                lstView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        account acc = (account) accountListViewAdapter.getItem(i);
                        acc.ID = 1;
                        Toast.makeText(ManageAccountActivity.this, acc.email, Toast.LENGTH_SHORT).show();
                    }
                });

                findViewById(R.id.delete).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (lst.size() > 0)
                        {
                            for (int i = 0; i < accountListViewAdapter.getCount(); i++)
                            {
                                account acc = (account) accountListViewAdapter.getItem(i);
                                if (acc.ID == 1)
                                {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                                    {
                                        String id = childSnapshot.getKey();
                                        if (childSnapshot.child("email").getValue().toString().equals(acc.email))
                                        {
                                            DatabaseReference ref = database.getReference("Users").child(id);
                                            ref.removeValue();
                                        }
                                    }
                                    lst.remove(i);
                                    accountListViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }
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

