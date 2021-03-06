package com.wandera.wanderaowner.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.datamodel.BarangayDataModel;
import com.wandera.wanderaowner.datamodel.MunicipalityDataModel;
import com.wandera.wanderaowner.mapModel.BarangayMapModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.MunicipalityMapModel;
import com.wandera.wanderaowner.mapModel.RestaurantLocationMapModel;
import com.wandera.wanderaowner.mapModel.SignalWifiMapModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class OwnerRegistrationNew extends AppCompatActivity {
        List<String> categories = new ArrayList<String>();
        Spinner spinner;
        TextInputEditText inpt_name,input_contact, inpt_email;
        TextView saveProfile,selectBType;
        Context context;
        FirebaseAuth mAuth;
        private LocationGooglePlayServicesProvider provider;
        DatabaseReference databaseReference;
        String businessType;
        private static final int LOCATION_PERMISSION_ID = 1001;
        private static final int RC_SIGN_IN = 297;
        private static final int storagepermision_access_code = 548;
        boolean access_storage;
        private static final int READ_REQUEST_CODE = 42;
        CircleImageView businessProfile;
        boolean imageSet = false;
        Uri bannerUri;
        ConstraintLayout loadingContainer;
        StorageReference mStorageRef;
        TextView selectMunicipality;
        String municipality;
        String key;
        TextView setLocation;
        TextView selectBarangay;
        ArrayList<BarangayDataModel> barangayDataModelArrayList = new ArrayList<>();
        String municipalityId;
        boolean businessSpecificLocation = false;
        TextView textWifi;
        ImageView wifiIcon;
        TextView signalStreng;
        boolean wifiAvail = true;
        ImageView signalImage;
        String signalLabel = "No Signal";
        EditText howToGetThere;
        Button managePermits;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            context = OwnerRegistrationNew.this;
            setContentView(R.layout.activity_owerner_registration);
            inpt_name = (TextInputEditText) findViewById(R.id.input_name);
            loadingContainer  = (ConstraintLayout) findViewById(R.id.loadingContainer);
            selectBarangay = (TextView) findViewById(R.id.selectBarangay);
            textWifi = (TextView) findViewById(R.id.textWifi);
            wifiIcon = (ImageView) findViewById(R.id.wifiIcon);
            signalStreng = (TextView) findViewById(R.id.signalStreng);
            signalImage = (ImageView) findViewById(R.id.signalImage);
            input_contact = (TextInputEditText) findViewById(R.id.input_contact);
            inpt_email = (TextInputEditText) findViewById(R.id.inpt_email);
            saveProfile = (TextView) findViewById(R.id.saveProfile);
            selectBType = (TextView) findViewById(R.id.selectBType);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            businessProfile = (CircleImageView) findViewById(R.id.businessProfile);
            selectMunicipality = (TextView) findViewById(R.id.selectMunicipality);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            setLocation = (TextView) findViewById(R.id.setLocation);
            howToGetThere = (EditText) findViewById(R.id.howToGetThere);
            managePermits = (Button) findViewById(R.id.managePermits);
            selectBType(getIntent().getExtras().getString("businessType"));
            categories.add("Restaurant");
            categories.add("Accomodation");
            categories.add("Pasalubong Center");
            categories.add("Education");
            mAuth = FirebaseAuth.getInstance();
            key = databaseReference.push().getKey();
            saveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validate()){

                    }else {
                        Utils.callToast(context,"Success");
                        uploadItemBanner(bannerUri);
                    }
                }
            });
            selectMunicipality.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectMunicipality();
                }
            });
            setLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBusinessLocation();
                }
            });

            try {
                inpt_email.setText(mAuth.getCurrentUser().getEmail());
                input_contact.setText(mAuth.getCurrentUser().getPhoneNumber());
            }catch (NullPointerException e){

            }

            showLast();
            if (ContextCompat.checkSelfPermission(OwnerRegistrationNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OwnerRegistrationNew.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                return;
            }

            businessProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performFileSearch();
                }
            });
            selectBarangay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (!municipalityId.equals(null)){
                            fetchBarangays();
                        }
                    }catch (NullPointerException e){
                        Utils.callToast(context,"Select Municipality Firts");
                    }
                }
            });
        getSpecificLocation();
        textWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiAvailability();
            }
        });
        signalStreng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignalStreng();
            }
        });
        managePermits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managePermits();
            }
        });
        }

        private void setSignalStreng(){

            if (signalLabel.equals("No Signal")) {
                signalImage.setColorFilter(getResources().getColor(R.color.poor));
                signalLabel = "poor";
                signalStreng.setText("poor");
            }
            else if (signalLabel.equals("poor")){
                signalImage.setColorFilter(getResources().getColor(R.color.fair));
                signalLabel = "fair";
                signalStreng.setText("fair");
            }
            else if (signalLabel.equals("fair")){
                signalImage.setColorFilter(getResources().getColor(R.color.Strong));
                signalLabel = "Strong";
                signalStreng.setText("Strong");
            }

            else if (signalLabel.equals("Strong")){
                signalImage.setColorFilter(getResources().getColor(R.color.noSignal));
                signalLabel = "No Signal";
                signalStreng.setText("No Signal");
            }


        }


        private void selectBusinessTypeDialog(){
            final Dialog dialog = new Dialog(OwnerRegistrationNew.this);
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
            try{
                if (inpt_name.getText().toString().trim().length()==0){
                    val = false;
                    inpt_name.setError("This field can not be blank");
                }
                else if (municipality.trim().length()==0){
                    val = false;
                    Utils.callToast(context,"Select Municipality");
                }
                else if (input_contact.getText().toString().trim().length()!=11 ){
                    val = false;
                    input_contact.setError("Number not Valid");
                }

                else if(inpt_email.getText().toString().trim().length()==0){
                    val = false;
                    inpt_email.setError("This field can not be blank");
                }
                else if (businessType==null){
                    val = false;
                }
                else if (selectBarangay.getText().toString().equals("Barangay")){
                    val = false;
                    Utils.callToast(context,"Select Barangay");
                }
                else if (!businessSpecificLocation){
                    val = false;
                    Utils.callToast(context,"Select Location");
                }
                else if (howToGetThere.getText().toString().trim().length()==0){
                    val = false;
                }

            }catch (NullPointerException e){
                val = false;
            }
            return val;
        }
        private void selectBType(String type){
            selectBType.setText(type);
            businessType = type;
        }



        private void showLast() {
            Location lastLocation = SmartLocation.with(this).location().getLastLocation();
            if (lastLocation != null) {

            }

            DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
            if (detectedActivity != null) {

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

    public void performFileSearch() {
        getStoragePermission();
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Filter to show only images, using the image MIME data imageType.
        // If one wanted to search for ogg vorbis files, the imageType would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Choose File"), READ_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, resultData);
        }
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("TAG", "Uri: " + uri.getLastPathSegment());
//                ImageBannerOldURL = bannerUri;

                setImage(uri,businessProfile);

            }else {
                System.out.println("null?");
            }
        }
    }
    private void setImage(Uri uri, ImageView imageView){
        // floatClearImage.setVisibility(View.VISIBLE);
        // Picasso.with(CreatePostActivity.this).load(uri).resize(300,600).into(imageToUpload);
        bannerUri = uri;
        imageSet = true;
        Glide.with(OwnerRegistrationNew.this).load(uri).into(imageView);
        imageView.setPadding(0,0,0,0);
    }


    public void uploadItemBanner(final Uri ImageStorageURI){
//        setProgress(true);
        loadingContainer.setVisibility(View.VISIBLE);
        if (ImageStorageURI!=null) {
            InputStream storeBannerFile = null;
            try {
                storeBannerFile = getContentResolver().openInputStream(ImageStorageURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final InputStream file = storeBannerFile;

            final StorageReference ImagestoreRef = mStorageRef.child("images"+ File.separator+ File.separator + getFileName(ImageStorageURI)
                    +storeBannerFile.toString()+File.separator+getFileName(ImageStorageURI));
            ImagestoreRef.putStream(storeBannerFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImagestoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            uploadPost(caption.getText().toString(),uri.toString());

                            Utils.callToast(context,"Success");
                            loadingContainer.setVisibility(View.GONE);
                            saveProfile(inpt_name.getText().toString(),
                                    input_contact.getText().toString(),
                                    inpt_email.getText().toString(),uri.toString(),selectBarangay.getText().toString()
                            );
                            signalWifiStrength(signalLabel,wifiAvail);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    setProgress(false);
                    System.out.print(e);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    try {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/file.available());
//                        uploadProgress.setText("Uploading " +taskSnapshot.getBytesTransferred()+" of "+file.available()+" "+progress+" %");
                    }catch (IOException e){

                    }

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        access_storage = false;
        switch (requestCode) {
            case storagepermision_access_code: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    access_storage = true;
                }
            }
        }
    }


    private String getFileName(Uri uri) {

        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void getStoragePermission(){
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            access_storage = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    storagepermision_access_code);
        }
    }
    private void selectMunicipality(){
        final String[] items ={};
        final ArrayList<String> mun = new ArrayList<>();
        final ArrayList<MunicipalityDataModel> municipalityDataModelArrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("municipality").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MunicipalityDataModel municipalityDataModel = new MunicipalityDataModel();
                    MunicipalityMapModel municipalityMapModel = dataSnapshot1.getValue(MunicipalityMapModel.class);
                    municipalityDataModel.setKey(municipalityMapModel.key);
                    municipalityDataModel.setMunicipality(municipalityMapModel.municipality);
                    municipalityDataModelArrayList.add(municipalityDataModel);
                    mun.add(municipalityMapModel.municipality);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OwnerRegistrationNew.this);
                builder.setTitle("Select Municipality");
                builder.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, mun),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectMunicipality.setText(mun.get(which));
                                municipality = municipalityDataModelArrayList.get(which).getKey();
                                municipalityId  = municipalityDataModelArrayList.get(which).getKey();
                            }
                        });
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void selectBusinessLocation(){
        Intent i = new Intent(context,MapsProfileUpdateActivity.class);
        i.putExtra("key",key);
        startActivity(i);

    }
    void managePermits(){
        Intent i = new Intent(context,ManagePermits.class);
        i.putExtra("key",key);
        startActivity(i);
    }


    private void fetchBarangays(){
        barangayDataModelArrayList.clear();
        final ArrayList<String> mun = new ArrayList<>();
        databaseReference.child(Utils.BARANGAY_DIR).child(municipalityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                barangayDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    BarangayDataModel barangayDataModel = new BarangayDataModel();
                    BarangayMapModel barangayMapModel = dataSnapshot1.getValue(BarangayMapModel.class);
                    barangayDataModel.setBarangay(barangayMapModel.barangay);
                    barangayDataModel.setKey(barangayMapModel.key);
                    barangayDataModel.setMunId(barangayMapModel.munId);
                    barangayDataModelArrayList.add(barangayDataModel);
                    mun.add(barangayMapModel.barangay);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OwnerRegistrationNew.this);
                builder.setTitle("Select Barangay");
                builder.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, mun),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectBarangay.setText(mun.get(which));

                            }
                        });
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getSpecificLocation(){
        databaseReference.child("businessLocations").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RestaurantLocationMapModel restaurantLocationMapModel = dataSnapshot.getValue(RestaurantLocationMapModel.class);
                try {
                    if (!restaurantLocationMapModel.key.equals(null)){
                        businessSpecificLocation = true;
                        setLocation.setText("true");
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void wifiAvailability(){
        Utils.callToast(OwnerRegistrationNew.this,"clicked");
        if (wifiAvail){
            wifiIcon.setColorFilter(getResources().getColor(R.color.lightGrey));
            textWifi.setText("Wifi Unavailable");
            wifiAvail = !wifiAvail;
        }else {
            wifiIcon.setColorFilter(getResources().getColor(R.color.green));
            textWifi.setText("Wifi Available");
            wifiAvail = !wifiAvail;
        }
    }

    private void signalWifiStrength(String signal,boolean wifi){
        SignalWifiMapModel signalWifiMapModel =  new SignalWifiMapModel(key,wifi,signal);
        Map<String,Object> profileValue = signalWifiMapModel.toMap();
        Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(key,profileValue);

        databaseReference.child("signalWifiStrength").updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(context,ManageBusiness.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void saveProfile(String name,String contact,String emaill,String url,String barangay){
        String uid = mAuth.getUid();

        BusinessProfileMapModel businessProfileMapModel = new BusinessProfileMapModel(uid,name,"null for now",contact,emaill,businessType,url,key,municipality,barangay,
                howToGetThere.getText().toString().equals(null)? "Null":howToGetThere.getText().toString());
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
}


