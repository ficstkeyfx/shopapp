package com.example.shoppingapp.ui.admin.updateProduct;

import androidx.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.ui.admin.addProduct.AddProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class updateProduct extends AppCompatActivity {
    ImageView img, goback;
    EditText name, type, decrip, price;
    Button save;
    String id ;
    FirebaseFirestore fireStore;
    String idProduct, urlImg;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        storage = FirebaseStorage.getInstance();
        img = findViewById(R.id.img);
        goback = findViewById(R.id.goBack);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        decrip = findViewById(R.id.decrip);
        price = findViewById(R.id.price);
        save = findViewById(R.id.save_btn);
        fireStore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String nameProduct = intent.getStringExtra("key_1");

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(updateProduct.this, searchProduct.class));
            }
        });

        fireStore.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc: task.getResult())
                {
                    String nameP = doc.getString("name");
                    if (nameProduct.equalsIgnoreCase(nameP))
                    {
                        id = doc.getId();
                        name.setText(nameP);
                        String typeP = doc.getString("type");
                        type.setText(typeP);
                        Uri imgProduct = Uri.parse(doc.getString("img_url"));
                        Glide.with(updateProduct.this).load(imgProduct).into(img);
                        String descrip = doc.getString("description");
                        decrip.setText(descrip);
                        String priceP = doc.get("price").toString();
                        price.setText(priceP);
                    }
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(updateProduct.this, "Tên sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(price.getText().toString())){
                    Toast.makeText(updateProduct.this, "Giá sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(type.getText().toString())){
                    Toast.makeText(updateProduct.this, "Loại sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((TextUtils.equals(type.getText().toString(), "adidas") == false) && (TextUtils.equals(type.getText().toString(), "nike") == false) && (TextUtils.equals(type.getText().toString(), "new balance") == false) && (TextUtils.equals(type.getText().toString(), "converse") == false)&& (TextUtils.equals(type.getText().toString(), "gucci") == false)){
                    Toast.makeText(updateProduct.this, "Loại sản phẩm bị sai", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(decrip.getText().toString())){
                    Toast.makeText(updateProduct.this, "Mô tả sản phẩm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                fireStore.collection("AllProducts").document(id).update("name", name.getText().toString());
                fireStore.collection("AllProducts").document(id).update("price",Integer.parseInt(price.getText().toString()));
                fireStore.collection("AllProducts").document(id).update("type", type.getText().toString());
                fireStore.collection("AllProducts").document(id).update("description", decrip.getText().toString());
                if(urlImg!=null){
                    fireStore.collection("AllProducts").document(id).update("img_url", urlImg);
                }
                Toast.makeText(updateProduct.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(updateProduct.this, searchProduct.class));
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

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri profileUri = data.getData();
            img.setImageURI(profileUri);
            final StorageReference reference = storage.getReference().child("product_picture")
                    .child(idProduct);
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(updateProduct.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlImg = uri.toString();
                            Toast.makeText(updateProduct.this,"Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}