package com.wandera.wandera.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;
import com.wandera.wandera.datamodel.RestaurantMenuCategoryDataModel;
import com.wandera.wandera.datamodel.RestaurantMenuDataModel;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class RestuarantMenusRecyclerViewAdapter
        extends RecyclerView.Adapter<RestuarantMenusRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<RestaurantMenuDataModel> restaurantMenuCategoryDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView menuImage;
        public TextView menuName;



        public MyViewHolder(View view){
            super(view);
            menuImage = (ImageView) view.findViewById(R.id.menuImage);
            menuName = (TextView) view.findViewById(R.id.menuName);




        }
    }

    public RestuarantMenusRecyclerViewAdapter(Context c, ArrayList<RestaurantMenuDataModel> restaurantMenuDataModels){
        this.restaurantMenuCategoryDataModelArrayList = restaurantMenuDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_menu,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RestaurantMenuDataModel restaurantMenuDataModel = restaurantMenuCategoryDataModelArrayList.get(position);
        GlideApp.with(context).load(restaurantMenuDataModel.getMenuIconPath()).centerCrop().into(holder.menuImage);
        holder.menuName.setText(restaurantMenuDataModel.getMenuName());

    }

    @Override
    public int getItemCount() {
        return restaurantMenuCategoryDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


