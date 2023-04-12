package com.example.shoppingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapters.ViewAllAdapters;
import com.example.shoppingapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    ViewAllAdapters viewAllAdapters;
    List<ViewAllModel> viewAllModelList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.view_all_rec);
//        toolbar = findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        firebaseFirestore = FirebaseFirestore.getInstance();
        viewAllModelList = new ArrayList<>();
        viewAllAdapters = new ViewAllAdapters(this,viewAllModelList);

        recyclerView.setAdapter(viewAllAdapters);

        // Getting fruits
        if(type != null && type.equalsIgnoreCase("fruit")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","fruit").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        // Getting vegetable
        if(type != null && type.equalsIgnoreCase("vegetable")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","vegetable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        // Getting milk
        if(type != null && type.equalsIgnoreCase("milk")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","milk").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        // Getting egg
        if(type != null && type.equalsIgnoreCase("egg")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","egg").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
        // Getting fish
        if(type != null && type.equalsIgnoreCase("fish")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","fish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapters.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}