package com.example.shoppingapp.user.ui.cart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.activities.HomeActivity;
import com.example.shoppingapp.user.activities.PayActivity;
import com.example.shoppingapp.user.adapters.MapAdapter;
import com.example.shoppingapp.user.adapters.MyCartAdapters;
import com.example.shoppingapp.user.models.MyCartModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyCartsFragment extends Fragment implements OnMapReadyCallback {

    static View root;

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

    ImageView imgGetAddres;
    EditText address;

    Button buynow;
    // gg map
    LocationManager locationManager;
    String[] locationPermission;
    final int REQUEST_CODE_LOCATION = 124;
    public double latitude ,longtitude;
    GoogleMap Map ;
    private HomeActivity activity;

    static int price=0;
    int type = 0;

    public static void updateView(int _price) {
        price = _price;
        if(price == 0) ((TextView) root.findViewById(R.id.cart_total)).setText("Tổng tiền:          " + String.valueOf(price) + "đ");
        else ((TextView) root.findViewById(R.id.cart_total)).setText("Tổng tiền:          " + String.valueOf(price) + "000đ");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        price = 0;
        root = inflater.inflate(R.layout.fragment_my_carts, container, false);

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
        imgGetAddres = root.findViewById(R.id.imgGetAddress);
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
                                totalPrice.setText("Tổng tiền:          " + String.valueOf(price) + "000đ");
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
                userName.setText("Họ và tên:          " + nameChange);
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
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
                userSdt.setText("Số điện thoại:   " + sdtChange);
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSdt.getText().equals("Số điện thoại:   Chưa cập nhật"))
                {
                    Toast.makeText(getActivity(),"Vui lòng cập nhật số điện thoại ở thông tin tài khoản",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (myCartModels.isEmpty())
                {
                    Toast.makeText(getActivity(),"Bạn không có hàng để mua",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 0)
                {
                    Toast.makeText(getActivity(),"Vui lòng xác định vị trí trước khi mua",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(address.getText()!=null&&address.getText().length()>0){
                    Intent intent = new Intent(getContext(), PayActivity.class);
                    intent.putExtra("itemList", (Serializable) myCartModels);
                    intent.putExtra("name",String.valueOf(userName.getText()));
                    intent.putExtra("number",String.valueOf(userSdt.getText()));
                    intent.putExtra("address",String.valueOf(address.getText()));
                    intent.putExtra("price",String.valueOf(price) + "000");
                    startActivity(intent);

//                    Intent intent = new Intent(getContext(), PlaceOrderActivity.class);
//                    intent.putExtra("itemList", (Serializable) myCartModels);
//                    intent.putExtra("address",String.valueOf(address.getText()));
//                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"Hãy nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }

            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //GetAddress
        locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragMaps);

        imgGetAddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressUser = address.getText().toString();
                List<Address> addressList = null;
                if (!addressUser.isEmpty() || !addressUser.equals(""))
                {
                    Geocoder geocoder  = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(addressUser ,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address add = (addressList.isEmpty() ? null : addressList.get(0));
                    if (add != null )
                    {
                        LatLng latLng = new LatLng(add.getLatitude(), add.getLongitude());
                        Map.addMarker(new MarkerOptions().position(latLng).title(addressUser));
                        Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        type = 1;
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Bạn chưa nhập đúng địa chỉ",Toast.LENGTH_SHORT).show();
                        type = 0;
                    }

                }
            }
        });
        supportMapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Map = googleMap;
    }

}