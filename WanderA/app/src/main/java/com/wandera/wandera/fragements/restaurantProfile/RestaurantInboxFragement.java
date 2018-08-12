package com.wandera.wandera.fragements.restaurantProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.ChatDataModel;
import com.wandera.wandera.ChatListMapModel;
import com.wandera.wandera.views.ChatListRecyclerViewAdapter;
import com.wandera.wandera.ChatMessageMapModel;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.businessProfiles.RestaurantProfileBotNav;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantInboxFragement extends Fragment {

    String businessKey;
    DatabaseReference mDatabase;
    EditText inputMessage;
    ImageView sndBtn;
    String businessName;
    FirebaseAuth mAuth;
    RecyclerView chatList;


    ChatListRecyclerViewAdapter chatListRecyclerViewAdapter;
    ArrayList<ChatDataModel> chatDataModels= new ArrayList<>();
    RestaurantProfileBotNav act;

    public RestaurantInboxFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        act = (RestaurantProfileBotNav) getActivity();
        View view = inflater.inflate(R.layout.frag_restaurant_inbox, container, false);
        businessKey = act.getBusinessKey();
        inputMessage = (EditText) view.findViewById(R.id.selectMunicipality);

        sndBtn = (ImageView) view.findViewById(R.id.sendBtn);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        chatList = (RecyclerView) view.findViewById(R.id.chatList);
        mAuth = FirebaseAuth.getInstance();




        if (businessKey !=null){
            mDatabase.child(Utils.businessProfiel).child(businessKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);

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



        chatListRecyclerViewAdapter = new ChatListRecyclerViewAdapter(getActivity(), chatDataModels);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatListRecyclerViewAdapter);
        getChatList();
        return view;
    }
    private void sendMessage(String message){
        final String key = mDatabase.push().getKey();
        final String userID= mAuth.getCurrentUser().getUid();
        String date = Utils.getDateToStrig();
        String userName= mAuth.getCurrentUser().getDisplayName();
        String userImagePath= mAuth.getCurrentUser().getPhotoUrl().toString();
        ChatMessageMapModel chatMessageMapModel= new ChatMessageMapModel(userID, userName, date, message, businessKey, userImagePath, userID);
        Map<String,Object> profileValue = chatMessageMapModel.toMap();
        final Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(key,profileValue);

        mDatabase.child(Utils.chats).child(businessKey).child(userID).updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Utils.callToast(getActivity(),"Message Sent");
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
                final Map<String, Object>updatesUserSide = new HashMap<>();
                updatesUserSide.put(businessKey,userKey);
                mDatabase.child(Utils.chatUserList_userSide).child(userID).updateChildren(updatesUserSide).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    chatDataModel.setSenderId(chatMessageMapModel.senderId);
                    chatDataModel.setBusinessId(chatMessageMapModel.businessId);
                    chatDataModels.add(chatDataModel);
                }
                chatListRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
