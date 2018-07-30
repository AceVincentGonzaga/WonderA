package com.wandera.wandera;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wandera.wandera.activity.InboxActivity;
import com.wandera.wandera.adapter.ViewPagerAdapter;
import com.wandera.wandera.fragements.HomeFragement;
import com.wandera.wandera.fragements.ItirenaryFragement;
import com.wandera.wandera.fragements.PhraseBookFragement;
import com.wandera.wandera.fragements.TranspoFragement;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeSlider extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText selectMunicipality;
    private TextView mTextMessage,userName,userEmail;
    private Button municipalitybutton;
    HomeFragement homeFragement;
    ItirenaryFragement itirenaryFragement;
    PhraseBookFragement phraseBookFragement;
    TranspoFragement transpoFragement;
    MenuItem prevMenuItem;
    private ViewPager viewPager;
    CircleImageView circleImageView;
    private Button allbutton;
    FirebaseAuth firebaseAuth;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_transpo:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_phrasebook:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_itinerary:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_slider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        selectMunicipality = (EditText)findViewById(R.id.selectMunicipality);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.inbox) {

                    Intent i = new Intent(HomeSlider.this, InboxActivity.class);
                    startActivity(i);
                    return true;

                } else if (id == R.id.sign_out) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        View hView = navigationView.inflateHeaderView(R.layout.nav_header_home_slider);
        TextView userName = (TextView) hView.findViewById(R.id.userName);
        TextView userEmail = (TextView) hView.findViewById(R.id.userEmail);
        CircleImageView userImage = (CircleImageView) hView.findViewById(R.id.userImage);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        GlideApp.with(HomeSlider.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImage);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home_slider_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.inbox) {

            Intent i = new Intent(HomeSlider.this, InboxActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.sign_out) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragement=new HomeFragement();
        itirenaryFragement=new ItirenaryFragement();
        phraseBookFragement=new PhraseBookFragement();
        transpoFragement = new TranspoFragement();
        adapter.addFragment(homeFragement);
        adapter.addFragment(transpoFragement);
        adapter.addFragment(phraseBookFragement);
        adapter.addFragment(itirenaryFragement);


        viewPager.setAdapter(adapter);
    }
}
