package com.wandera.wandera.fragements.accomodationsProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.businessProfiles.AccomodationProfileBotNav;
import com.wandera.wandera.datamodel.RoomDataModel;
import com.wandera.wandera.mapmodel.RoomMapModel;
import com.wandera.wandera.views.accmodationProfile.AccomodationRoomRecyclerViewAdapter;

import java.util.ArrayList;

public class AccomodationRoomsFragement extends Fragment {
    RecyclerView roomList;
    AccomodationRoomRecyclerViewAdapter accomodationRoomRecyclerViewAdapter;
    ArrayList<RoomDataModel> roomDataModelArrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    AccomodationProfileBotNav act;



    public AccomodationRoomsFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accomodations_roomss, container, false);
        roomList = (RecyclerView) view.findViewById(R.id.roomList);
        act = (AccomodationProfileBotNav) getActivity();
        accomodationRoomRecyclerViewAdapter = new AccomodationRoomRecyclerViewAdapter(getActivity(),roomDataModelArrayList);
        roomList.setLayoutManager(new LinearLayoutManager(getActivity()));
        roomList.setAdapter(accomodationRoomRecyclerViewAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Utils.ROOMS_DIR)
                .child(act.getBusinessKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RoomDataModel roomDataModel = new RoomDataModel();
                    RoomMapModel roomMapModel = dataSnapshot1.getValue(RoomMapModel.class);
                    roomDataModel.setBusinessKey(roomMapModel.businessKey);
                    roomDataModel.setRoomDescription(roomMapModel.roomDescription);
                    roomDataModel.setRoomId(roomMapModel.roomId);
                    roomDataModel.setRoomImage(roomMapModel.roomImage);
                    roomDataModel.setRoomPrice(roomMapModel.roomPrice);
                    roomDataModel.setRoomName(roomMapModel.roomName);

                    roomDataModelArrayList.add(roomDataModel);
                }
                accomodationRoomRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(roomList);

        return view;
    }
}
