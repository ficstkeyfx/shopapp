package com.example.shoppingapp.user.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.models.UserModel;
import com.example.shoppingapp.user.util.PasswordHash;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    Button signUp;
    TextView signIn;
    EditText email,password,name,repassword;
    ProgressBar progressBar;

    CheckBox checkBox;
    GoogleApiClient googleApiClient;
    boolean captcha = false;
    String SiteKey = "6LehhDgmAAAAAEW6UI7k0w8l9s5tjfgPz8XYfER4";


    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        checkBox = findViewById(R.id.checkBox);

        progressBar.setVisibility(View.GONE);

        // captcha

        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API)
                .addConnectionCallbacks(RegistrationActivity.this).build();
        googleApiClient.connect();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    System.out.println(status.isSuccess());
                                    if(status != null && status.isSuccess()) {
                                        Toast.makeText(RegistrationActivity.this,"Xác nhận thành công",Toast.LENGTH_SHORT).show();
                                        captcha = true;
                                        checkBox.setChecked(true);
                                        checkBox.setTextColor(Color.BLUE);
                                    }else{
                                        Toast.makeText(RegistrationActivity.this,"Xác nhận không thành công",Toast.LENGTH_SHORT).show();
                                        checkBox.setTextColor(Color.RED);
                                    }
                                }
                            });
                }else {
                    captcha = false;
                    checkBox.setTextColor(Color.BLACK);
                }

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!captcha){
                    Toast.makeText(RegistrationActivity.this,"Hãy xác nhận captcha trước",Toast.LENGTH_SHORT).show();
                }else{
                    createUser();
                    progressBar.setVisibility(View.VISIBLE);
                }


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
        String hashPass = PasswordHash.encryptThisString(userPassword);
        auth.createUserWithEmailAndPassword(userEmail,hashPass).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserModel userModel = new UserModel(userName,userEmail,hashPass);
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
                            auth.signOut();
                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                        }else {
                            Toast.makeText(RegistrationActivity.this, "Lỗi hệ thống, đăng ký thất bại ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}