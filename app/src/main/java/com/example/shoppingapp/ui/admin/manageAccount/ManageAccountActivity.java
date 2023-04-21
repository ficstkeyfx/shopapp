package com.example.shoppingapp.ui.admin.manageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.models.ManagerAccountModel;
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
        lstView = findViewById(R.id.view_account);
        auth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();


        DatabaseReference emailRef = database.getReference().child("Users");

        emailRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ManagerAccountModel> lst = new ArrayList<>();
                AccountListViewAdapter accountListViewAdapter;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot.getKey());
                    System.out.println(snapshot.child("email").getValue());
                    String email = snapshot.child("email").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    if(snapshot.child("avatar").getValue()!=null) {
                        String url = snapshot.child("avatar").getValue().toString();
                        lst.add(new ManagerAccountModel(email, name, url));
                    }
                    else lst.add(new ManagerAccountModel(email,name));
                }

                accountListViewAdapter = new AccountListViewAdapter(lst);
                lstView.setAdapter(accountListViewAdapter);

//                for (int i = 0; i < lst.size(); i++) {
//                    System.out.println(lst.get(i).getEmail());
//                }

                lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ManagerAccountModel acc = (ManagerAccountModel) accountListViewAdapter.getItem(i);
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

