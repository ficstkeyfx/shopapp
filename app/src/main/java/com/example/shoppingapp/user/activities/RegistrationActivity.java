package com.example.shoppingapp.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        if(!rePassword.equals(userPassword)){
            Toast.makeText(this,"Xác nhận mật khẩu không giống nhau",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(userEmail,userPassword).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserModel userModel = new UserModel(userName,userEmail,userPassword);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);
                            database.getReference().child("Users").child(id).child("gender").setValue(userModel.getGender());
                            database.getReference().child("Users").child(id).child("phone").setValue(userModel.getPhone());
                            database.getReference().child("Users").child(id).child("birth").setValue(userModel.getBirth());
                            database.getReference().child("Users").child(id).child("cccd").setValue(userModel.getCCCD());
                            database.getReference().child("Users").child(id).child("ngayCap").setValue(userModel.getNgayCap());
                            database.getReference().child("Users").child(id).child("noiCap").setValue(userModel.getNoiCap());
                            database.getReference().child("Users").child(id).child("ngayHetHan").setValue(userModel.getNgayHetHan());
                            database.getReference().child("Users").child(id).child("address").setValue(userModel.getAddress());
                            database.getReference().child("Users").child(id).child("job").setValue(userModel.getJob());
                            database.getReference().child("Users").child(id).child("position").setValue(userModel.getPosition());
                            database.getReference().child("Users").child(id).child("avatar").setValue("https://firebasestorage.googleapis.com/v0/b/shoppingapp-3f5b0.appspot.com/o/avatar.png?alt=media&token=406846d4-d88a-423a-b54d-068ae1100134");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistrationActivity.this, "Đăng ký tài khoản thành công. Hãy xác thực tài khoản!!!", Toast.LENGTH_SHORT).show();
                                        email.setText("");
                                        password.setText("");
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                        }else {
                            Toast.makeText(RegistrationActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}