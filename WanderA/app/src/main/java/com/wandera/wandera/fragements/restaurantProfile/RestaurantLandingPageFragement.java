package com.wandera.wandera.fragements.restaurantProfile;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.wandera.wandera.activity.businessProfiles.RestaurantProfileBotNav;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.datamodel.GalleryDataModel;
import com.wandera.wandera.datamodel.RatingCommentDataModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;
import com.wandera.wandera.mapmodel.GalleryMapModel;
import com.wandera.wandera.mapmodel.MunicipalityMapModel;
import com.wandera.wandera.mapmodel.RatingCommentMapModel;
import com.wandera.wandera.mapmodel.RestaurantLocationMapModel;
import com.wandera.wandera.mapmodel.SignalWifiMapModel;
import com.wandera.wandera.views.GalleryRecyclerViewAdapter;
import com.wandera.wandera.views.ratingAndComments.RatingsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantLandingPageFragement extends Fragment implements OnMapReadyCallback {
    RestaurantProfileBotNav act;
    AppBarLayout appbar;
    CollapsingToolbarLayout colapsToolbar;
    String businessKey;
    DatabaseReference databaseReference;
    ImageView app_bar_image;
    TextView textTitle,contactNumber,location;
    RecyclerView ratingAndCommentList;
    RatingsRecyclerViewAdapter ratingsRecyclerViewAdapter;
    ArrayList<RatingCommentDataModel> ratingCommentDataModelArrayList = new ArrayList<>();
    RatingBar ratingBar;
    TextView rating;
    GoogleMap mMap;
    Marker marker;
    RecyclerView galleryList;
    ArrayList<GalleryDataModel> galleryDataModelArrayList = new ArrayList<>();
    GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    ImageView wifiImage;
    TextView network,howToGetHere;
    ImageView networkImage;
    TextView wifi;
    public RestaurantLandingPageFragement(){

    }
    float finalRating;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_business_prof_landing_page, container, false);
        act = (RestaurantProfileBotNav) getActivity();
        businessKey = act.getBusinessKey();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                databaseReference.child("businessLocations").child(businessKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            RestaurantLocationMapModel restaurantLocationMapModel = dataSnapshot.getValue(RestaurantLocationMapModel.class);
                            LatLng latLng = new LatLng(restaurantLocationMapModel.locationLatitude,restaurantLocationMapModel.locationLongitude);
                            if (mMap != null) {
                                marker = mMap.addMarker(new MarkerOptions()
                                        .position(latLng).title("test")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                        .draggable(false).visible(true));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(restaurantLocationMapModel.locationLatitude,
                                                restaurantLocationMapModel.locationLongitude), 15));
                            }

                        }catch (NullPointerException e){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        wifi = (TextView) view.findViewById(R.id.wifi);
        wifiImage = (ImageView) view.findViewById(R.id.wifiImage);
        networkImage = (ImageView) view.findViewById(R.id.networkImage);
        network = (TextView) view.findViewById(R.id.network);
        galleryList = (RecyclerView) view.findViewById(R.id.galleryList);
        rating = (TextView) view.findViewById(R.id.rating);
        contactNumber = (TextView) view.findViewById(R.id.contactNumber);
        location = (TextView) view.findViewById(R.id.location);
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        colapsToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.colapsToolbar);
        textTitle = (TextView) view.findViewById(R.id.textTitle);
        app_bar_image = (ImageView) view.findViewById(R.id.app_bar_image);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        howToGetHere = (TextView) view.findViewById(R.id.hotToGetHere);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSignalWifiStats();
        databaseReference.child("businessProfiles").child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                final BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                textTitle.setText(businessProfileMapModel.name);
                contactNumber.setText(businessProfileMapModel.contact);
                try {
                    howToGetHere.setText(businessProfileMapModel.howToGetThere);
                }catch (NullPointerException e){

                }
                databaseReference.child("municipality").child(businessProfileMapModel.municipality).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MunicipalityMapModel municipalityMapModel = dataSnapshot.getValue(MunicipalityMapModel.class);
                        location.setText(businessProfileMapModel.barangay+", "+municipalityMapModel.municipality);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                GlideApp.with(getActivity()).load(businessProfileMapModel.restoProfileImagePath).centerCrop().into(app_bar_image);
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
        galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(getActivity(),galleryDataModelArrayList,businessKey);
        galleryList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        galleryList.setAdapter(galleryRecyclerViewAdapter);
        getGallery();
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(galleryList);
        return view;
    }
    private void getGallery(){
        FirebaseDatabase.getInstance().getReference().child(Utils.GAL_DIR).child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    GalleryMapModel galleryMapModel = dataSnapshot1.getValue(GalleryMapModel.class);
                    GalleryDataModel galleryDataModel = new GalleryDataModel();
                    galleryDataModel.setImagePath(galleryMapModel.imagePath);
                    galleryDataModelArrayList.add(galleryDataModel);
                }
                galleryRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

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

    private void getSignalWifiStats(){
        databaseReference.child(Utils.WIFISIGNAL_DIR).child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    SignalWifiMapModel signalWifiMapModel = dataSnapshot.getValue(SignalWifiMapModel.class);
                    setSignalColor(signalWifiMapModel.signal);
                    if (signalWifiMapModel.wifi){
                        wifiImage.setColorFilter(getResources().getColor(R.color.Strong));
                        wifi.setText("Available");

                    }else {

                        wifi.setText("Unavailable");
                        wifiImage.setColorFilter(getResources().getColor(R.color.noSignal));

                    }
                }catch (NullPointerException e){
                    setSignalColor("No Signal");
                    wifi.setText("Unavailable");
                    wifiImage.setColorFilter(getResources().getColor(R.color.noSignal));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setSignalColor(String signalLabel){
        network.setText(signalLabel);
        if (signalLabel.equals("No Signal")) {

            networkImage.setColorFilter(getResources().getColor(R.color.noSignal));
        }
        else if (signalLabel.equals("poor")){
            networkImage.setColorFilter(getResources().getColor(R.color.poor));


        }
        else if (signalLabel.equals("fair")){
            networkImage.setColorFilter(getResources().getColor(R.color.fair));


        }

        else if (signalLabel.equals("Strong")){
            networkImage.setColorFilter(getResources().getColor(R.color.Strong));


        }
    }

}
