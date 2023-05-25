package com.example.shoppingapp.admin.activities.CSKH;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.CSKH_Model;
import com.example.shoppingapp.user.adapters.ChatAdapter;
import com.example.shoppingapp.user.models.ChatModel;
import com.example.shoppingapp.user.models.UserModel;
import com.example.shoppingapp.user.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Detail_CSKH_Activity extends AppCompatActivity {
    ListView lstView;
    EditText msg;
    ImageView send;
    ImageView avatar, goBack;
    TextView name;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ChatAdapter chatAdapter;
    List<ChatModel> lstChat;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    CSKH_Model cskh_model = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_detail_cskh);
        goBack = findViewById(R.id.goBack);
        lstView = findViewById(R.id.lstView);
        msg = findViewById(R.id.msg);
        send = findViewById(R.id.send_);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        lstView.setVisibility(View.GONE);
        msg.setVisibility(View.GONE);
        send.setVisibility(View.GONE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);


        lstChat = new ArrayList<>();
        chatAdapter = new ChatAdapter(lstChat);
        lstView.setAdapter(chatAdapter);

        final Object object = getIntent().getSerializableExtra("detail");

        if (object instanceof CSKH_Model) {
            cskh_model = (CSKH_Model) object;
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Detail_CSKH_Activity.this, CSKH_Activity.class));
                finish();
            }
        });

        Glide.with(Detail_CSKH_Activity.this).load(cskh_model.getAva()).into(avatar);
        name.setText(cskh_model.getName());
//        firebaseFirestore.collection("AdminChat").document(cskh_model.getID()).collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
//                    ChatModel chatModel = documentSnapshot.toObject(ChatModel.class);
//                    Map<String,Object> m = new HashMap<>();
//                    m.put("type",1);
//                    firebaseFirestore.collection("AdminChat").document(cskh_model.getID()).collection("Chat").document(documentSnapshot.getId()).update(m);
//                    lstChat.add(chatModel);
//                    Collections.sort(lstChat, new Comparator<ChatModel>() {
//                        @Override
//                        public int compare(ChatModel o1, ChatModel o2) {
//                            SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm a dd/MM/yy", Locale.ENGLISH);
//                            try {
//                                if(currentDate.parse(o1.getTime()).before(currentDate.parse(o2.getTime()))){
//                                    return -1;
//                                }else return 1;
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//                            return 0;
//                        }
//                    });
//                    chatAdapter.notifyDataSetChanged();
//                    lstView.setSelection(lstView.getAdapter().getCount()-1);
//                    progressBar.setVisibility(View.GONE);
//                    lstView.setVisibility(View.VISIBLE);
//                    msg.setVisibility(View.VISIBLE);
//                    send.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        firebaseFirestore.collection("AdminChat").document(cskh_model.getID()).collection("Chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : value.getDocumentChanges())
                {
                    DocumentSnapshot document = dc.getDocument();
                    switch (dc.getType()) {
                        case ADDED:
                            System.out.println(document.getId());
                            ChatModel chatModel = document.toObject(ChatModel.class);
                            lstChat.add(chatModel);
                            Map<String,Object> m = new HashMap<>();
                            m.put("type",1);
                            firebaseFirestore.collection("AdminChat").document(cskh_model.getID()).collection("Chat").document(document.getId()).update(m);

                            Collections.sort(lstChat, new Comparator<ChatModel>() {
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
                            chatAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            lstView.setVisibility(View.VISIBLE);
                            msg.setVisibility(View.VISIBLE);
                            send.setVisibility(View.VISIBLE);
                            lstView.setSelection(lstView.getAdapter().getCount()-1);
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:

                            break;
                    }
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().toString().trim().equals("") || msg.getText() == null)
                {

                    Toast.makeText(Detail_CSKH_Activity.this, "Bạn chưa nhập tin nhắn", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    String id = cskh_model.getID();
                    ChatModel chatModel = new ChatModel();
                    chatModel.setAvatar("https://firebasestorage.googleapis.com/v0/b/shoppingapp-3f5b0.appspot.com/o/icon.png?alt=media&token=409eb3d0-0ae4-462a-9a0d-a5a338822617");
                    chatModel.setName("Admin");
                    chatModel.setMsg(msg.getText().toString().trim());
                    chatModel.setType(0);
                    String saveCurrentDate, saveCurrentTime;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                    saveCurrentDate = currentDate.format(calendar.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                    saveCurrentTime = currentTime.format(calendar.getTime());
                    chatModel.setTime(saveCurrentTime + " " + saveCurrentDate);

                    Collections.sort(lstChat, new Comparator<ChatModel>() {
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
                    msg.setText("");
                    firebaseFirestore.collection("AdminChat").document(id).collection("Chat").add(chatModel);
                    chatAdapter.notifyDataSetChanged();
                    lstView.setSelection(lstView.getAdapter().getCount()-1);
                }

            }
        });
    }
}