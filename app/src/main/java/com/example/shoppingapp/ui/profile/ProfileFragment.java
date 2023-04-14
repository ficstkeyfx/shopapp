package com.example.shoppingapp.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment
{

    TextView DinhDanh, BoSung;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    TextView ten, name, gender, birth, CCCD, ngayCap, noiCap, ngayHetHan, email, address, job, position, phone, sdt;

    CircleImageView avatar;
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
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

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

        avatar = root.findViewById(R.id.avatar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        DatabaseReference nameChange = database.getReference().child("Users").child(id).child("name");

        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                if(userModel!=null||userModel.getAvatar()!=null||userModel.getAvatar().length()!=0)  {
                                    Glide.with(getContext()).load(userModel.getAvatar()).into(avatar);
                                }else {
                                    Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/shoppingapp-3f5b0.appspot.com/o/avatar.png?alt=media&token=0be702f2-aa4e-4a7c-b7a0-768924e21882").into(avatar);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri profileUri = data.getData();
            avatar.setImageURI(profileUri);

            final StorageReference reference = storage.getReference().child("profile_picture")
                    .child(auth.getCurrentUser().getUid());

            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"Uploaded", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                                    .child("avatar").setValue(uri.toString());
                            Toast.makeText(getContext(),"Profile Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}