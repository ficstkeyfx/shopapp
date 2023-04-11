package com.example.shoppingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    Button signUp;
    TextView signIn;
    EditText email,password,name,repassword;
    ProgressBar progressBar;


    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signUp = findViewById(R.id.login_btn);
        signIn = findViewById(R.id.sign_in);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        name = findViewById(R.id.name);
        progressBar = findViewById(R.id.progressbar);
        repassword = findViewById(R.id.repassword_reg);

        progressBar.setVisibility(View.GONE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createUser() {
        String userName = name.getText().toString();
        String userPassword = password.getText().toString();
        String userEmail = email.getText().toString();
        String rePassword = repassword.getText().toString();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Tên trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Mật khẩu trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length()<6){
            Toast.makeText(this, "Mật khẩu ít hơn 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(rePassword!=userPassword){
//            Toast.makeText(this,"Xác nhận mật khẩu không giống nhau",Toast.LENGTH_SHORT).show();
//            return;
//        }
        auth.createUserWithEmailAndPassword(userEmail,userPassword).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserModel userModel = new UserModel(userName,userEmail,userPassword);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegistrationActivity.this, "Lỗi: " + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}