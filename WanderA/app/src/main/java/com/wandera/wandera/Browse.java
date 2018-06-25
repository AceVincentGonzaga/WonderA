package com.wandera.wandera;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Browse extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    DatabaseReference databaseReference;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<BusinessProfileModel> businessList = new ArrayList<>();
    RecyclerView businessListRV;
    BussinessListRecyclerViewAdapter bussinessListRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        businessListRV = (RecyclerView) findViewById(R.id.businessListRV);
        bussinessListRecyclerViewAdapter = new BussinessListRecyclerViewAdapter(Browse.this,businessList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        businessListRV.setLayoutManager(layoutManager);
        businessListRV.setAdapter(bussinessListRecyclerViewAdapter);

        databaseReference.child("businessProfiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot1.getValue(BusinessProfileMapModel.class);
                    BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                    businessProfileModel.setName(businessProfileMapModel.name);
                    businessList.add(businessProfileModel);
                }
                bussinessListRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
