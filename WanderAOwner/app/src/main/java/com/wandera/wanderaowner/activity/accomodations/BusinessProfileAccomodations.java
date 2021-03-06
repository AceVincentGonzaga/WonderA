package com.wandera.wanderaowner.activity.accomodations;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.StorageReference;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.activity.InboxActivity;
import com.wandera.wanderaowner.activity.OwernerRegistration;
import com.wandera.wanderaowner.activity.OwernerRegistrationUpdate;
import com.wandera.wanderaowner.activity.touristHotSpot.BusinessProfileTouristSpots;
import com.wandera.wanderaowner.datamodel.GalleryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.RoomDataModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.RoomMapModel;
import com.wandera.wanderaowner.views.BussinessListRecyclerViewAdapter;
import com.wandera.wanderaowner.views.GalleryRecyclerViewAdapter;
import com.wandera.wanderaowner.views.RoomsRecyclerViewAdapter;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileAccomodations extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    DatabaseReference mDatabase;
    TextView messages,businessProfile;
    ImageView profileIcon;
    Button addCategory;
    Context c;
    RoomsRecyclerViewAdapter roomsRecyclerViewAdapter;
    ArrayList<RoomDataModel> roomDataModelArrayList = new ArrayList<>();
    RecyclerView roomList;
    String businessKey;
    ArrayList<GalleryDataModel> galleryDataModelArrayList = new ArrayList<>();
    GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    RecyclerView galleryList;
    private static final int READ_REQUEST_CODE = 42;
    boolean access_storage;
    private static final int storagepermision_access_code = 548;
    StorageReference mStorageRef;
    Uri bannerUri;
    boolean imageSet;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_accomodations);
        addCategory =(Button) findViewById(R.id.addCategory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle=getIntent().getExtras();
        businessKey = bundle.getString("key");
        c = BusinessProfileAccomodations.this;
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

        roomsRecyclerViewAdapter = new RoomsRecyclerViewAdapter(BusinessProfileAccomodations.this,roomDataModelArrayList);
        roomList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        roomList.setAdapter(roomsRecyclerViewAdapter);

        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileAccomodations.this,OwernerRegistrationUpdate.class);
                i.putExtra("businessKey", businessKey);
                startActivity(i);
                finish();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(BusinessProfileAccomodations.this, AddRoom.class);
                i.putExtra("businessKey",businessKey);
                startActivity(i);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=  new Intent(BusinessProfileAccomodations.this, InboxActivity.class);
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
                       GlideApp.with(BusinessProfileAccomodations.this).load(businessProfileMapModel.restoProfileImagePath).into(profileIcon);
                   }catch (IllegalArgumentException e){

                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){
            Intent i = new Intent(BusinessProfileAccomodations.this,OwernerRegistration.class);
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
        FirebaseDatabase.getInstance().getReference().child("accomodations").child("rooms")
                .child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RoomMapModel roomMapModel = dataSnapshot1.getValue(RoomMapModel.class);
                    RoomDataModel roomDataModel = new RoomDataModel();
                    roomDataModel.setRoomName(roomMapModel.roomName);
                    roomDataModel.setBusinessKey(roomMapModel.businessKey);
                    roomDataModel.setRoomId(roomMapModel.roomId);
                    roomDataModel.setRoomImage(roomMapModel.roomImage);
                    roomDataModel.setRoomPrice(roomMapModel.roomPrice);
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
        final Dialog dialog = new Dialog(BusinessProfileAccomodations.this);
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
