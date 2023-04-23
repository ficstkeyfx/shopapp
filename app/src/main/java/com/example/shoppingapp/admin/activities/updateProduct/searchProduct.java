package com.example.shoppingapp.admin.activities.updateProduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.example.shoppingapp.admin.adapter.ManageProductListViewAdapter;
import com.example.shoppingapp.admin.model.ManageProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class searchProduct extends AppCompatActivity {

    ListView lstView;
    FirebaseFirestore fireStore;
    ImageView goBack;
    EditText search;
    ManageProductListViewAdapter productListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        goBack = findViewById(R.id.goBack);
        search= findViewById(R.id.search);
        lstView = findViewById(R.id.lstView);
        fireStore = FirebaseFirestore.getInstance();


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(searchProduct.this, MenuAdminActivity.class) );
            }
        });

        ArrayList<ManageProductModel> lst = new ArrayList<>();

        fireStore.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                lst.clear();
                for (DocumentSnapshot doc: task.getResult())
                {
                    String name = doc.getString("name");
                    String type = doc.getString("type");
                    Uri imgProduct = Uri.parse(doc.getString("img_url"));
                    ManageProductModel product = new ManageProductModel(name, type, imgProduct);
                    lst.add(product);
                }
                productListViewAdapter = new ManageProductListViewAdapter(lst);
                lstView.setAdapter(productListViewAdapter);
            }

        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = search.getText().toString();
                fireStore.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        lst.clear();
                        for (DocumentSnapshot doc: task.getResult())
                        {
                            String name = doc.getString("name");
                            String type = doc.getString("type");
                            Uri imgProduct = Uri.parse(doc.getString("img_url"));
                            ManageProductModel product = new ManageProductModel(name, type, imgProduct);
                            if (name.toLowerCase().contains(s.toLowerCase()))
                            {
                                lst.add(product);
                            }
                        }
                        productListViewAdapter = new ManageProductListViewAdapter(lst);
                        lstView.setAdapter(productListViewAdapter);
                    }

                });
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ManageProductModel product = (ManageProductModel)  productListViewAdapter.getItem(i);
                Intent intent = new Intent(searchProduct.this, updateProduct.class);
                intent.putExtra("key_1", product.getName());
                startActivity(intent);
            }
        });


    }
}