package com.wandera.wanderaowner.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
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
        StorageReference mStorageRef;
        TextView selectMunicipality;
        String municipality;
        String key;
        TextView setLocation;

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

            input_contact = (TextInputEditText) findViewById(R.id.input_contact);
            inpt_email = (TextInputEditText) findViewById(R.id.inpt_email);
            saveProfile = (TextView) findViewById(R.id.saveProfile);
            selectBType = (TextView) findViewById(R.id.selectBType);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            businessProfile = (CircleImageView) findViewById(R.id.businessProfile);
            selectMunicipality = (TextView) findViewById(R.id.selectMunicipality);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            setLocation = (TextView) findViewById(R.id.setLocation);

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
                        Utils.callToast(context,"incomplete");
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
            businessProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performFileSearch();
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
            if (municipality.trim().length()==0){
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

        private void saveProfile(String name,String contact,String emaill,String url){
            String uid = mAuth.getUid();

            BusinessProfileMapModel businessProfileMapModel = new BusinessProfileMapModel(uid,name,"null for now",contact,emaill,businessType,url,key,municipality);
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

        private void showLast() {
            Location lastLocation = SmartLocation.with(this).location().getLastLocation();
            if (lastLocation != null) {

            }

            DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
            if (detectedActivity != null) {

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

                        }
                    }
                });
            } else {

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

    public void performFileSearch() {

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
        Glide.with(OwernerRegistration.this).load(uri).into(imageView);
        imageView.setPadding(0,0,0,0);


    }


    public void uploadItemBanner(final Uri ImageStorageURI){
//        setProgress(true);
//        loadingContainer.setVisibility(View.VISIBLE);
        if (ImageStorageURI!=null) {
            InputStream storeBannerFile = null;
            try {
                storeBannerFile = getContentResolver().openInputStream(ImageStorageURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final InputStream file = storeBannerFile;

            final StorageReference ImagestoreRef = mStorageRef.child("blog/images"+ File.separator+ File.separator + getFileName(ImageStorageURI)
                    +storeBannerFile.toString()+File.separator+getFileName(ImageStorageURI));
            ImagestoreRef.putStream(storeBannerFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImagestoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            uploadPost(caption.getText().toString(),uri.toString());

                            Utils.callToast(context,"Success");
//                            loadingContainer.setVisibility(View.GONE);
                            saveProfile(inpt_name.getText().toString(),
                                    input_contact.getText().toString(),
                                    inpt_email.getText().toString(),uri.toString()
                            );
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
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    storagepermision_access_code);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
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

    private void selectMunicipality(){
        final String[] items = {"Tibiao", "Culasi", "Sebaste","Pandan","Libertad","Caluya"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Municipality");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                municipality = items[item];
                selectMunicipality.setText(municipality);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void selectBusinessLocation(){
        Intent i = new Intent(context,MapsProfileUpdateActivity.class);
        i.putExtra("key",key);
        startActivity(i);

    }

}
