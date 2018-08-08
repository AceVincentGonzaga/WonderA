package com.wandera.wanderaowner.activity.touristHotSpot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.mapModel.RoomMapModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    boolean access_storage;
    private static final int storagepermision_access_code = 548;
    boolean imageSet = false;
    Uri bannerUri;
    ImageView roomPhoto;
    TextInputEditText roomName,roomPrice,roomDes;
    TextView save;
    Context context;
    ConstraintLayout loadingContainer;
    StorageReference mStorageRef;
    DatabaseReference mdataBaseRef;
    String businessKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        roomPhoto = (ImageView) findViewById(R.id.roomImage);
        roomName = (TextInputEditText) findViewById(R.id.roomName);
        roomPrice = (TextInputEditText) findViewById(R.id.roomPrice);
        roomDes = (TextInputEditText) findViewById(R.id.roomDes);
        save = (TextView) findViewById(R.id.saveRoom);
        context = AddActivity.this;
        businessKey = getIntent().getExtras().getString("businessKey");
        mdataBaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        loadingContainer = (ConstraintLayout) findViewById(R.id.loadingContainer);
        roomPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    Utils.callToast(context,"true");
                    loadingContainer.setVisibility(View.VISIBLE);
                    uploadItemBanner(bannerUri);
                }
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

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

                setImage(uri,roomPhoto);

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
        GlideApp.with(AddActivity.this).load(uri).centerCrop().into(imageView);
        imageView.setPadding(0,0,0,0);
    }

    private boolean validate(){
        boolean val = true;

        if (roomName.getText().toString().trim().equals("")){
            val = false;
        }
        if (roomPrice.getText().toString().trim().equals("")){
            val = false;
        }
        if (roomDes.getText().toString().trim().equals("")){
            val = false;
        }
        if (bannerUri==null){
            val = false;
        }


        return val;
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
                            saveRoom(uri.toString());
                            Utils.callToast(context,"Success");
                            loadingContainer.setVisibility(View.GONE);

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



    private void saveRoom(String roomImage){

       String key = mdataBaseRef.child("touristspots").child("activity").child(businessKey).push().getKey();
       RoomMapModel roomMapModel = new RoomMapModel(key,roomName.getText().toString(),Integer.parseInt(roomPrice.getText().toString()),roomDes.getText().toString(),businessKey,roomImage);
       Map<String,Object> value = roomMapModel.toMap();
       Map<String,Object> childUpdates = new HashMap<>();
       childUpdates.put(key,value);

        mdataBaseRef.child("touristspots").child("activity").child(businessKey).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }
}
