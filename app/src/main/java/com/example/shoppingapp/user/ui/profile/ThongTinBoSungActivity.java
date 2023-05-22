package com.example.shoppingapp.user.ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapp.user.activities.HomeActivity;
import com.example.shoppingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ThongTinBoSungActivity extends AppCompatActivity
{
    EditText email, address, job, position;

    ImageView back;
    FirebaseDatabase database;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_bo_sung);

        database = FirebaseDatabase.getInstance();
        address = findViewById(R.id.address);
        job = findViewById(R.id.job);
        position = findViewById(R.id.position);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save_btn);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(save.getContext());
                builder1.setMessage("Bạn có chắc chắn muốn cập nhật thông tin.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Đồng ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateInformation();
                                Toast.makeText(ThongTinBoSungActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
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
                //startActivity(new Intent(ThongTinBoSungActivity.this, HomeActivity.class));
            }
        });
    }
    private void updateInformation()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String Email = email.getText().toString();
        if (Email == null)
            Email = "Chưa cập nhật";
        String Job = job.getText().toString();
        if (Job == null)
            Job = "Chưa cập nhật";
        String Address = address.getText().toString();
        if (Address == null)
            Address = "Chưa cập nhật";
        String Position = position.getText().toString();
        if (Position == null)
            Position = "Chưa cập nhật";

        database.getReference().child("Users").child(id).child("email").setValue(Email);
        database.getReference().child("Users").child(id).child("job").setValue(Job);
        database.getReference().child("Users").child(id).child("address").setValue(Address);
        database.getReference().child("Users").child(id).child("position").setValue(Position);
    }
}