package com.wandera.wanderaowner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileModel;

import java.util.ArrayList;

public class ManageBusiness extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView businessItemsRecyclerView;
    BussinessListRecyclerViewAdapter bussinessListRecyclerViewAdapter;
    ArrayList<BusinessProfileModel> businessProfileModelArrayList = new ArrayList<>();
    Context context;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business);
        businessItemsRecyclerView = (RecyclerView) findViewById(R.id.businesItemsRV);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        context = ManageBusiness.this;

        bussinessListRecyclerViewAdapter = new BussinessListRecyclerViewAdapter(context,businessProfileModelArrayList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        businessItemsRecyclerView.setLayoutManager(layoutManager);
        businessItemsRecyclerView.setAdapter(bussinessListRecyclerViewAdapter);

        databaseReference.child("businessProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDatabase.getInstance().getReference().child("businessProfiles").orderByChild("userId").startAt(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                BusinessProfileMapModel businessProfileMapModel = dataSnapshot1.getValue(BusinessProfileMapModel.class);
                                BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                                businessProfileModel.setName(businessProfileMapModel.name);
                                System.out.println(businessProfileMapModel.name);
                                businessProfileModelArrayList.add(businessProfileModel);
                            }
                            BusinessProfileModel businessProfileModelAddBusness = new BusinessProfileModel();
                            businessProfileModelAddBusness.setName("Add Business");
                            businessProfileModelArrayList.add(businessProfileModelAddBusness);
                            bussinessListRecyclerViewAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
