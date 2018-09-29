package com.wandera.wanderaowner.activity;

import android.app.Dialog;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.activity.accomodations.BusinessProfileAccomodations;
import com.wandera.wanderaowner.activity.giftingcenter.BusinessProfileGiftingCenter;
import com.wandera.wanderaowner.activity.touristHotSpot.BusinessProfileTouristSpots;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.datamodel.BusinessProfileModel;
import com.wandera.wanderaowner.mapModel.UserProfileMapModel;
import com.wandera.wanderaowner.views.BussinessListRecyclerViewAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageBusiness extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView businessItemsRecyclerView;
    BussinessListRecyclerViewAdapter bussinessListRecyclerViewAdapter;
    ArrayList<BusinessProfileModel> businessProfileModelArrayList = new ArrayList<>();
    Context context;
    FirebaseAuth mAuth;
    ConstraintLayout container;
    TextView addBusinessBtn;
    AVLoadingIndicatorView managebusinessLoading;
    TextView signOut;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business);
        managebusinessLoading = (AVLoadingIndicatorView) findViewById(R.id.managebusinessLoading);
        businessItemsRecyclerView = (RecyclerView) findViewById(R.id.businesItemsRV);
        signOut = (TextView) findViewById(R.id.signOut);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        context = ManageBusiness.this;
        container= (ConstraintLayout)findViewById(R.id.container);
        databaseReference.child("businessProfiles").keepSynced(true);
        addBusinessBtn = (TextView) findViewById(R.id.managePermits);



        bussinessListRecyclerViewAdapter = new BussinessListRecyclerViewAdapter(context,businessProfileModelArrayList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        businessItemsRecyclerView.setLayoutManager(layoutManager);
        businessItemsRecyclerView.setAdapter(bussinessListRecyclerViewAdapter);

        BusinessProfileModel businessProfileModelAddBusness = new BusinessProfileModel();

        addBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBusinessTypeDialog();
            }

        });


        databaseReference.child("businessProfiles").addValueEventListener(new ValueEventListener() {
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
                                            businessProfileModel.setBusinessType(businessProfileMapModel.businessType);
                                            businessProfileModel.setRestoProfileImagePath(businessProfileMapModel.restoProfileImagePath);
                                            businessProfileModelArrayList.add(businessProfileModel);
                                        }
                                        managebusinessLoading.setVisibility(View.GONE);
                                        businessItemsRecyclerView.setVisibility(View.VISIBLE);
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
                if (businessProfileModelArraylist.getName().equals("Add Business")){
                    Intent i = new Intent(ManageBusiness.this,OwernerRegistration.class);
                    startActivity(i);

                }else {
                    if (businessProfileModelArraylist.getBusinessType().equals("Restaurants")){
                        Intent i = new Intent(ManageBusiness.this, BusinessProfileRestaurant.class);
                        i.putExtra("key",businessProfileModelArraylist.getKey());
                        startActivity(i);
                    }
                    if (businessProfileModelArraylist.getBusinessType().equals("Accomodations")){
                        Intent i = new Intent(ManageBusiness.this, BusinessProfileAccomodations.class);
                        i.putExtra("key",businessProfileModelArraylist.getKey());
                        startActivity(i);
                    }
                    if (businessProfileModelArraylist.getBusinessType().equals("Gifting Center")){
                        Intent i = new Intent(ManageBusiness.this, BusinessProfileGiftingCenter.class);
                        i.putExtra("key",businessProfileModelArraylist.getKey());
                        startActivity(i);
                    }
                    if (businessProfileModelArraylist.getBusinessType().equals(Utils.bTypeHotSpots)){
                        Intent i = new Intent(ManageBusiness.this, BusinessProfileTouristSpots.class);
                        i.putExtra("key",businessProfileModelArraylist.getKey());
                        startActivity(i);
                    }
                }

            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                signOut();
            }
        });


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        try {
                            saveUserProfile(FirebaseAuth.getInstance().getCurrentUser()
                                    .getUid(),FirebaseAuth.getInstance().getCurrentUser()
                                    .getPhotoUrl().toString());
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("owner", true);
                            FirebaseDatabase.getInstance()
                                    .getReference().child("users")
                                    .child(mAuth.getUid()).updateChildren(result);
                        }catch (NullPointerException e){

                        }

                    }

                },
                5000
        );

    }

    private void selectBusinessTypeDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.select_business_type_dialog);
        final ConstraintLayout selectResto = (ConstraintLayout) dialog.findViewById(R.id.selectResto);
        final ConstraintLayout selectAccomodation = (ConstraintLayout) dialog.findViewById(R.id.selectAccomodation);
        final ConstraintLayout selectPasalubongCenter = (ConstraintLayout) dialog.findViewById(R.id.pasalubongCenter);
        final ConstraintLayout selectTouristSpots = (ConstraintLayout) dialog.findViewById(R.id.touristSpots);
        selectResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OwnerRegistrationNew.class);
                i.putExtra("businessType","Restaurants");
                startActivity(i);
                dialog.dismiss();
            }
        });
        selectAccomodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OwnerRegistrationNew.class);
                i.putExtra("businessType","Accomodations");
                startActivity(i);
                dialog.dismiss();
            }
        });
        selectPasalubongCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OwnerRegistrationNew.class);
                i.putExtra("businessType","Gifting Center");
                startActivity(i);
                dialog.dismiss();
            }
        });
        selectTouristSpots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OwnerRegistrationNew.class);
                i.putExtra("businessType",Utils.bTypeHotSpots);
                startActivity(i);
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void signOut(){
        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getBaseContext(),LoginActivity.class/*To ur activity calss*/);
                        Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }

    private void saveUserProfile(String userId,String userImage){

        String userID= mAuth.getCurrentUser().getUid();
        String userImagePath= mAuth.getCurrentUser().getPhotoUrl().toString();
        UserProfileMapModel userProfileMapModel= new UserProfileMapModel(userID,
                userImagePath,mAuth.getCurrentUser().getDisplayName(),
                mAuth.getCurrentUser().getPhoneNumber(),
                mAuth.getCurrentUser().getEmail());
        Map<String,Object> profileValue = userProfileMapModel.toMap();
        Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(userId,profileValue);

        FirebaseDatabase.getInstance().getReference().child("users").updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }


}
