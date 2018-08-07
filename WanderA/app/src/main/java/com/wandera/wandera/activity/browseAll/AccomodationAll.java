package com.wandera.wandera.activity.browseAll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;
import com.wandera.wandera.views.BusinessBrowseRecyclerViewAdapter;

import java.util.ArrayList;

public class AccomodationAll extends AppCompatActivity {
    BusinessBrowseRecyclerViewAdapter businessBrowseRecyclerViewAdapter;
    ArrayList<BusinessProfileModel> businessProfileModelArrayList = new ArrayList<>();
    RecyclerView restaurantlist;
    Context context;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation_all);
        context = AccomodationAll.this;
        restaurantlist = (RecyclerView) findViewById(R.id.restaurantlist);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        businessBrowseRecyclerViewAdapter = new BusinessBrowseRecyclerViewAdapter(context,businessProfileModelArrayList);
        restaurantlist.setLayoutManager(new LinearLayoutManager(context));
        restaurantlist.setAdapter(businessBrowseRecyclerViewAdapter);

        databaseReference.child("businessProfiles").orderByChild("businessType").equalTo("Accomodations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot1.getValue(BusinessProfileMapModel.class);
                    businessProfileModel.setName(businessProfileMapModel.name);
                    businessProfileModel.setBusinessType(businessProfileMapModel.businessType);
                    businessProfileModel.setKey(businessProfileMapModel.key);
                    businessProfileModel.setRestoProfileImagePath(businessProfileMapModel.restoProfileImagePath);
                    businessProfileModelArrayList.add(businessProfileModel);
                }
                businessBrowseRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
