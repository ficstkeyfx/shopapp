package com.example.shoppingapp.admin.activities.manageOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.adapter.ManageOrderListViewAdapter;
import com.example.shoppingapp.admin.model.ManageOrderPeopleModel;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.example.shoppingapp.user.models.CommentModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ManageOrderActivity extends AppCompatActivity {

    ListView lstView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Storage storage;
    FirebaseFirestore fireStore;
    ImageView goBack;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_manage_order);

        goBack = findViewById(R.id.goBack);
        lstView = findViewById(R.id.view_order);
        progressBar = findViewById(R.id.progressbar);

        lstView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageOrderActivity.this, MenuAdminActivity.class) );
            }
        });
        boolean k = false;
//        System.out.println(fireStore.collection("CurrentUser").);
        DatabaseReference emailRef = database.getReference().child("Users");
        emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ManageOrderPeopleModel> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    String key = dataSnapshot.getKey();
                        fireStore.collection("CurrentUser").document(key).collection("Order").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value!=null&&!value.isEmpty()){
                                    for(DocumentSnapshot documentSnapshot: value.getDocuments()){
                                        ManageOrderPeopleModel order = new ManageOrderPeopleModel();
                                        order.setKey(key);
                                        order.setProductID(documentSnapshot.getId());
                                        String name = dataSnapshot.child("name").getValue().toString();
                                        order.setName(name);
                                        System.out.println(name);
                                        if(dataSnapshot.child("phone").getValue()!=null){
                                            String phone = dataSnapshot.child("phone").getValue().toString();
                                            order.setPhone(phone);
                                        }
                                        if(dataSnapshot.child("avatar").getValue()!=null){
                                            Uri ava = Uri.parse(dataSnapshot.child("avatar").getValue().toString());
                                            order.setAvatar((ava));
                                        }
                                        order.setAddress(documentSnapshot.getString("address"));
                                        order.setProductName(documentSnapshot.getString("productName"));
                                        order.setProductTime(documentSnapshot.getString("productDate"));
                                        order.setProductPrice(String.valueOf(documentSnapshot.get("productPrice")));
                                        order.setProductQuantity(documentSnapshot.getString("totalQuantity"));
                                        order.setTotalPrice(documentSnapshot.getString("totalPrice"));
                                        order.setProductAva(Uri.parse(documentSnapshot.getString("img_url")));
                                        if(documentSnapshot.get("size")!=null)
                                            order.setSize(Integer.parseInt(String.valueOf(documentSnapshot.get("size"))));
                                        if(documentSnapshot.get("status")!=null)
                                            order.setStatus(String.valueOf(documentSnapshot.get("status")));
                                        list.add(order);
                                        Collections.sort(list, new Comparator<ManageOrderPeopleModel>() {
                                            @Override
                                            public int compare(ManageOrderPeopleModel o1, ManageOrderPeopleModel o2) {
                                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                                                try {
                                                    if(currentDate.parse(o1.getProductTime()).before(currentDate.parse(o2.getProductTime()))){
                                                        return 1;
                                                    }else return -1;
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                return 0;
                                            }
                                        });
                                        ManageOrderListViewAdapter orderListViewAdapter = new ManageOrderListViewAdapter(list);
                                        lstView.setAdapter(orderListViewAdapter);

                                    }

                                }

                            }
                        });
                }
                lstView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        System.out.println(lst);

    }
}