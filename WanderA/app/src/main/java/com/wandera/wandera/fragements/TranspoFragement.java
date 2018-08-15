package com.wandera.wandera.fragements;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.activity.HomeSlider;
import com.wandera.wandera.datamodel.TranspoDataModel;
import com.wandera.wandera.mapmodel.TranspoMapModel;
import com.wandera.wandera.views.TransportaionRecyclerViewAdapter;

import java.util.ArrayList;

public class TranspoFragement extends Fragment {
    RecyclerView transpoList;
    TransportaionRecyclerViewAdapter transportaionRecyclerViewAdapter;
    ArrayList<TranspoDataModel> transpoDataModelArrayList = new ArrayList<>();
    HomeSlider act;
    DatabaseReference databaseReference;
    String numberToCall;
    public TranspoFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        act = (HomeSlider) getActivity();
        View view = inflater.inflate(R.layout.frag_transpo, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        transpoList = (RecyclerView) view.findViewById(R.id.transpoList);
        transportaionRecyclerViewAdapter = new TransportaionRecyclerViewAdapter(getActivity(),transpoDataModelArrayList);
        transpoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        transpoList.setAdapter(transportaionRecyclerViewAdapter);

        databaseReference.child("transportation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transpoDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    TranspoDataModel transpoDataModel = new TranspoDataModel();
                    TranspoMapModel transpoMapModel = dataSnapshot1.getValue(TranspoMapModel.class);
                    transpoDataModel.setDriverName(transpoMapModel.name);
                    transpoDataModel.setCapacity(transpoMapModel.seats);
                    transpoDataModel.setContactNumber(transpoMapModel.number);
                    transpoDataModel.setKey(transpoMapModel.key);
                    transpoDataModel.setPrice(transpoMapModel.price);
                    transpoDataModel.setVanImg(transpoMapModel.vanImg);
                    transpoDataModelArrayList.add(transpoDataModel);

                }
                transportaionRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        transportaionRecyclerViewAdapter.setOnItemClickListener(new TransportaionRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, TranspoDataModel transpoDataModel) {
                yourfunction(transpoDataModel.getContactNumber());
                numberToCall = transpoDataModel.getContactNumber();
            }
        });

        return view;

    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;

    public void yourfunction(final String number) {
        if (ContextCompat.checkSelfPermission(act,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            executeCall();
        }
    }

    private void executeCall() {
        // start your call here
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+"+numberToCall));
                startActivity(callIntent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!

                    executeCall();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

}
