package com.example.shoppingapp.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.shoppingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment
{

    TextView DinhDanh, BoSung;
    FirebaseDatabase database;
    TextView ten, name, gender, birth, CCCD, ngayCap, noiCap, ngayHetHan, email, address, job, position, phone, sdt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile,container,false);
        clickOn(root);
        return root;
    }

    private void clickOn(View root)
    {
        DinhDanh = root.findViewById(R.id.UpdateDinhDanh);
        BoSung = root.findViewById(R.id.UpdateBoSung);

        database = FirebaseDatabase.getInstance();

        email = root.findViewById(R.id.email);
        sdt = root.findViewById(R.id.sdt);
        phone = root.findViewById(R.id.phone);
        address = root.findViewById(R.id.address);
        job = root.findViewById(R.id.job);
        position = root.findViewById(R.id.position);
        ten = root.findViewById(R.id.ten);
        name = root.findViewById(R.id.name);
        gender = root.findViewById(R.id.gender);
        birth = root.findViewById(R.id.birth);
        CCCD = root.findViewById(R.id.cmnd);
        ngayCap = root.findViewById(R.id.ngayCap);
        noiCap = root.findViewById(R.id.noiCap);
        ngayHetHan = root.findViewById(R.id.ngayHetHan);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        DatabaseReference nameChange = database.getReference().child("Users").child(id).child("name");
        nameChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataName = snapshot.getValue().toString();
                name.setText(dataName);
                ten.setText(dataName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference phoneChange = database.getReference().child("Users").child(id).child("phone");
        phoneChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataPhone = snapshot.getValue().toString();
                phone.setText(dataPhone);
                sdt.setText(dataPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference genderChange = database.getReference().child("Users").child(id).child("gender");
        genderChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataGender = snapshot.getValue().toString();
                gender.setText(dataGender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference birthChange = database.getReference().child("Users").child(id).child("birth");
        birthChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataBirth = snapshot.getValue().toString();
                birth.setText(dataBirth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference cmndChange = database.getReference().child("Users").child(id).child("cccd");
        cmndChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataCCCD = snapshot.getValue().toString();
                CCCD.setText(dataCCCD);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ngayCapChange = database.getReference().child("Users").child(id).child("ngayCap");
        ngayCapChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String datangayCap = snapshot.getValue().toString();
                ngayCap.setText(datangayCap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference noiCapChange = database.getReference().child("Users").child(id).child("noiCap");
        noiCapChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String datanoiCap = snapshot.getValue().toString();
                noiCap.setText(datanoiCap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ngayHetHanChange = database.getReference().child("Users").child(id).child("ngayHetHan");
        ngayHetHanChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String datangayHetHan = snapshot.getValue().toString();
                ngayHetHan.setText(datangayHetHan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference emailChange = database.getReference().child("Users").child(id).child("email");
        emailChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataEmail = snapshot.getValue().toString();
                email.setText(dataEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference jobChange = database.getReference().child("Users").child(id).child("job");
        jobChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataJob = snapshot.getValue().toString();
                job.setText(dataJob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference addressChange = database.getReference().child("Users").child(id).child("address");
        addressChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataAddress = snapshot.getValue().toString();
                address.setText(dataAddress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference positionChange = database.getReference().child("Users").child(id).child("position");
        positionChange.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dataPosition = snapshot.getValue().toString();
                position.setText(dataPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        DinhDanh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), ThongTinDinhDanh.class));
            }
        });

        BoSung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), ThongTinBoSungActivity.class));
            }
        });
    }

}