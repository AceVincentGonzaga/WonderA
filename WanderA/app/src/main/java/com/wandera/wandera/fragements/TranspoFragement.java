package com.wandera.wandera.fragements;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.wandera.wandera.Transpo;
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
                    transpoDataModelArrayList.add(transpoDataModel);

                }
                transportaionRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;

    }
}
