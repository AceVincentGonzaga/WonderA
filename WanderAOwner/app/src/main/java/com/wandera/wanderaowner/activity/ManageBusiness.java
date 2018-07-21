package com.wandera.wanderaowner.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.datamodel.BusinessProfileModel;
import com.wandera.wanderaowner.views.BussinessListRecyclerViewAdapter;

import java.util.ArrayList;

public class ManageBusiness extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView businessItemsRecyclerView;
    BussinessListRecyclerViewAdapter bussinessListRecyclerViewAdapter;
    ArrayList<BusinessProfileModel> businessProfileModelArrayList = new ArrayList<>();
    Context context;
    FirebaseAuth mAuth;
    ConstraintLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business);
        businessItemsRecyclerView = (RecyclerView) findViewById(R.id.businesItemsRV);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        context = ManageBusiness.this;
        container= (ConstraintLayout)findViewById(R.id.container);

        bussinessListRecyclerViewAdapter = new BussinessListRecyclerViewAdapter(context,businessProfileModelArrayList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        businessItemsRecyclerView.setLayoutManager(layoutManager);
        businessItemsRecyclerView.setAdapter(bussinessListRecyclerViewAdapter);

        BusinessProfileModel businessProfileModelAddBusness = new BusinessProfileModel();
        businessProfileModelAddBusness.setName("Add Business");
        businessProfileModelArrayList.add(businessProfileModelAddBusness);
        bussinessListRecyclerViewAdapter.notifyDataSetChanged();


        databaseReference.child("businessProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                                FirebaseDatabase.getInstance().getReference().child("businessProfiles").orderByChild("userId").startAt(mAuth.getUid().toString()).endAt(mAuth.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        businessProfileModelArrayList.clear();
                                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                                            BusinessProfileMapModel businessProfileMapModel = dataSnapshot1.getValue(BusinessProfileMapModel.class);
                                            BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                                            businessProfileModel.setName(businessProfileMapModel.name);
                                            businessProfileModel.setKey(businessProfileMapModel.key);
                                            businessProfileModel.setRestoProfileImagePath(businessProfileMapModel.restoProfileImagePath);
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

                                try {

                                }catch (NullPointerException e){
                                    Snackbar.make(container,e.toString(),Snackbar.LENGTH_SHORT).show();
                                }
                            }

                        },
                        5000
                );
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bussinessListRecyclerViewAdapter.setOnItemClickListener(new BussinessListRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, BusinessProfileModel businessProfileModelArraylist) {
                Intent i = new Intent(ManageBusiness.this, BusinessProfile.class);
                i.putExtra("key",businessProfileModelArraylist.getKey());
                startActivity(i);
            }
        });

    }
}
