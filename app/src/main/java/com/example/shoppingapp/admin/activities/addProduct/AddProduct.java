package com.example.shoppingapp.admin.activities.addProduct;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.models.ViewAllModel;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddProduct extends AppCompatActivity {

    ImageView img, goBack;
    Button save_btn, clear_btn;
    EditText name, price, type, decrip;

    FirebaseFirestore fireStore;
    FirebaseStorage storage;

    String idProduct;
    String urlImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        img = findViewById(R.id.img);
        goBack = findViewById(R.id.goBack);
        save_btn = findViewById(R.id.save_btn);
        clear_btn = findViewById(R.id.clear_btn);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        type = findViewById(R.id.type);
        decrip = findViewById(R.id.decrip);
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddProduct.this, MenuAdminActivity.class));
            }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                price.setText("");
                type.setText("");
                decrip.setText("");
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idProduct = name.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSp,loaiSp,moTa;
                int giaSp;
                tenSp = name.getText().toString();
                idProduct = tenSp;
                if (price.getText().toString().equals("") == false ) {
                    try{
                        giaSp = Integer.parseInt(price.getText().toString());
                    }
                    catch (Exception e) {
                        Toast.makeText(AddProduct.this, "Nhập sai định dạng giá", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else
                     giaSp = 0;
                loaiSp = type.getText().toString();
                moTa = decrip.getText().toString();

                if(TextUtils.isEmpty(tenSp)){
                    Toast.makeText(AddProduct.this, "Tên sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(giaSp == 0){
                    Toast.makeText(AddProduct.this, "Giá sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(loaiSp)){
                    Toast.makeText(AddProduct.this, "Loại sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((TextUtils.equals(loaiSp, "adidas") == false) && (TextUtils.equals(loaiSp, "nike") == false) && (TextUtils.equals(loaiSp, "new balance") == false) && (TextUtils.equals(loaiSp, "converse") == false)&& (TextUtils.equals(loaiSp, "gucci") == false)){
                    Toast.makeText(AddProduct.this, "Loại sản phẩm bị sai", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(moTa)){
                    Toast.makeText(AddProduct.this, "Mô tả sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(urlImg==null){
                    Toast.makeText(AddProduct.this, "Ảnh sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                fireStore.collection("AllProducts").add(new ViewAllModel(tenSp ,moTa, "", urlImg, loaiSp, giaSp));
                Toast.makeText(AddProduct.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(idProduct==null||idProduct.equals("")){
            Toast.makeText(AddProduct.this,"Hãy nhập tên sản phẩm trước",Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri profileUri = data.getData();
            img.setImageURI(profileUri);
            final StorageReference reference = storage.getReference().child("product_picture")
                    .child(idProduct);
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddProduct.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlImg = uri.toString();
                            Toast.makeText(AddProduct.this,"Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}