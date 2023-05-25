package com.example.shoppingapp.user.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.util.PasswordHash;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassActivity extends AppCompatActivity {

    EditText oldPass, newPass, confirmPass;
    Button changePass;
    ImageView goBack, canhBaoOldPass, canhBaoNewPass, canhBaoConfirmPass;
    FirebaseDatabase database;
    String pass ="";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_change_pass);
        canhBaoOldPass = findViewById(R.id.canhBaoOldPass);
        canhBaoNewPass = findViewById(R.id.canhBaoNewPass);
        canhBaoConfirmPass = findViewById(R.id.canhBaoConfirmPass);
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        confirmPass = findViewById(R.id.confirmPass);
        changePass = findViewById(R.id.buttonChangePassword);
        goBack = findViewById(R.id.goBack);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String ID = getIntent().getStringExtra("ID");

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        database.getReference().child("Users").child(ID).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pass = snapshot.getValue().toString();
                System.out.println(pass);
                changePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(oldPass.getText()))
                        {
                            Toast.makeText(ChangePassActivity.this, "Bạn chưa nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                            canhBaoOldPass.setVisibility(View.VISIBLE);
                            return;
                        }
                        else if (TextUtils.isEmpty(newPass.getText()))
                        {
                            Toast.makeText(ChangePassActivity.this, "Bạn chưa nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                            canhBaoNewPass.setVisibility(View.VISIBLE);
                            return;
                        }
                        else if (TextUtils.isEmpty(confirmPass.getText()))
                        {
                            Toast.makeText(ChangePassActivity.this, "Bạn chưa xác nhận mật khẩu mới", Toast.LENGTH_SHORT).show();
                            canhBaoConfirmPass.setVisibility(View.VISIBLE);
                            return;
                        }
                        else if (!pass.equalsIgnoreCase(PasswordHash.encryptThisString(oldPass.getText().toString())))
                        {
                            Toast.makeText(ChangePassActivity.this, "Bạn nhập mật khẩu cũ chưa đúng", Toast.LENGTH_SHORT).show();
                            canhBaoOldPass.setVisibility(View.VISIBLE);
                            return;
                        }
                        else if (!confirmPass.getText().toString().equals(newPass.getText().toString()))
                        {
                            Toast.makeText(ChangePassActivity.this, "Bạn nhập lại mật khẩu mới chưa đúng", Toast.LENGTH_SHORT).show();
                            canhBaoConfirmPass.setVisibility(View.VISIBLE);
                            return;
                        }
                        else
                        {
                            auth.getCurrentUser().updatePassword(newPass.getText().toString());
                            database.getReference().child("Users").child(ID).child("password").setValue(newPass.getText().toString());
                            Toast.makeText(ChangePassActivity.this, "Bạn đã đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        oldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(oldPass.getText()))
                {
                    canhBaoOldPass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(newPass.getText()))
                {
                    canhBaoNewPass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(confirmPass.getText()))
                {
                    canhBaoConfirmPass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        changePass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (TextUtils.isEmpty(oldPass.getText()))
//                {
//                    Toast.makeText(ChangePassActivity.this, "Bạn chưa nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
//                    canhBaoOldPass.setVisibility(View.VISIBLE);
//                    return;
//                }
//                else if (TextUtils.isEmpty(newPass.getText()))
//                {
//                    Toast.makeText(ChangePassActivity.this, "Bạn chưa nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
//                    canhBaoNewPass.setVisibility(View.VISIBLE);
//                    return;
//                }
//                else if (TextUtils.isEmpty(confirmPass.getText()))
//                {
//                    Toast.makeText(ChangePassActivity.this, "Bạn chưa xác nhận mật khẩu mới", Toast.LENGTH_SHORT).show();
//                    canhBaoConfirmPass.setVisibility(View.VISIBLE);
//                    return;
//                }
//                else if (pass.equalsIgnoreCase(oldPass.getText().toString()))
//                {
//                    Toast.makeText(ChangePassActivity.this, "Bạn nhập mật khẩu cũ chưa đúng", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else if (confirmPass.getText().toString().equals(oldPass.getText().toString()))
//                {
//                    Toast.makeText(ChangePassActivity.this, "Bạn nhập lại mật khẩu mới chưa đúng", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else
//                {
//                    auth.getCurrentUser().updatePassword(newPass.getText().toString());
//                    database.getReference().child("Users").child(ID).child("password").setValue(newPass.getText().toString());
//                    Toast.makeText(ChangePassActivity.this, "Bạn đã đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
//                    finish();
//                    return;
//                }
//            }
//        });
    }
}