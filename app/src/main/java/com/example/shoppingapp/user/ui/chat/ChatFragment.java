package com.example.shoppingapp.user.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.adapters.ChatAdapter;
import com.example.shoppingapp.user.models.ChatModel;
import com.example.shoppingapp.user.models.UserModel;
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

public class ChatFragment extends Fragment {
    ListView lstView;
    EditText msg;
    ImageView send;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ChatAdapter chatAdapter;
    List<ChatModel> lstChat;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    String ava;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_chat, container, false);
        lstView = root.findViewById(R.id.lstView);
        msg = root.findViewById(R.id.msg);
        send = root.findViewById(R.id.send_);
        progressBar = root.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        lstView.setVisibility(View.GONE);
        msg.setVisibility(View.GONE);
        send.setVisibility(View.GONE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        String id = auth.getCurrentUser().getUid();
        lstChat = new ArrayList<>();
        chatAdapter = new ChatAdapter(lstChat);
        lstView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        if(lstView.getAdapter().getCount() == 0) {
            progressBar.setVisibility(View.GONE);
            lstView.setVisibility(View.VISIBLE);
            msg.setVisibility(View.VISIBLE);
            send.setVisibility(View.VISIBLE);
        }

//        firebaseFirestore.collection("AdminChat").document(id).collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
//                    ChatModel chatModel = documentSnapshot.toObject(ChatModel.class);
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
//                            return 0;
//                        }
//                    });
//                    chatAdapter.notifyDataSetChanged();
//                }
//                Collections.sort(lstChat, new Comparator<ChatModel>() {
//                    @Override
//                    public int compare(ChatModel o1, ChatModel o2) {
//                        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm a dd/MM/yy", Locale.ENGLISH);
//                        try {
//                            if(currentDate.parse(o1.getTime()).before(currentDate.parse(o2.getTime()))){
//                                return -1;
//                            }else return 1;
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        return 0;
//                    }
//                });
//                chatAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                lstView.setVisibility(View.VISIBLE);
//                msg.setVisibility(View.VISIBLE);
//                send.setVisibility(View.VISIBLE);
//                lstView.setSelection(lstView.getAdapter().getCount()-1);
//            }
//        });
        firebaseDatabase.getReference().child("Users").child(id).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ava = (String) snapshot.getValue();
                if(ava != null)
                    firebaseFirestore.collection("AdminChat").document(id).collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                if(!documentSnapshot.get("name").equals("Admin")){
                                    firebaseFirestore.collection("AdminChat").document(id).collection("Chat").document(documentSnapshot.getId()).update("avatar",ava);
                                }
                                System.out.println(task.getResult().getDocuments().size());
                            }
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseFirestore.collection("AdminChat").document(id).collection("Chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    Toast.makeText(getActivity(), "Bạn chưa nhập tin nhắn", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    firebaseDatabase.getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            ChatModel newChatModel = new ChatModel();
                            if(userModel.getAvatar()!=null||userModel.getAvatar().length()!=0)  {
                                String ava = userModel.getAvatar();
                                newChatModel.setAvatar(ava);
                            }else {
                                String ava = "https://firebasestorage.googleapis.com/v0/b/shoppingapp-3f5b0.appspot.com/o/avatar.png?alt=media&token=0be702f2-aa4e-4a7c-b7a0-768924e21882";
                                newChatModel.setAvatar(ava);
                            }

                            newChatModel.setName(userModel.getName());

                            String saveCurrentDate, saveCurrentTime;
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                            saveCurrentDate = currentDate.format(calendar.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                            saveCurrentTime = currentTime.format(calendar.getTime());
                            newChatModel.setTime(saveCurrentTime + " " + saveCurrentDate);

                            newChatModel.setMsg(msg.getText().toString());
                            firebaseFirestore.collection("AdminChat").document(id).collection("Chat").add(newChatModel);
                            Map<String, Object> stat = new HashMap<>();
                            stat.put("ID",id);
                            firebaseFirestore.collection("AdminChat").document(id).set(stat);
//                            lstChat.add(newChatModel);

//                            Collections.sort(lstChat, new Comparator<ChatModel>() {
//                                @Override
//                                public int compare(ChatModel o1, ChatModel o2) {
//
//                                    SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm a dd/MM/yy");
//                                    try {
//                                        if(currentDate.parse(o1.getTime()).before(currentDate.parse(o2.getTime()))){
//                                            return -1;
//                                        }else return 1;
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    return 0;
//                                }
//                            });

                            chatAdapter.notifyDataSetChanged();
                            lstView.setSelection(lstView.getAdapter().getCount()-1);
                            msg.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
        return root;
    }
}