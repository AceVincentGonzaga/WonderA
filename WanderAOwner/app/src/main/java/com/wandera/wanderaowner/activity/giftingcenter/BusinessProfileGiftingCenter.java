package com.wandera.wanderaowner.activity.giftingcenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.wandera.wanderaowner.activity.InboxActivity;
import com.wandera.wanderaowner.activity.OwernerRegistration;
import com.wandera.wanderaowner.activity.OwernerRegistrationUpdate;
import com.wandera.wanderaowner.activity.touristHotSpot.BusinessProfileTouristSpots;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.CategoryMapModel;
import com.wandera.wanderaowner.views.CategoryRecyclerViewAdapter;
import com.wandera.wanderaowner.views.gifting.GiftingCategoryRecyclerViewAdapter;
import com.wandera.wanderaowner.views.gifting.GiftingMenusItemRecyclerViewAdapter;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileGiftingCenter extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    DatabaseReference mDatabase;
    TextView messages,businessProfile;
    ImageView profileIcon;
    Button addCategory;
    Context c;
    ArrayList<CategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    GiftingCategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    RecyclerView categoryList;
    String businessKey;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_restaurant);
        addCategory =(Button) findViewById(R.id.addCategory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle=getIntent().getExtras();
        businessKey = bundle.getString("key");
        c = BusinessProfileGiftingCenter.this;
        categoryList = (RecyclerView) findViewById(R.id.categoryList);
        categoryRecyclerViewAdapter = new GiftingCategoryRecyclerViewAdapter(c,categoryDataModelArrayList);
        categoryList.setLayoutManager(new LinearLayoutManager(c));
        categoryList.setAdapter(categoryRecyclerViewAdapter);
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

        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileGiftingCenter.this,OwernerRegistrationUpdate.class);
                i.putExtra("businessKey", businessKey);
                startActivity(i);
                finish();
            }
        });
        getCategory();
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=  new Intent(BusinessProfileGiftingCenter.this, InboxActivity.class);
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
                       GlideApp.with(BusinessProfileGiftingCenter.this).load(businessProfileMapModel.restoProfileImagePath).into(profileIcon);
                   }catch (IllegalArgumentException e){

                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){
            Intent i = new Intent(BusinessProfileGiftingCenter.this,OwernerRegistration.class);
            startActivity(i);
            finish();
        }
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

    private void AddMenuDialog(){

    }

    private void addCategory(){

            final Dialog dialog = new Dialog(c);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.add_category_dialog);
        final TextInputEditText inputCategory = (TextInputEditText) dialog.findViewById(R.id.inputCategory);
        TextView bntSave = (TextView) dialog.findViewById(R.id.btnSave);

        bntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCategory.getText().toString().trim().length()>0){
                    final String key = mDatabase.child("gifting").child("category").child(businessKey).push().getKey();
                    CategoryMapModel categoryMapModel = new CategoryMapModel(inputCategory.getText().toString(),key,businessKey);
                    Map<String,Object> value = categoryMapModel.toMap();
                    Map<String,Object> childUpdates=new HashMap<>();
                    childUpdates.put(key,value);
                    mDatabase.child("gifting").child("category").child(businessKey).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();

    }

    public void getCategory(){

        mDatabase.child("gifting").child("category").child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CategoryDataModel categoryDataModel = new CategoryDataModel();
                    CategoryMapModel categoryMapModel = dataSnapshot1.getValue(CategoryMapModel.class);
                    categoryDataModel.setKey(categoryMapModel.key);
                    categoryDataModel.setCategory(categoryMapModel.category);
                    categoryDataModel.setBusinessKey(categoryMapModel.businessKey);
                    categoryDataModelArrayList.add(categoryDataModel);
                }
                categoryRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteBusinessCofirmation(){
        final Dialog dialog = new Dialog(BusinessProfileGiftingCenter.this);
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
