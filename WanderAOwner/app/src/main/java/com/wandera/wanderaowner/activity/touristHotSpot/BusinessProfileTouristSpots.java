package com.wandera.wanderaowner.activity.touristHotSpot;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.activity.BusinessProfile;
import com.wandera.wanderaowner.activity.BusinessProfileRestaurant;
import com.wandera.wanderaowner.activity.InboxActivity;
import com.wandera.wanderaowner.activity.OwernerRegistration;
import com.wandera.wanderaowner.activity.OwernerRegistrationUpdate;
import com.wandera.wanderaowner.activity.accomodations.AddRoom;
import com.wandera.wanderaowner.datamodel.RoomDataModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.RoomMapModel;
import com.wandera.wanderaowner.views.ActivityRecyclerViewAdapter;
import com.wandera.wanderaowner.views.RoomsRecyclerViewAdapter;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileTouristSpots extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    DatabaseReference mDatabase;
    TextView messages,businessProfile;
    ImageView profileIcon;
    Button addCategory;
    Context c;
    ActivityRecyclerViewAdapter roomsRecyclerViewAdapter;
    ArrayList<RoomDataModel> roomDataModelArrayList = new ArrayList<>();
    RecyclerView roomList;
    String businessKey;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_tourist);
        addCategory =(Button) findViewById(R.id.addCategory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle=getIntent().getExtras();
        businessKey = bundle.getString("key");
        c = BusinessProfileTouristSpots.this;
        roomList = (RecyclerView) findViewById(R.id.roomList);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withToolbarMenuToggle(toolbar)
                .withDragDistance(200)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withRootViewScale(1f)
                .withRootViewYTranslation(4)
                .withMenuLayout(R.layout.slide_root_nav_business_profile_restaurant)
                .withSavedState(savedInstanceState)
                .withContentClickableWhenMenuOpened(true)
                .inject();
        messages = (TextView)findViewById(R.id.messages);
        businessProfile = (TextView) findViewById(R.id.manageProfile);
        profileIcon = (CircleImageView) findViewById(R.id.profileIcon);

        roomsRecyclerViewAdapter = new ActivityRecyclerViewAdapter(BusinessProfileTouristSpots.this,roomDataModelArrayList);
        roomList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        roomList.setAdapter(roomsRecyclerViewAdapter);

        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileTouristSpots.this,OwernerRegistrationUpdate.class);
                i.putExtra("businessKey", businessKey);
                startActivity(i);
                finish();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(BusinessProfileTouristSpots.this, AddActivity.class);
                i.putExtra("businessKey",businessKey);
                startActivity(i);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=  new Intent(BusinessProfileTouristSpots.this, InboxActivity.class);
                i.putExtra("key", businessKey);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.deleteBusiness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBusinessCofirmation();
            }
        });


        try {
            mDatabase.child(Utils.businessProfiles).child(businessKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                    toolbar.setTitle(businessProfileMapModel.name);
                   try {
                       GlideApp.with(BusinessProfileTouristSpots.this).load(businessProfileMapModel.restoProfileImagePath).into(profileIcon);
                   }catch (IllegalArgumentException e){

                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){
            Intent i = new Intent(BusinessProfileTouristSpots.this,OwernerRegistration.class);
            startActivity(i);
            finish();
        }

        getRooms();
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(roomList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buttom_sheet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }

    private void getRooms(){
        FirebaseDatabase.getInstance().getReference().child("touristspots").child("activity").child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RoomMapModel roomMapModel = dataSnapshot1.getValue(RoomMapModel.class);
                    RoomDataModel roomDataModel = new RoomDataModel();
                    roomDataModel.setRoomName(roomMapModel.roomName);
                    roomDataModel.setRoomImage(roomMapModel.roomImage);
                    roomDataModel.setRoomPrice(roomMapModel.roomPrice);
                    roomDataModel.setBusinessKey(roomMapModel.businessKey);
                    roomDataModel.setRoomId(roomMapModel.roomId);
                    roomDataModel.setRoomDescription(roomMapModel.roomDescription);
                    roomDataModelArrayList.add(roomDataModel);
                }
                roomsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void deleteBusinessCofirmation(){
        final Dialog dialog = new Dialog(BusinessProfileTouristSpots.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dilog_delete_business);
        dialog.findViewById(R.id.deleteCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.deleteConfirmed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBusiness(dialog);
            }
        });



        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void deleteBusiness(final Dialog dialog){
        FirebaseDatabase.getInstance().getReference()
                .child("businessProfiles").child(businessKey)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                finish();
            }
        });
    }




}
