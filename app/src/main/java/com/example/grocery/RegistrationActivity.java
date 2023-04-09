package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity
{

    Button signUp;
    EditText name, email, password, re_password;
    TextView signIn;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        signUp = findViewById(R.id.signUp_btn);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        re_password = findViewById(R.id.re_password);
        signIn = findViewById(R.id.signIn);

        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }

        });

        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createUser();
            }

            private void createUser()
            {
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userRe_Password = password.getText().toString();

                if (TextUtils.isEmpty(userName))
                {
                    Toast.makeText(RegistrationActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(RegistrationActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPassword))
                {
                    Toast.makeText(RegistrationActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPassword.length() < 6)
                {
                    Toast.makeText(RegistrationActivity.this, "Password length must be greater then 6 letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userRe_Password))
                {
                    Toast.makeText(RegistrationActivity.this, "Password again is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ((TextUtils.equals(userPassword, userRe_Password)) == false && userPassword != "" && userRe_Password != "" )
                {
                    Toast.makeText(RegistrationActivity.this, "Wrong password. Input password again", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Create User
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    UserModel userModel = new UserModel(userName, userEmail, userPassword);
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(userModel);
                                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(RegistrationActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }
}