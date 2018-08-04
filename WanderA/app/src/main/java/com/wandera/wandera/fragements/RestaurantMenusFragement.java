package com.wandera.wandera.fragements;

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
import com.wandera.wandera.activity.RestaurantProfileBotNav;
import com.wandera.wandera.datamodel.RestaurantMenuCategoryDataModel;
import com.wandera.wandera.mapmodel.RestuarantCategoryMenuMapModel;
import com.wandera.wandera.views.RestuarantCategoryRecyclerViewAdapter;

import java.util.ArrayList;

public class RestaurantMenusFragement extends Fragment {
    RecyclerView categoryList;
    RestuarantCategoryRecyclerViewAdapter restuarantCategoryRecyclerViewAdapter;
    ArrayList<RestaurantMenuCategoryDataModel> restaurantMenuCategoryDataModelArrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    RestaurantProfileBotNav act;
    public RestaurantMenusFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_menus, container, false);
                act = (RestaurantProfileBotNav) getActivity();
                categoryList = (RecyclerView) view.findViewById(R.id.categoryList);
                restuarantCategoryRecyclerViewAdapter = new RestuarantCategoryRecyclerViewAdapter(getActivity(),restaurantMenuCategoryDataModelArrayList);
                categoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
                categoryList.setAdapter(restuarantCategoryRecyclerViewAdapter);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child(Utils.CATEGORY_DIR)
                        .child(act.getBusinessKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                           RestaurantMenuCategoryDataModel restaurantMenuCategoryDataModel = new RestaurantMenuCategoryDataModel();
                           RestuarantCategoryMenuMapModel restuarantCategoryMenuMapModel = dataSnapshot1.getValue(RestuarantCategoryMenuMapModel.class);
                           restaurantMenuCategoryDataModel.setCategory(restuarantCategoryMenuMapModel.category);
                           restaurantMenuCategoryDataModel.setBusinessKey(restuarantCategoryMenuMapModel.businessKey);
                           restaurantMenuCategoryDataModel.setKey(restuarantCategoryMenuMapModel.key);
                           restaurantMenuCategoryDataModelArrayList.add(restaurantMenuCategoryDataModel);
                       }
                       restuarantCategoryRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(categoryList);
        return view;
    }
}
