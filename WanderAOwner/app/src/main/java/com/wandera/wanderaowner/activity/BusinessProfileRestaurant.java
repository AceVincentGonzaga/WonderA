package com.wandera.wanderaowner.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.activity.restaurant.AddGalleryActivity;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.datamodel.GalleryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.CategoryMapModel;
import com.wandera.wanderaowner.mapModel.GalleryMapModel;
import com.wandera.wanderaowner.mapModel.MenuMapModel;
import com.wandera.wanderaowner.mapModel.UserListMapModel;
import com.wandera.wanderaowner.views.CategoryRecyclerViewAdapter;
import com.wandera.wanderaowner.views.GalleryRecyclerViewAdapter;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileRestaurant extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    DatabaseReference mDatabase;
    TextView messages,businessProfile,managePermits;
    ImageView profileIcon;
    Button addCategory;
    Context c;
    ArrayList<CategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    RecyclerView categoryList;
    String businessKey;
    TextView textWifi;
    ImageView wifiIcon;
    boolean wifiAvail = true;
    ArrayList<GalleryDataModel> galleryDataModelArrayList = new ArrayList<>();
    GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    RecyclerView galleryList;
    TextView deleteBusiness;
    private static final int READ_REQUEST_CODE = 42;
    boolean access_storage;
    private static final int storagepermision_access_code = 548;
    StorageReference mStorageRef;
    Uri bannerUri;
    boolean imageSet;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_restaurant);
        addCategory =(Button) findViewById(R.id.addCategory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle=getIntent().getExtras();
        galleryList = (RecyclerView) findViewById(R.id.galleryList);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        businessKey = bundle.getString("key");
        c = BusinessProfileRestaurant.this;
        categoryList = (RecyclerView) findViewById(R.id.categoryList);
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(c,categoryDataModelArrayList);
        categoryList.setLayoutManager(new LinearLayoutManager(c));
        categoryList.setAdapter(categoryRecyclerViewAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withToolbarMenuToggle(toolbar)
                .withDragDistance(200)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withRootViewScale(1f)
                .withRootViewYTranslation(4)
                .withMenuLayout(R.layout.slide_root_nav_business_profile_restaurant)
                .withSavedState(savedInstanceState)
                .withContentClickableWhenMenuOpened(true)
                .inject();
        messages = (TextView)findViewById(R.id.messages);
        businessProfile = (TextView) findViewById(R.id.manageProfile);
        profileIcon = (CircleImageView) findViewById(R.id.profileIcon);
        deleteBusiness = (TextView) findViewById(R.id.deleteBusiness);
        managePermits = (TextView) findViewById(R.id.managePermits);

        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfileRestaurant.this,OwernerRegistrationUpdate.class);
                i.putExtra("businessKey", businessKey);
                startActivity(i);
                finish();
            }
        });
        getCategory();
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=  new Intent(BusinessProfileRestaurant.this, InboxActivity.class);
                i.putExtra("key", businessKey);
                startActivity(i);
                finish();
            }
        });

        managePermits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(c,ManagePermits.class);
                i.putExtra("key",businessKey);
                startActivity(i);
            }
        });
        deleteBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBusinessCofirmation();
            }
        });


        try {
            mDatabase.child(Utils.businessProfiles).child(businessKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                    toolbar.setTitle(businessProfileMapModel.name);
                   try {
                       GlideApp.with(BusinessProfileRestaurant.this).load(businessProfileMapModel.restoProfileImagePath).into(profileIcon);
                   }catch (IllegalArgumentException e){

                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){
            Intent i = new Intent(BusinessProfileRestaurant.this,OwernerRegistration.class);
            startActivity(i);
            finish();
        }
        galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(c,galleryDataModelArrayList,businessKey);
        galleryList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        galleryList.setAdapter(galleryRecyclerViewAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(galleryList);
        getGallery();

        getInboxUnReadMessages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buttom_sheet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }

    private void AddMenuDialog(){

    }

    private void addCategory(){

            final Dialog dialog = new Dialog(c);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.add_category_dialog);
        final TextInputEditText inputCategory = (TextInputEditText) dialog.findViewById(R.id.inputCategory);
        TextView bntSave = (TextView) dialog.findViewById(R.id.btnSave);

        bntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCategory.getText().toString().trim().length()>0){
                    final String key = mDatabase.child("restaurant").child("category").child(businessKey).push().getKey();
                    CategoryMapModel categoryMapModel = new CategoryMapModel(inputCategory.getText().toString(),key,businessKey);
                    Map<String,Object> value = categoryMapModel.toMap();
                    Map<String,Object> childUpdates=new HashMap<>();
                    childUpdates.put(key,value);
                    mDatabase.child("restaurant").child("category").child(businessKey).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();

    }

    public void getCategory(){

        mDatabase.child("restaurant").child("category").child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CategoryDataModel categoryDataModel = new CategoryDataModel();
                    CategoryMapModel categoryMapModel = dataSnapshot1.getValue(CategoryMapModel.class);
                    categoryDataModel.setKey(categoryMapModel.key);
                    categoryDataModel.setCategory(categoryMapModel.category);
                    categoryDataModel.setBusinessKey(categoryMapModel.businessKey);
                    categoryDataModelArrayList.add(categoryDataModel);
                }
                categoryRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getGallery(){
        FirebaseDatabase.getInstance().getReference().child(Utils.GAL_DIR).child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                galleryDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    GalleryMapModel galleryMapModel = dataSnapshot1.getValue(GalleryMapModel.class);
                    GalleryDataModel galleryDataModel = new GalleryDataModel();
                    galleryDataModel.setKey(galleryMapModel.key);
                    galleryDataModel.setBusinesskey(galleryMapModel.businesskey);
                    galleryDataModel.setImagePath(galleryMapModel.imagePath);
                    galleryDataModelArrayList.add(galleryDataModel);
                }
                GalleryDataModel galleryDataModel = new GalleryDataModel();
                galleryDataModel.setKey("nokey");
                galleryDataModel.setBusinesskey("no");
                galleryDataModel.setImagePath("no");
                galleryDataModelArrayList.add(galleryDataModel);
                galleryRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        galleryRecyclerViewAdapter.setOnItemClickListener(new GalleryRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, GalleryDataModel galleryDataModel) {
                if(galleryDataModelArrayList.get(position).getKey().equals("nokey")){
                    performFileSearch();
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


                setImage(uri);


            }else {
                System.out.println("null?");
            }
        }
    }


    private void setImage(Uri uri){
        // floatClearImage.setVisibility(View.VISIBLE);
        // Picasso.with(CreatePostActivity.this).load(uri).resize(300,600).into(imageToUpload);
        bannerUri = uri;
        imageSet = true;

        uploadItemBanner(bannerUri);

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

            final StorageReference ImagestoreRef = mStorageRef.child("images"+ File.separator+ File.separator + getFileName(ImageStorageURI)
                    +storeBannerFile.toString()+File.separator+getFileName(ImageStorageURI));
            ImagestoreRef.putStream(storeBannerFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImagestoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            uploadPost(caption.getText().toString(),uri.toString());
                            addGallery(uri.toString());
                            Utils.callToast(c,"Success");


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


    private void addGallery(String menuImagePath){
        String key = mDatabase.child(Utils.GAL_DIR).child(businessKey).push().getKey();
        GalleryMapModel galleryMapModel = new GalleryMapModel(key,menuImagePath,businessKey);
        Map<String,Object> value = galleryMapModel.toMap();
        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(key,value);

        mDatabase.child(Utils.GAL_DIR).child(businessKey).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    private void deleteBusinessCofirmation(){
        final Dialog dialog = new Dialog(BusinessProfileRestaurant.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dilog_delete_business);
        dialog.findViewById(R.id.deleteCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.deleteConfirmed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBusiness(dialog);
            }
        });



        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void deleteBusiness(final Dialog dialog){
        FirebaseDatabase.getInstance().getReference()
                .child("businessProfiles").child(businessKey)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                finish();
            }
        });
    }

    private void getInboxUnReadMessages(){


        FirebaseDatabase.getInstance().getReference().child("chatUserList").child(businessKey).orderByChild("key").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int unreadMessage = 0;
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                    UserListMapModel userListMapModel = dataSnapshot1.getValue(UserListMapModel.class);
                    if (!userListMapModel.seen_owner){
                        unreadMessage++;
                    }
                }
                TextView unreadMessageCounter = findViewById(R.id.unreadMessage);
                if (unreadMessage!=0){
                    unreadMessageCounter.setVisibility(View.VISIBLE);
                }
                unreadMessageCounter.setText(unreadMessage+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
