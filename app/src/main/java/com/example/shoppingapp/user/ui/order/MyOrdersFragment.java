package com.example.shoppingapp.user.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.adapters.MyOrderAdapter;
import com.example.shoppingapp.user.models.ChatModel;
import com.example.shoppingapp.user.models.MyOrderModel;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {

    RecyclerView rcOrder ;
    ProgressBar progressBar;
    MyOrderAdapter myOrderAdapter ;
    List<MyOrderModel> myOrders ;
    FirebaseFirestore db ;
    FirebaseAuth auth ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcOrder =view.findViewById(R.id.rcOrder) ;

        progressBar = view.findViewById(R.id.progressbar);
        rcOrder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        db =FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();


        rcOrder.setLayoutManager(new LinearLayoutManager(getContext() ,LinearLayoutManager.VERTICAL,false));
        myOrders = new ArrayList<>() ;
        myOrderAdapter = new MyOrderAdapter(getContext(),myOrders);
        rcOrder.setAdapter(myOrderAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("Order").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){
                    for(DocumentSnapshot documentSnapshot :value.getDocuments()){
                        String id =documentSnapshot.getId();
                        MyOrderModel myOrder =documentSnapshot.toObject(MyOrderModel.class);
                        myOrder.setOrder_ID(id);
                        myOrders.add(myOrder);
                    }
                    Collections.sort(myOrders, new Comparator<MyOrderModel>() {
                        @Override
                        public int compare(MyOrderModel o1, MyOrderModel o2) {
                            SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm a dd/MM/yy", Locale.ENGLISH);
                            System.out.println(o1.getProductTime() + " " + o1.getProductDate());
                            try {
                                if(currentDate.parse(o1.getProductTime() + " " + o1.getProductDate()).before(currentDate.parse(o2.getProductTime() + " " + o2.getProductDate()))){
                                    return 1;
                                }else return -1;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });
                }

                myOrderAdapter.notifyDataSetChanged();
                rcOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });



    }
}