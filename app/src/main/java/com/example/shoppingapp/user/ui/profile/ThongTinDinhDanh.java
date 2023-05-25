package com.example.shoppingapp.user.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.shoppingapp.R;
import com.example.shoppingapp.user.activities.HomeActivity;
import com.example.shoppingapp.user.models.UserModel;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThongTinDinhDanh extends AppCompatActivity {

    EditText name, gender, birth, CCCD, ngayCap, noiCap, ngayHetHan, phone;

    ImageView back;
    FirebaseDatabase database;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database.getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (!userModel.getName().equals("Chưa cập nhật"))
                    name.setText(userModel.getName());
                if (!userModel.getPhone().equals("Chưa cập nhật"))
                phone.setText(userModel.getPhone());
                if (!userModel.getGender().equals("Chưa cập nhật"))
                gender.setText(userModel.getGender());
                if (!userModel.getBirth().equals("Chưa cập nhật"))
                birth.setText(userModel.getBirth());
                if (!userModel.getCCCD().equals("Chưa cập nhật"))
                CCCD.setText(userModel.getCCCD());
                if (!userModel.getNgayCap().equals("Chưa cập nhật"))
                ngayCap.setText(userModel.getNgayCap());
                if (!userModel.getNoiCap().equals("Chưa cập nhật"))
                noiCap.setText(userModel.getNoiCap());
                if (!userModel.getNgayHetHan().equals("Chưa cập nhật"))
                ngayHetHan.setText(userModel.getNgayHetHan());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(gender.getText().toString()!=null)
                    if(!gender.getText().toString().toLowerCase().equals("nam")&&!gender.getText().toString().toLowerCase().equals("nữ")){
                        Toast.makeText(ThongTinDinhDanh.this,"Giới tính phải là nam hoặc nữ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                if(TextUtils.isEmpty(birth.getText().toString()))
                    try{
                        Calendar calendar = Calendar.getInstance();
                        Date currentDate = calendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date date = format.parse(birth.getText().toString());
                        if(date.after(currentDate)){
                            Toast.makeText(ThongTinDinhDanh.this,"Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        Toast.makeText(ThongTinDinhDanh.this,"Ngày sinh không hợp lệ, định dạng ngày/tháng/năm", Toast.LENGTH_SHORT).show();
                        return;
                    }
                if(!TextUtils.isEmpty(ngayCap.getText().toString()))
                    try{
                        Calendar calendar = Calendar.getInstance();
                        Date currentDate = calendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date date = format.parse(ngayCap.getText().toString());
                        Date date_birth = format.parse(birth.getText().toString());
                        if(date.after(currentDate)||date.before(date_birth)){
                            Toast.makeText(ThongTinDinhDanh.this,"Ngày cấp giấy tờ không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        Toast.makeText(ThongTinDinhDanh.this,"Ngày cấp giấy tờ không hợp lệ, định dạng ngày/tháng/năm", Toast.LENGTH_SHORT).show();
                        return;
                    }
                if(!TextUtils.isEmpty(ngayHetHan.getText().toString()))
                    try{
                        Calendar calendar = Calendar.getInstance();
                        Date currentDate = calendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date date = format.parse(ngayCap.getText().toString());
                        Date date_hh = format.parse(ngayHetHan.getText().toString());
                        if(date.after(date_hh)){
                            Toast.makeText(ThongTinDinhDanh.this,"Ngày hết hạn giấy tờ không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        Toast.makeText(ThongTinDinhDanh.this,"Ngày hết hạn giấy tờ không hợp lệ, định dạng ngày/tháng/năm", Toast.LENGTH_SHORT).show();
                        return;
                    }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(save.getContext());
                builder1.setMessage("Bạn có chắc chắn muốn cập nhật thông tin.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Đồng ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateInformation();
                                Toast.makeText(ThongTinDinhDanh.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                                finish();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "Hủy bỏ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
    private void updateInformation()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String Name = name.getText().toString();
        if (TextUtils.isEmpty(name.getText()))
            Name = "Chưa cập nhật";
        String Gender = gender.getText().toString();
        if (TextUtils.isEmpty(gender.getText()))
            Gender = "Chưa cập nhật";
        String Birth = birth.getText().toString();
        if (TextUtils.isEmpty(birth.getText()))
            Birth = "Chưa cập nhật";
        String CMND = CCCD.getText().toString();
        if (TextUtils.isEmpty(CCCD.getText()))
            CMND = "Chưa cập nhật";
        String NgayCap = ngayCap.getText().toString();
        if (TextUtils.isEmpty(ngayCap.getText()))
            NgayCap = "Chưa cập nhật";
        String NoiCap = noiCap.getText().toString();
        if (TextUtils.isEmpty(noiCap.getText()))
            NoiCap = "Chưa cập nhật";
        String NgayHetHan = ngayHetHan.getText().toString();
        if (TextUtils.isEmpty(ngayHetHan.getText()))
            NgayHetHan = "Chưa cập nhật";
        String Phone = phone.getText().toString();
        if (TextUtils.isEmpty(phone.getText()))
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