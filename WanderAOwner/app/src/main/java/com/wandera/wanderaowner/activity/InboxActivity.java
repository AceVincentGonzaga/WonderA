package com.wandera.wanderaowner.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.ChatMessageMapModel;
import com.wandera.wanderaowner.mapModel.UserListMapModel;
import com.wandera.wanderaowner.views.UserChatListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class InboxActivity extends AppCompatActivity {
private RecyclerView inboxlist;
UserChatListRecyclerViewAdapter userChatListRecyclerViewAdapter;
Context c;
ArrayList<UserListDataModel> userListDataModelArrayList = new ArrayList<>();
DatabaseReference mdatabase;
String businessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        inboxlist= (RecyclerView)findViewById(R.id.inbox);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        businessKey = getIntent().getExtras().getString("key");
        c = InboxActivity.this;
        userChatListRecyclerViewAdapter= new UserChatListRecyclerViewAdapter(c, userListDataModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
        inboxlist.setLayoutManager(new LinearLayoutManager(c));
        inboxlist.setAdapter(userChatListRecyclerViewAdapter);
        mdatabase.child("chatUserList").child(businessKey).orderByChild("key").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    UserListMapModel userListMapModel = dataSnapshot1.getValue(UserListMapModel.class);
                    UserListDataModel userListDataModel = new UserListDataModel();
                    userListDataModel.setBusinessKey(userListMapModel.businessKey);
                    userListDataModel.setKey(userListMapModel.key);
                    userListDataModel.setUserId(userListMapModel.userId);
                    userListDataModelArrayList.add(userListDataModel);
                }
                Collections.reverse(userListDataModelArrayList);
                userChatListRecyclerViewAdapter.notifyDataSetChanged();
                inboxlist.scrollToPosition(userListDataModelArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userChatListRecyclerViewAdapter.setOnItemClickListener(new UserChatListRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, UserListDataModel userListDataModel) {
                Intent chatActivityIntent= new Intent(InboxActivity.this, ChatActivity.class);
                chatActivityIntent.putExtra("BusinessKey", businessKey);
                chatActivityIntent.putExtra("UserID",userListDataModel.getUserId());
                startActivity(chatActivityIntent);

            }
        });



    }
}
