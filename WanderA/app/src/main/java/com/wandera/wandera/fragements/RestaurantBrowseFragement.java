package com.wandera.wandera.fragements;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.BrowseBusinesses;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;
import com.wandera.wandera.views.BusinessBrowseRecyclerViewAdapter;

import java.util.ArrayList;

public class RestaurantBrowseFragement extends Fragment {
    RecyclerView restaurantList;
    BusinessBrowseRecyclerViewAdapter businessBrowseRecyclerViewAdapter;
    ArrayList<BusinessProfileModel> businessProfileModelArrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    BrowseBusinesses act;
    TextView businessType;
    public RestaurantBrowseFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_resturant_browse, container, false);
        restaurantList = (RecyclerView) view.findViewById(R.id.restaurantList);
        businessType = (TextView) view.findViewById(R.id.businessType);
        act = (BrowseBusinesses) getActivity();
        businessType.setText(act.getBusinessType());
        businessBrowseRecyclerViewAdapter = new BusinessBrowseRecyclerViewAdapter(getActivity(),businessProfileModelArrayList);
        restaurantList.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantList.setAdapter(businessBrowseRecyclerViewAdapter);
        businessProfileModelArrayList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Utils.businessProfiel).orderByChild("municipality").equalTo(act.municipalityKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                businessProfileModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot1.getValue(BusinessProfileMapModel.class);
                    BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                    businessProfileModel.setName(businessProfileMapModel.name);
                    businessProfileModel.setKey(businessProfileMapModel.key);
                    businessProfileModel.setRestoProfileImagePath(businessProfileMapModel.restoProfileImagePath);

                    if (businessProfileMapModel.businessType.equals(Utils.bTypeResto)){
                        businessProfileModelArrayList.add(businessProfileModel);
                    }
                }
                businessBrowseRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
