package com.wandera.wandera.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.wandera.wandera.R;
import com.wandera.wandera.adapter.ViewPagerAdapter;
import com.wandera.wandera.fragements.accomodationsProfile.AccmodationLandingPageFragement;
import com.wandera.wandera.fragements.accomodationsProfile.AccomodationRoomsFragement;
import com.wandera.wandera.fragements.accomodationsProfile.AccomodationsInboxFragement;
import com.wandera.wandera.fragements.touristspots.TouristSpotsActivityFragement;

public class TourisSpotsProfileBotNav extends AppCompatActivity {

    private TextView mTextMessage;
    AccmodationLandingPageFragement accmodationLandingPageFragement;
    TouristSpotsActivityFragement touristSpotsActivityFragement;
    AccomodationsInboxFragement accomodationsInboxFragement;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView navigation;
    ViewPagerAdapter adapter;
    String businessKey;
    Context context;



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
        setContentView(R.layout.activity_tourist_spot_profile_bot_nav);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        businessKey = getIntent().getExtras().getString("businessKey");
        context = TourisSpotsProfileBotNav.this;


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
        accmodationLandingPageFragement = new AccmodationLandingPageFragement();
        touristSpotsActivityFragement = new TouristSpotsActivityFragement();
        accomodationsInboxFragement = new AccomodationsInboxFragement();
        adapter.addFragment(accmodationLandingPageFragement);
        adapter.addFragment(touristSpotsActivityFragement);
        adapter.addFragment(accomodationsInboxFragement);

        viewPager.setAdapter(adapter);
    }

    public String getBusinessKey() {
        return businessKey;
    }


}
