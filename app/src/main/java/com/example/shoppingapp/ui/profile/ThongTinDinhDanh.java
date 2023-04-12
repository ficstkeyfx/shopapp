package com.example.shoppingapp.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.shoppingapp.activities.HomeActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.UserModel;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.ktx.Firebase;

public class ThongTinDinhDanh extends AppCompatActivity {

    EditText name, gender, birth, CCCD, ngayCap, noiCap, ngayHetHan, phone;

    ImageView back;
    FirebaseDatabase database;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_dinh_danh);

        database = FirebaseDatabase.getInstance();
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        gender = findViewById(R.id.gender);
        birth = findViewById(R.id.birth);
        CCCD = findViewById(R.id.CCCD);
        ngayCap = findViewById(R.id.ngayCap);
        noiCap = findViewById(R.id.noiCap);
        ngayHetHan = findViewById(R.id.ngayHetHan);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save_btn);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                updateInformation();
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ThongTinDinhDanh.this, HomeActivity.class));
            }
        });
    }
    private void updateInformation()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String Name = name.getText().toString();
        if (Name == null)
            Name = "Chưa cập nhật";
        String Gender = gender.getText().toString();
        if (Gender == null)
            Gender = "Chưa cập nhật";
        String Birth = birth.getText().toString();
        if (Birth == null)
            Birth = "Chưa cập nhật";
        String CMND = CCCD.getText().toString();
        if (CMND == null)
            CMND = "Chưa cập nhật";
        String NgayCap = ngayCap.getText().toString();
        if (NgayCap == null)
            NgayCap = "Chưa cập nhật";
        String NoiCap = noiCap.getText().toString();
        if (NoiCap == null)
            NoiCap = "Chưa cập nhật";
        String NgayHetHan = ngayHetHan.getText().toString();
        if (NgayHetHan == null)
            NgayHetHan = "Chưa cập nhật";
        String Phone = phone.getText().toString();
        if (Phone == null)
            Phone = "Chưa cập nhật";

        database.getReference().child("Users").child(id).child("name").setValue(Name);
        database.getReference().child("Users").child(id).child("phone").setValue(Phone);
        database.getReference().child("Users").child(id).child("gender").setValue(Gender);
        database.getReference().child("Users").child(id).child("birth").setValue(Birth);
        database.getReference().child("Users").child(id).child("cccd").setValue(CMND);
        database.getReference().child("Users").child(id).child("ngayCap").setValue(NgayCap);
        database.getReference().child("Users").child(id).child("noiCap").setValue(NoiCap);
        database.getReference().child("Users").child(id).child("ngayHetHan").setValue(NgayHetHan);

    }
}