package com.wandera.wandera.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.UserListDataModel;
import com.wandera.wandera.mapmodel.UserListMapModel;
import com.wandera.wandera.views.BusinessListInboxRecyclerViewAdapter;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {
    String userKey;
    DatabaseReference databaseReference;
    Context context;
    RecyclerView inboxList;
    BusinessListInboxRecyclerViewAdapter businessListInboxRecyclerViewAdapter;
    ArrayList<UserListDataModel> userListDataModelArrayList = new ArrayList<>();
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        inboxList = (RecyclerView) findViewById(R.id.inboxList);
        context = InboxActivity.this;
        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        backBtn = (ImageView) findViewById(R.id.backBtn);
        businessListInboxRecyclerViewAdapter = new BusinessListInboxRecyclerViewAdapter(context,userListDataModelArrayList);
        inboxList.setLayoutManager(new LinearLayoutManager(context));
        inboxList.setAdapter(businessListInboxRecyclerViewAdapter);
        databaseReference.child(Utils.chatUserList_userSide).child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    UserListDataModel userListDataModel = new UserListDataModel();
                    UserListMapModel userListMapModel = dataSnapshot1.getValue(UserListMapModel.class);
                    userListDataModel.setBusinessKey(userListMapModel.businessKey);
                    userListDataModel.setKey(userListMapModel.key);
                    userListDataModel.setUserId(userListMapModel.userId);
                    userListDataModelArrayList.add(userListDataModel);
                }
                businessListInboxRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
