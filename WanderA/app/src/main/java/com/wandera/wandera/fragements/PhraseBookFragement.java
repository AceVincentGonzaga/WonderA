package com.wandera.wandera.fragements;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;
import com.wandera.wandera.mapmodel.PhraseBookCategoryMapModel;
import com.wandera.wandera.views.PhraseCategoryRecyclerViewAdapter;

import java.util.ArrayList;

public class PhraseBookFragement extends Fragment {
    RecyclerView categoryList;
    PhraseCategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    ArrayList<PhraseCategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    DatabaseReference databaseReference;

    public PhraseBookFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_phrasebook, container, false);
        categoryList = (RecyclerView) view.findViewById(R.id.categoryList);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        categoryRecyclerViewAdapter = new PhraseCategoryRecyclerViewAdapter(getContext(),categoryDataModelArrayList);
        categoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryList.setAdapter(categoryRecyclerViewAdapter);

        databaseReference.child(Utils.PHRASE_CATEGORY_DIR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    PhraseCategoryDataModel categoryDataModel = new PhraseCategoryDataModel();
                    PhraseBookCategoryMapModel phraseBookCategoryMapModel = dataSnapshot1.getValue(PhraseBookCategoryMapModel.class);
                    categoryDataModel.setCategory(phraseBookCategoryMapModel.category);
                    categoryDataModel.setKey(phraseBookCategoryMapModel.key);
                    categoryDataModelArrayList.add(categoryDataModel);
                }
                categoryRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;

    }
}
