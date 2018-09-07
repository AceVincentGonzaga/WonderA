package com.wandera.wanderaowner.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.datamodel.ChatsDataModel;
import com.wandera.wanderaowner.mapModel.ChatListMapModel;
import com.wandera.wanderaowner.mapModel.ChatMessageMapModel;
import com.wandera.wanderaowner.views.ChatsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    String businessKey,userId;
    Toolbar toolbar;
    Context c;
    ArrayList<ChatsDataModel> chatsDataModelArrayList = new ArrayList<>();
    DatabaseReference mDatabase;
    ChatsRecyclerViewAdapter chatsRecyclerViewAdapter;
    RecyclerView chatlistRecyclerView;
    EditText inputMessage;
    ImageView sndBtn;
    String businessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        sndBtn = (ImageView) findViewById(R.id.sndBtn);
        c = ChatActivity.this;

        chatlistRecyclerView = (RecyclerView) findViewById(R.id.chats);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        businessKey = getIntent().getExtras().getString("BusinessKey");
        userId = getIntent().getExtras().getString("UserID");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(getEditeText(inputMessage));
            }
        });

        chatsRecyclerViewAdapter = new ChatsRecyclerViewAdapter(ChatActivity.this,chatsDataModelArrayList);//passing context and Data Arraylist to Adapter

        chatlistRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));//setting linear Layout manager to recyclerview

        chatlistRecyclerView.setAdapter(chatsRecyclerViewAdapter); //setting adapter to recyclerView
        mDatabase.child("users").child(userId).child("userName").addValueEventListener(new ValueEventListener() {//getting username using userID
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toolbar.setTitle(dataSnapshot.getValue(String.class));



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("businessProfiles").child(businessKey).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                businessName= dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("chats").child(businessKey).child(userId).addValueEventListener(new ValueEventListener() {//getting chats using businessID and UserID
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            chatsDataModelArrayList.clear();
                for (DataSnapshot newDataSnapShot:dataSnapshot.getChildren()){//PASSING LIST OF SNAPCHAT CHILD TO ARRAYLIST
                    ChatMessageMapModel chatMessageMapModel = newDataSnapShot.getValue(ChatMessageMapModel.class);
                    ChatsDataModel chatsDataModel = new ChatsDataModel();
                    chatsDataModel.setMessage(chatMessageMapModel.message);
                    chatsDataModel.setTimeStamp(chatMessageMapModel.timeStamp);
                    chatsDataModel.setUserId(chatMessageMapModel.userId);
                    chatsDataModel.setSenderId(chatMessageMapModel.senderId);
                    chatsDataModel.setBusinessId(chatMessageMapModel.businessId);
                    chatsDataModelArrayList.add(chatsDataModel);
                }
                chatsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setSeenToTrue();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String getEditeText(EditText editText)
    {
        return editText.getText().toString();
    }

    private void sendMessage(String message){
       final String key = mDatabase.push().getKey();
       ChatMessageMapModel chatMessageMapModel = new ChatMessageMapModel(userId, businessName,
               Utils.getDateToStrig(), message, businessKey,businessKey);
                Map<String,Object> profileValue = chatMessageMapModel.toMap();
        final Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(key,profileValue);

        mDatabase.child("chats").child(businessKey).child(userId).updateChildren(childupdates).
            addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    inputMessage.setText("");
                    ChatListMapModel chatListMapModel = new ChatListMapModel(userId, key, businessKey);
                    Map<String, Object> userKey = chatListMapModel.toMap();
                    final Map<String, Object>updates = new HashMap<>();
                    updates.put(userId,userKey);
                    mDatabase.child("chatUserList").child(businessKey).updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    final Map<String, Object>updatesUserSide = new HashMap<>();
                    updatesUserSide.put(businessKey,userKey);
                    mDatabase.child(Utils.chatUserList_userSide).child(userId).updateChildren(updatesUserSide).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            });
        }

        private void setSeenToTrue(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("seen_owner", true);
            mDatabase.child("chatUserList").child(businessKey).child(userId).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
}
