package com.wandera.wandera.fragements.restaurantProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.activity.businessProfiles.RestaurantProfileBotNav;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;

public class RestaurantLandingPageFragement extends Fragment {
    RestaurantProfileBotNav act;
    AppBarLayout appbar;
    CollapsingToolbarLayout colapsToolbar;
    String businessKey;
    DatabaseReference databaseReference;
    ImageView app_bar_image;
    TextView textTitle;
    public RestaurantLandingPageFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_business_prof_landing_page, container, false);
        act = (RestaurantProfileBotNav) getActivity();
        businessKey = act.getBusinessKey();
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        colapsToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.colapsToolbar);
        textTitle = (TextView) view.findViewById(R.id.textTitle);
        app_bar_image = (ImageView) view.findViewById(R.id.app_bar_image);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("businessProfiles").child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                textTitle.setText(businessProfileMapModel.name);
                GlideApp.with(getActivity()).load(businessProfileMapModel.restoProfileImagePath).centerCrop().into(app_bar_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
