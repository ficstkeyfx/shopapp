package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.activities.PlaceOrderActivity;
import com.example.shoppingapp.adapters.MyCartAdapters;
import com.example.shoppingapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartsFragment extends Fragment {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    RecyclerView recyclerView;
    MyCartAdapters myCartAdapters;
    List<MyCartModel> myCartModels;

    TextView userName, userSdt;
    TextView totalPrice;

    EditText address;

    Button buynow;

    int price=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        recyclerView = root.findViewById(R.id.rcMyCart);
        userName = root.findViewById(R.id.cart_name);
        userSdt = root.findViewById(R.id.cart_mobile);
        totalPrice = root.findViewById(R.id.cart_total);
        progressBar = root.findViewById(R.id.progressbar);
        relativeLayout = root.findViewById(R.id.relativeLayout);
        buynow = root.findViewById(R.id.btnBuyNow);
        address = root.findViewById(R.id.edtAddress);

        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        myCartModels = new ArrayList<>();
        myCartAdapters = new MyCartAdapters(myCartModels,getActivity());

        recyclerView.setAdapter(myCartAdapters);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){

                                String documentId = documentSnapshot.getId();

                                MyCartModel myCartModel = documentSnapshot.toObject(MyCartModel.class);

                                myCartModel.setDocumentId(documentId);

                                myCartModels.add(myCartModel);
                                myCartAdapters.notifyDataSetChanged();
                                price += myCartModel.getTotalPrice();
                                totalPrice.setText("Tổng tiền: " + String.valueOf(price) + "vnđ");
                            }
                        }

                    }
                });



        String id = auth.getCurrentUser().getUid();

        DatabaseReference name = database.getReference().child("Users").child(id).child("name");
        name.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String nameChange = snapshot.getValue().toString();
                userName.setText("Họ và tên: " + nameChange);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference phone = database.getReference().child("Users").child(id).child("phone");
        phone.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String sdtChange = snapshot.getValue().toString();
                userSdt.setText("Số điện thoại: " + sdtChange);
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressBar.setVisibility(View.GONE);

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.getText()!=null&&address.getText().length()>0){
                    Intent intent = new Intent(getContext(), PlaceOrderActivity.class);
                    intent.putExtra("itemList", (Serializable) myCartModels);
                    intent.putExtra("address",String.valueOf(address.getText()));
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"Hãy nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }
}