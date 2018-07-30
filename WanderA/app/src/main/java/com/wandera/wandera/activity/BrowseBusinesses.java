package com.wandera.wandera.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.adapter.ViewPagerAdapter;
import com.wandera.wandera.fragements.AccomodationsBrowseFragement;
import com.wandera.wandera.fragements.GiftingCenterBrowseFragement;
import com.wandera.wandera.fragements.HomeFragement;
import com.wandera.wandera.fragements.ItirenaryFragement;
import com.wandera.wandera.fragements.PhraseBookFragement;
import com.wandera.wandera.fragements.RestaurantBrowseFragement;
import com.wandera.wandera.fragements.TouristHotSpotsBrowseFragement;
import com.wandera.wandera.fragements.TranspoFragement;
import com.wandera.wandera.mapmodel.MunicipalityMapModel;

public class BrowseBusinesses extends AppCompatActivity {


    private ImageView bckBtn;
    private TextView municipalityName;
    String munKey;
    DatabaseReference databaseReference;
    RestaurantBrowseFragement restaurantBrowseFragement;
    AccomodationsBrowseFragement accomodationsFrag;
    GiftingCenterBrowseFragement giftCenterFrag;
    TouristHotSpotsBrowseFragement hotspotsFrag;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView navigation;
    String businessType = "Restaurants";

    ViewPagerAdapter adapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_resto:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_accomodation:
                    viewPager.setCurrentItem(1);
                 
                    return true;
                case R.id.nav_gifting_center:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.nav_hotspots:
                    viewPager.setCurrentItem(3);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_businesses);
        bckBtn = (ImageView) findViewById(R.id.bckBtn);
        munKey = getIntent().getExtras().getString("municipalityKey");
        municipalityName = (TextView) findViewById(R.id.municipalityName);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Utils.municipality).child(munKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MunicipalityMapModel municipalityMapModel = dataSnapshot.getValue(MunicipalityMapModel.class);
                municipalityName.setText(municipalityMapModel.municipality);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    public String municipalityKey(){
        return munKey;
    }

    public String getBusinessType() {
        return businessType;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        restaurantBrowseFragement =new RestaurantBrowseFragement();
        accomodationsFrag = new AccomodationsBrowseFragement();
        giftCenterFrag = new GiftingCenterBrowseFragement();
        hotspotsFrag = new TouristHotSpotsBrowseFragement();
        adapter.addFragment(restaurantBrowseFragement);
        adapter.addFragment(accomodationsFrag);
        adapter.addFragment(giftCenterFrag);
        adapter.addFragment(hotspotsFrag);

        viewPager.setAdapter(adapter);
    }

}
