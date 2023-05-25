package com.example.shoppingapp.admin.activities.CSKH;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
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
import com.google.firebase.firestore.DocumentChange;
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
    ImageView goBack;
    CSKH_Adapter cskh_adapter;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_cskh);

        lstView = findViewById(R.id.lstView);
        goBack = findViewById(R.id.goBack);
        layout = findViewById(R.id.linearLayout14);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        lstView.setVisibility(View.GONE);

        List<ChatModel> lstCSKH = new ArrayList<>();
        List<CSKH_Model> lst = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        cskh_adapter = new CSKH_Adapter(lst);
        lstView.setAdapter(cskh_adapter);
        cskh_adapter.notifyDataSetChanged();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CSKH_Activity.this, MenuAdminActivity.class));
                finish();
            }
        });

        firebaseFirestore.collection("AdminChat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                {
                    CSKH_Model cskh_model = new CSKH_Model();
                    Map<String,Object> Map = documentSnapshot.getData();
                    String ID = Map.get("ID").toString();
                    cskh_model.setID(ID);
                    firebaseFirestore.collection("AdminChat").document(ID).collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments())
                            {
                                ChatModel chatModel = documentSnapshot.toObject(ChatModel.class);
                                lstCSKH.add(chatModel);
                            }
                            System.out.println(lstCSKH.size() + "-------------");
                            Collections.sort(lstCSKH, new Comparator<ChatModel>() {
                                @Override
                                public int compare(ChatModel o1, ChatModel o2) {
                                    SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm a dd/MM/yy", Locale.ENGLISH);
                                    try {
                                        if(currentDate.parse(o1.getTime()).before(currentDate.parse(o2.getTime()))){
                                            return -1;
                                        }else return 1;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });

                            for (ChatModel chatModel: lstCSKH)
                            {
                                if (!chatModel.getName().equals("Admin"))
                                {
                                    cskh_model.setName(chatModel.getName());
                                    cskh_model.setAva(chatModel.getAvatar());
                                    cskh_model.setType(chatModel.getType());
                                    cskh_model.setMsg(chatModel.getMsg());
                                    cskh_model.setTime(chatModel.getTime());
                                }
                                else
                                {
                                    cskh_model.setType(1);
                                    cskh_model.setMsg("Bạn: " + chatModel.getMsg());
                                    cskh_model.setTime(chatModel.getTime());
                                }
                            }
                            lstCSKH.clear();
                            System.out.println(cskh_model.getName() + "------------");
                            lst.add(cskh_model);
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