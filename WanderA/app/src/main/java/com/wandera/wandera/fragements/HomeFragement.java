package com.wandera.wandera.fragements;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.browseAll.AccomodationAll;
import com.wandera.wandera.activity.browseAll.RestaurantAll;
import com.wandera.wandera.datamodel.MunicipalityDataModel;
import com.wandera.wandera.mapmodel.MunicipalityMapModel;
import com.wandera.wandera.views.MunicipalityRecyclerViewAdapter;

import java.util.ArrayList;

public class HomeFragement extends Fragment {
    RecyclerView municipality_list;
    MunicipalityRecyclerViewAdapter municipalityRecyclerViewAdapter;
    ArrayList<MunicipalityDataModel> municipalityDataModelArrayList = new ArrayList<>();
    Button municipalitybutton,all_button;
    ConstraintLayout businessType;
    ImageView resto,accomodation;
    public HomeFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home, container, false);

        resto = (ImageView) view.findViewById(R.id.resto);
        accomodation = (ImageView) view.findViewById(R.id.accomodation);
        businessType = (ConstraintLayout) view.findViewById(R.id.businessType);
        all_button = (Button)view.findViewById(R.id.all_button);
        municipality_list = (RecyclerView) view.findViewById(R.id.municipality_list);
        municipalityRecyclerViewAdapter = new MunicipalityRecyclerViewAdapter(getActivity(),municipalityDataModelArrayList);
        municipality_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        municipality_list.setAdapter(municipalityRecyclerViewAdapter);

        FirebaseDatabase.getInstance().getReference().child(Utils.municipality).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                municipalityDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MunicipalityDataModel municipalityDataModel = new MunicipalityDataModel();
                    MunicipalityMapModel municipalityMapModel = dataSnapshot1.getValue(MunicipalityMapModel.class);
                    municipalityDataModel.setKey(municipalityMapModel.key);
                    municipalityDataModel.setMunicipality(municipalityMapModel.municipality);
                    municipalityDataModelArrayList.add(municipalityDataModel);
                }
                municipalityRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        municipalitybutton= (Button) view.findViewById(R.id.municipality_button);
        municipalitybutton.setSelected(true);
        municipalitybutton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.background));
        all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_button.setSelected(true);
                municipalitybutton.setSelected(false);
                municipalitybutton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.white));
                all_button.setTextColor(getActivity().getApplication().getResources().getColor(R.color.background));
                municipality_list.setVisibility(View.GONE);
                businessType.setVisibility(View.VISIBLE);


            }
        });
        municipalitybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_button.setSelected(false);
                municipalitybutton.setSelected(true);
                municipalitybutton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.background));
                all_button.setTextColor(getActivity().getApplication().getResources().getColor(R.color.white));
                municipality_list.setVisibility(View.VISIBLE);
                businessType.setVisibility(View.GONE);
            }
        });

        resto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RestaurantAll.class);
                startActivity(i);
            }
        });
        accomodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AccomodationAll.class);
                startActivity(i);
            }
        });

        return view;
    }
}
