package com.wandera.wandera;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Phrasebook extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home= new Intent(Phrasebook.this, Transpo.class);
                    startActivity(home);
                    finish();
                    return true;
                case R.id.navigation_transpo:
                    Intent transpo= new Intent(Phrasebook.this, Transpo.class);
                    startActivity(transpo);
                    finish();
                    return true;
                case R.id.navigation_phrasebook:
                    Intent phrasebook= new Intent(Phrasebook.this, Phrasebook.class);
                    startActivity(phrasebook);
                    return true;
                case R.id.navigation_itinerary:
                    Intent itinerary= new Intent(Phrasebook.this, Itinerary.class);
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
        setContentView(R.layout.activity_phrasebook);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_phrasebook);
    }

}
