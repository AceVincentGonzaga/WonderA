package com.wandera.wandera.fragements.touristspots;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.businessProfiles.AccomodationProfileBotNav;
import com.wandera.wandera.activity.businessProfiles.TourisSpotsProfileBotNav;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.datamodel.RatingCommentDataModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;
import com.wandera.wandera.mapmodel.RatingCommentMapModel;
import com.wandera.wandera.views.ratingAndComments.RatingsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TouristSpotsLandingPageFragement extends Fragment {

    TourisSpotsProfileBotNav act;
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
    TextView rating;
    Dialog dialog;
    float finalRating;
    public TouristSpotsLandingPageFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_accomodation_prof_landing_page, container, false);
        act = (TourisSpotsProfileBotNav) getActivity();
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
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
        rating = (TextView) view.findViewById(R.id.rating);
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
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog();
            }
        });
        return view;
    }

    private void ratingDialog(){

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.rating_and_input_comment_dialogue);//layout resource
        final TextInputEditText comment = (TextInputEditText) dialog.findViewById(R.id.inputComment);
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        Button submitBtn = (Button) dialog.findViewById(R.id.submitBtn);
        TextView notNow = (TextView) dialog.findViewById(R.id.notNow);
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                finalRating = rating;
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment.getText().toString().trim().equals("") && finalRating!=0){
                    submitRatingComment(comment.getText().toString(),finalRating);
                }
            }
        });




        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    private void submitRatingComment(String comment,float rating){



        String accountId = FirebaseAuth.getInstance().getUid();
        RatingCommentMapModel ratingCommentMapModel = new RatingCommentMapModel(accountId,
                comment,rating,businessKey);

        Map<String,Object> commentvalue = ratingCommentMapModel.toMap();
        Map<String,Object> childUpdate = new HashMap<>();
        childUpdate.put(accountId,commentvalue);

        databaseReference.child(Utils.RATING_DIR).child(businessKey).updateChildren(childUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
            }
        });


    }
}
