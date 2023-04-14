package com.example.shoppingapp;

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
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.activities.HomeActivity;
import com.example.shoppingapp.activities.PlaceOrderActivity;
import com.example.shoppingapp.adapters.MyCartAdapters;
import com.example.shoppingapp.models.MyCartModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class MyCartsFragment extends Fragment implements LocationListener{

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

//        imgGetAddres.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkLocationPermission()) {
//                    detectLocation();
//
//
//                } else {
//                    //không cho phép
//                    requestLocationPermission();
//                }
//            }
//        });
        return root;
    }

    private void detectLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), locationPermission, REQUEST_CODE_LOCATION);
    }

    private boolean checkLocationPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  REQUEST_CODE_LOCATION:{
                if(grantResults.length>0){
                    boolean locationAcepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(locationAcepted){
                        detectLocation();

                    }else {
                        Toast.makeText(getActivity(), "không cho phép truy cập địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //location detected

        latitude = location.getLatitude();
        longtitude =location.getLongitude();

        getCurrentAdress();


    }
    private void getCurrentAdress() {
        Geocoder geocoder ;
        List<Address> addresses;
        if(getContext() !=null){
            geocoder=new Geocoder(getContext(), Locale.getDefault());
            //Set postion
            LatLng latLngUser =new LatLng(latitude ,longtitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latLngUser);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_marker)));

            //add market
            Map.addMarker(markerOptions);
            //zoom
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 15 ));
        }else{

            return;
        }

        try {
            addresses=geocoder.getFromLocation(latitude ,longtitude,1);
            String addresss =addresses.get(0).getAddressLine(0);
            address.setText(addresss);

        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity= (HomeActivity) activity;

    }
}