package com.example.shoppingapp.admin.activities.CSKH;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.adapter.CSKH_Adapter;
import com.example.shoppingapp.admin.model.CSKH_Model;
import com.example.shoppingapp.user.models.ChatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CSKH_Activity extends AppCompatActivity {
    ListView lstView;
    ProgressBar progressBar;
    LinearLayout layout;

    CSKH_Adapter cskh_adapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cskh);

        lstView = findViewById(R.id.lstView);

        layout = findViewById(R.id.linearLayout14);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        lstView.setVisibility(View.GONE);

        List<CSKH_Model> lstCSKH = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        cskh_adapter = new CSKH_Adapter(lstCSKH);
        lstView.setAdapter(cskh_adapter);

        firebaseFirestore.collection("AdminChat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    CSKH_Model cskh_model = new CSKH_Model();
                    Map<String,Object> Map = documentSnapshot.getData();
                    String ID = Map.get("ID").toString();
                    cskh_model.setID(ID);
                    firebaseFirestore.collection("AdminChat").document(ID).collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {

                                ChatModel chatModel = documentSnapshot.toObject(ChatModel.class);
                                cskh_model.setName(chatModel.getName());
                                System.out.println(chatModel.getName());
                                cskh_model.setAva(chatModel.getAvatar());
                                cskh_model.setMsg(chatModel.getMsg());
                                cskh_model.setTime(chatModel.getTime().substring(0,8));
                                cskh_model.setType(chatModel.getType());
                            }
                            lstCSKH.add(cskh_model);
                            Collections.sort(lstCSKH, new Comparator<CSKH_Model>() {
                                @Override
                                public int compare(CSKH_Model o1, CSKH_Model o2) {
                                    SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm a dd/MM/yy", Locale.ENGLISH);
                                    try {
                                        if(currentDate.parse(o1.getTime()).before(currentDate.parse(o2.getTime()))){
                                            return 1;
                                        }else return -1;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });
                            cskh_adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            lstView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });


    }
}