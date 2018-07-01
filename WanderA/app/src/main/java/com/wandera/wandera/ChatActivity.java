package com.wandera.wandera;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    String businessKey;
    DatabaseReference mDatabase;
    EditText inputMessage;
    ImageView sndBtn;
    String businessName;
    FirebaseAuth mAuth;
    RecyclerView chatList;
    ChatListRecyclerViewAdapter chatListRecyclerViewAdapter;
    ArrayList <ChatDataModel> chatDataModels= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        inputMessage = (EditText) findViewById(R.id.inptMessage);
        sndBtn = (ImageView) findViewById(R.id.sendBtn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        businessKey = getIntent().getExtras().getString("key");
        chatList = (RecyclerView)findViewById(R.id.chatList);
        mAuth = FirebaseAuth.getInstance();


        if (businessKey !=null){
            mDatabase.child(Utils.businessProfiel).child(businessKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                    toolbar.setTitle(businessProfileMapModel.name);
                    businessName = businessProfileMapModel.name;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputMessage.getText().equals("")){
                    sendMessage(inputMessage.getText().toString());
                }
            }
        });

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withToolbarMenuToggle(toolbar)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withRootViewScale(0.7f)
                .withRootViewYTranslation(4)
                .withMenuLayout(R.layout.chat_root_nav)
                .withSavedState(savedInstanceState)
                .withContentClickableWhenMenuOpened(true)
                .inject();

        chatListRecyclerViewAdapter = new ChatListRecyclerViewAdapter(ChatActivity.this, chatDataModels);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatListRecyclerViewAdapter);

        getChatList();
    }

    private void sendMessage(String message){
        final String key = mDatabase.push().getKey();
        final String userID= mAuth.getCurrentUser().getUid();
        String date = Utils.getDateToStrig();
        String userName= mAuth.getCurrentUser().getDisplayName();
        String userImagePath= mAuth.getCurrentUser().getPhotoUrl().toString();
        ChatMessageMapModel chatMessageMapModel= new ChatMessageMapModel(userID, userName, date, message, businessKey, userImagePath);
        Map<String,Object> profileValue = chatMessageMapModel.toMap();
        final Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(key,profileValue);

        mDatabase.child(Utils.chats).child(businessKey).child(userID).updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Utils.callToast(ChatActivity.this,"Message Sent");
                inputMessage.setText("");
                ChatListMapModel chatListMapModel = new ChatListMapModel(userID, key, businessKey);
                Map<String, Object> userKey = chatListMapModel.toMap();
                final Map<String, Object>updates = new HashMap<>();
                updates.put(userID,userKey);
                mDatabase.child(Utils.chatUserList).child(businessKey).updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }
        });

    }

   private void getChatList(){
        mDatabase.child(Utils.chats).child(businessKey).child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatDataModels.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ChatMessageMapModel chatMessageMapModel = dataSnapshot1.getValue(ChatMessageMapModel.class);
                    ChatDataModel chatDataModel = new ChatDataModel();
                    chatDataModel.setMessage(chatMessageMapModel.message);
                    chatDataModel.setUserId(chatMessageMapModel.userId);
                    chatDataModel.setTimeStamp(chatMessageMapModel.timeStamp);
                    chatDataModels.add(chatDataModel);
                }
                chatListRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
   }


/*    private void saveProfile(String name,String address,String contact,String emaill){
        String uid = mAuth.getUid();
        String key = databaseReference.push().getKey();
        BusinessProfileMapModel businessProfileMapModel = new BusinessProfileMapModel(uid,name,address,contact,emaill,businessType,"null for now",key);
        Map<String,Object> profileValue = businessProfileMapModel.toMap();
        Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(key,profileValue);

        databaseReference.child("businessProfiles").updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(context,ManageBusiness.class);
                startActivity(i);
                finish();
            }
        });
    }*/
}
