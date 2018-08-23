package com.wandera.wandera.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.BrowseBusinesses;
import com.wandera.wandera.datamodel.MunicipalityDataModel;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;
import com.wandera.wandera.datamodel.RestaurantMenuCategoryDataModel;
import com.wandera.wandera.datamodel.RestaurantMenuDataModel;
import com.wandera.wandera.mapmodel.RestaurantMenuMapModel;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class RestuarantCategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<RestuarantCategoryRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<RestaurantMenuCategoryDataModel> restaurantMenuCategoryDataModelArrayList = new ArrayList<>();

    private Context context;
    RestuarantMenusRecyclerViewAdapter restuarantMenusRecyclerViewAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public RecyclerView menuList;



        public MyViewHolder(View view){
            super(view);

            categoryName =(TextView) view.findViewById(R.id.categoryName);
            menuList = (RecyclerView) view.findViewById(R.id.menuList);



        }
    }

    public RestuarantCategoryRecyclerViewAdapter(Context c, ArrayList<RestaurantMenuCategoryDataModel> restaurantMenuCategoryDataModels){
        this.restaurantMenuCategoryDataModelArrayList = restaurantMenuCategoryDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_category,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RestaurantMenuCategoryDataModel restaurantMenuCategoryDataModel = restaurantMenuCategoryDataModelArrayList.get(position);
        final ArrayList<RestaurantMenuDataModel> restaurantMenuDataModelArrayList = new ArrayList<>();
        holder.categoryName.setText(restaurantMenuCategoryDataModel.getCategory());
        restuarantMenusRecyclerViewAdapter = new RestuarantMenusRecyclerViewAdapter(context,restaurantMenuDataModelArrayList);
        holder.menuList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        holder.menuList.setAdapter(restuarantMenusRecyclerViewAdapter);
        FirebaseDatabase.getInstance().getReference().child(Utils.MENU_DIR)
                .child(restaurantMenuCategoryDataModel.getBusinessKey())
                .child(restaurantMenuCategoryDataModel.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantMenuDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RestaurantMenuMapModel restaurantMenuMapModel = dataSnapshot1.getValue(RestaurantMenuMapModel.class);
                    RestaurantMenuDataModel restaurantMenuDataModel = new RestaurantMenuDataModel();
                    restaurantMenuDataModel.setMenuIconPath(restaurantMenuMapModel.menuIconPath);
                    restaurantMenuDataModel.setMenuName(restaurantMenuMapModel.menuName);
                    restaurantMenuDataModel.setMenuPrice(restaurantMenuMapModel.menuPrice);
                    restaurantMenuDataModelArrayList.add(restaurantMenuDataModel);
                }
                restuarantMenusRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantMenuCategoryDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position,RestaurantMenuDataModel restaurantMenuDataModel, RestaurantMenuCategoryDataModel restaurantMenuCategoryDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


