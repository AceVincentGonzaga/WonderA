package com.wandera.wandera.fragements.restaurantProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.businessProfiles.RestaurantProfileBotNav;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.datamodel.RatingCommentDataModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;
import com.wandera.wandera.mapmodel.RatingCommentMapModel;
import com.wandera.wandera.views.ratingAndComments.RatingsRecyclerViewAdapter;

import java.util.ArrayList;

public class RestaurantLandingPageFragement extends Fragment {
    RestaurantProfileBotNav act;
    AppBarLayout appbar;
    CollapsingToolbarLayout colapsToolbar;
    String businessKey;
    DatabaseReference databaseReference;
    ImageView app_bar_image;
    TextView textTitle;
    RecyclerView ratingAndCommentList;
    RatingsRecyclerViewAdapter ratingsRecyclerViewAdapter;
    ArrayList<RatingCommentDataModel> ratingCommentDataModelArrayList = new ArrayList<>();
    RatingBar ratingBar;
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
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
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
        ratingAndCommentList = (RecyclerView) view.findViewById(R.id.ratingAndCommentList);
        ratingsRecyclerViewAdapter = new RatingsRecyclerViewAdapter(getActivity(),ratingCommentDataModelArrayList);
        ratingAndCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ratingAndCommentList.setAdapter(ratingsRecyclerViewAdapter);

        databaseReference.child(Utils.RATING_DIR).child(act.getBusinessKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratingCommentDataModelArrayList.clear();
                float aveRating = 0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RatingCommentDataModel commentDataModel = new RatingCommentDataModel();
                    RatingCommentMapModel ratingCommentMapModel = dataSnapshot1.getValue(RatingCommentMapModel.class);
                    commentDataModel.setComment(ratingCommentMapModel.comment);
                    commentDataModel.setAccountId(ratingCommentMapModel.accountId);
                    commentDataModel.setRating(ratingCommentMapModel.rating);
                    commentDataModel.setBusinessId(ratingCommentMapModel.businessId);
                    ratingCommentDataModelArrayList.add(commentDataModel);
                    aveRating+=ratingCommentMapModel.rating;
                }
                ratingsRecyclerViewAdapter.notifyDataSetChanged();

                ratingBar.setRating(aveRating/ratingCommentDataModelArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
