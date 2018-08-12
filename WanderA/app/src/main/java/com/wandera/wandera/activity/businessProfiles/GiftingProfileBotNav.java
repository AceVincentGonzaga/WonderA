package com.wandera.wandera.activity.businessProfiles;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.adapter.ViewPagerAdapter;
import com.wandera.wandera.fragements.GiftingProfile.GiftingInboxFragement;
import com.wandera.wandera.fragements.GiftingProfile.GiftingLandingPageFragement;
import com.wandera.wandera.fragements.GiftingProfile.GiftingMenusFragement;
import com.wandera.wandera.mapmodel.RatingCommentMapModel;

import java.util.HashMap;
import java.util.Map;

public class GiftingProfileBotNav extends AppCompatActivity {

    private TextView mTextMessage;
    GiftingLandingPageFragement giftingLandingPageFragement;
    GiftingMenusFragement giftingMenusFragement;
    GiftingInboxFragement giftingInboxFragement;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView navigation;
    ViewPagerAdapter adapter;
    String businessKey;
    Context context;
    Dialog dialog;
    float finalRating;
    DatabaseReference databaseReference;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_restaurant:
                    viewPager.setCurrentItem(0);
                    return  true;
                case R.id.nav_menus:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_inbox:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifting_profile_bot_nav);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        businessKey = getIntent().getExtras().getString("businessKey");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        context = GiftingProfileBotNav.this;


        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    navigation.getMenu().getItem(0).setChecked(false);

                }

                Log.d("page", "onPageSelected: "+position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });




        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        giftingLandingPageFragement = new GiftingLandingPageFragement();
        giftingMenusFragement = new GiftingMenusFragement();
        giftingInboxFragement = new GiftingInboxFragement();
        adapter.addFragment(giftingLandingPageFragement);
        adapter.addFragment(giftingMenusFragement);
        adapter.addFragment(giftingInboxFragement);

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        databaseReference.child(Utils.RATING_DIR).child(businessKey).child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    finish();
                }else {
                    ratingDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String getBusinessKey() {
        return businessKey;
    }

    private void ratingDialog(){

        dialog = new Dialog(context);
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
                finish();
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
                finish();
            }
        });


    }
}
