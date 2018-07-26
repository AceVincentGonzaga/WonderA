package com.wandera.wandera;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Homepage extends AppCompatActivity {

    private TextView mTextMessage;
    private Button municipalitybutton;
    private Button allbutton;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_transpo:
                    Intent transpo= new Intent(Homepage.this, Transpo.class);
                    startActivity(transpo);
                    finish();
                    return true;
                case R.id.navigation_phrasebook:
                    Intent phrasebook= new Intent(Homepage.this, Phrasebook.class);
                    startActivity(phrasebook);
                    finish();
                    return true;
                case R.id.navigation_itinerary:
                    Intent itinerary= new Intent(Homepage.this, Itinerary.class);
                    startActivity(itinerary);
                    finish();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        municipalitybutton= (Button) findViewById(R.id.municipality_button);
        municipalitybutton.setSelected(true);
        municipalitybutton.setTextColor(getApplication().getResources().getColor(R.color.background));

        /*municipalitybutton.setTextColor(Color.parseColor("#2b2b2b"));*/
        }
    }






