package com.wandera.wanderaowner;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wandera.wanderaowner.mapModel.GetLocationActivity;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class OwernerRegistration extends AppCompatActivity implements OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {
        List<String> categories = new ArrayList<String>();
        Spinner spinner;
        TextInputEditText inpt_name,inpt_address,input_contact, inpt_email;
        TextView saveProfile,selectBType;
        Context context;
        FirebaseUser mAuth;
        private LocationGooglePlayServicesProvider provider;
        DatabaseReference databaseReference;
        String businessType;
        private static final int LOCATION_PERMISSION_ID = 1001;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            context = OwernerRegistration.this;
            setContentView(R.layout.activity_owerner_registration);
            inpt_name = (TextInputEditText) findViewById(R.id.input_name);
            inpt_address = (TextInputEditText) findViewById(R.id.input_address);
            input_contact = (TextInputEditText) findViewById(R.id.input_contact);
            inpt_email = (TextInputEditText) findViewById(R.id.inpt_email);
            saveProfile = (TextView) findViewById(R.id.saveProfile);
            selectBType = (TextView) findViewById(R.id.selectBType);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            categories.add("Restaurant");
            categories.add("Accomodation");
            categories.add("Pasalubong Center");
            categories.add("Education");
            mAuth = FirebaseAuth.getInstance().getCurrentUser();
            saveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validate()){
                        Utils.callToast(context,"incomplete");

                    }else {
                        Utils.callToast(context,"Success");
                        saveProfile(inpt_name.getText().toString(),
                                    inpt_address.getText().toString(),
                                    input_contact.getText().toString(),
                                    inpt_email.getText().toString()
                                );
                    }
                }
            });
            inpt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(OwernerRegistration.this, GetLocationActivity.class);
                    startActivity(i);
                }
            });
            inpt_email.setText(mAuth.getEmail());
            input_contact.setText(mAuth.getPhoneNumber());

            showLast();
            if (ContextCompat.checkSelfPermission(OwernerRegistration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OwernerRegistration.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                return;
            }
            startLocation();
            selectBType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBusinessTypeDialog();
                }
            });
        }

        private void selectBusinessTypeDialog(){
            final Dialog dialog = new Dialog(OwernerRegistration.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.select_business_type_dialog);
            final ConstraintLayout selectResto = (ConstraintLayout) dialog.findViewById(R.id.selectResto);
            final ConstraintLayout selectAccomodation = (ConstraintLayout) dialog.findViewById(R.id.selectAccomodation);
            final ConstraintLayout selectPasalubongCenter = (ConstraintLayout) dialog.findViewById(R.id.pasalubongCenter);
            final ConstraintLayout selectTouristSpots = (ConstraintLayout) dialog.findViewById(R.id.touristSpots);
            selectResto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBType("Restaurants");
                    dialog.dismiss();
                }
            });
            selectAccomodation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBType("Accomodations");
                    dialog.dismiss();
                }
            });
            selectPasalubongCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBType("Pasalubong Center");
                    dialog.dismiss();
                }
            });
            selectTouristSpots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBType("Tourist Spots");
                    dialog.dismiss();
                }
            });
            Window window = dialog.getWindow();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.show();
        }
        private boolean validate(){
            boolean val = true;
            if (inpt_name.getText().toString().trim().length()==0){
                val = false;
            }
            if (inpt_address.getText().toString().trim().length()==0){
                val = false;
            }
            if (input_contact.getText().toString().trim().length()==0){
                val = false;
            }
            if(inpt_email.getText().toString().trim().length()==0){
                val = false;
            }
            if (businessType==null){
                val = false;
            }
            return val;
        }
        private void selectBType(String type){
            selectBType.setText(type);
            businessType = type;
        }

        private void saveProfile(String name,String address,String contact,String emaill){
            String uid = mAuth.getUid();
            String key = databaseReference.push().getKey();
            BusinessProfileMapModel businessProfileMapModel = new BusinessProfileMapModel(uid,name,address,contact,emaill,businessType,"null for now",key);
            Map<String,Object> profileValue = businessProfileMapModel.toMap();
            Map<String,Object> childupdates = new HashMap<>();
            childupdates.put(key,profileValue);

            databaseReference.child("businessProfiles").updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                Intent i = new Intent(context,ManageBusiness.class);
                startActivity(i);
                finish();
                }
            });
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            }
        }

        private void showLast() {
            Location lastLocation = SmartLocation.with(this).location().getLastLocation();
            if (lastLocation != null) {
                inpt_address.setText(
                        String.format("",
                                lastLocation.getLatitude(),
                                lastLocation.getLongitude())
                );
            }

            DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
            if (detectedActivity != null) {
                inpt_address.setText(
                        String.format("",
                                getNameFromType(detectedActivity),
                                detectedActivity.getConfidence())
                );
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (provider != null) {
                provider.onActivityResult(requestCode, resultCode, data);
            }
        }

        private void startLocation() {

            provider = new LocationGooglePlayServicesProvider();
            provider.setCheckLocationSettings(true);

            SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

            smartLocation.location(provider).start(this);
            smartLocation.activity().start(this);

            // Create some geofences
            GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
            smartLocation.geofencing().add(mestalla).start(this);
        }

        private void stopLocation() {
            SmartLocation.with(this).location().stop();
            inpt_address.setText("Location stopped!");

            SmartLocation.with(this).activity().stop();
    //        activityText.setText("Activity Recognition stopped!");

            SmartLocation.with(this).geofencing().stop();
          //  geofenceText.setText("Geofencing stopped!");
        }

        private void showLocation(Location location) {
            if (location != null) {
                final String text = String.format("",
                        location.getLatitude(),
                        location.getLongitude());
                inpt_address.setText(text);

                // We are going to get the address for the current position
                SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location original, List<Address> results) {
                        if (results.size() > 0) {
                            Address result = results.get(0);
                            StringBuilder builder = new StringBuilder(text);
                            builder.append("");
                            List<String> addressElements = new ArrayList<>();
                            for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                                addressElements.add(result.getAddressLine(i));
                            }
                            builder.append(TextUtils.join(", ", addressElements));
                            inpt_address.setText(builder.toString());
                        }
                    }
                });
            } else {
                inpt_address.setText("Null location");
            }
        }

        private void showActivity(DetectedActivity detectedActivity) {
            if (detectedActivity != null) {
    //            activityText.setText(
    //                    String.format("Activity %s with %d%% confidence",
    //                            getNameFromType(detectedActivity),
    //                            detectedActivity.getConfidence())
    //            );
            } else {
    //            activityText.setText("Null activity");
            }
        }

        private void showGeofence(Geofence geofence, int transitionType) {
    //        if (geofence != null) {
    //            geofenceText.setText("Transition " + getTransitionNameFromType(transitionType) + " for Geofence with id = " + geofence.getRequestId());
    //        } else {
    //            geofenceText.setText("Null geofence");
    //        }
        }

        @Override
        public void onLocationUpdated(Location location) {
            showLocation(location);
        }

        @Override
        public void onActivityUpdated(DetectedActivity detectedActivity) {
            showActivity(detectedActivity);
        }

        @Override
        public void onGeofenceTransition(TransitionGeofence geofence) {
            showGeofence(geofence.getGeofenceModel().toGeofence(), geofence.getTransitionType());
        }

        private String getNameFromType(DetectedActivity activityType) {
            switch (activityType.getType()) {
                case DetectedActivity.IN_VEHICLE:
                    return "in_vehicle";
                case DetectedActivity.ON_BICYCLE:
                    return "on_bicycle";
                case DetectedActivity.ON_FOOT:
                    return "on_foot";
                case DetectedActivity.STILL:
                    return "still";
                case DetectedActivity.TILTING:
                    return "tilting";
                default:
                    return "unknown";
            }
        }

        private String getTransitionNameFromType(int transitionType) {
            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    return "enter";
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    return "exit";
                default:
                    return "dwell";
            }
        }

}
