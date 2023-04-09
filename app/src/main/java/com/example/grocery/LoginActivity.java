package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    EditText name, password;
    TextView signUp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.signIn_btn);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }

        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginUser();
            }


        });

    }
    private void loginUser()
    {
        String userName = name.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userName))
        {
            Toast.makeText(LoginActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword))
        {
            Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //login user
        auth.createUserWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {

                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}