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
import com.wandera.wandera.datamodel.RestaurantMenuDataModel;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class RestuarantMenusRecyclerViewAdapter
        extends RecyclerView.Adapter<RestuarantMenusRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<RestaurantMenuDataModel> restaurantMenuDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView menuImage;
        public TextView menuName;
        public TextView menuPrice;



        public MyViewHolder(View view){
            super(view);
            menuImage = (ImageView) view.findViewById(R.id.menuImage);
            menuName = (TextView) view.findViewById(R.id.menuName);
            menuPrice = (TextView) view.findViewById(R.id.menuPrice);

        }
    }

    public RestuarantMenusRecyclerViewAdapter(Context c, ArrayList<RestaurantMenuDataModel> restaurantMenuDataModels){
        this.restaurantMenuDataModelArrayList = restaurantMenuDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_menu,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RestaurantMenuDataModel restaurantMenuDataModel = restaurantMenuDataModelArrayList.get(position);
        GlideApp.with(context).load(restaurantMenuDataModel.getMenuIconPath()).centerCrop().into(holder.menuImage);
        holder.menuName.setText(restaurantMenuDataModel.getMenuName());
        holder.menuPrice.setText("₱ "+restaurantMenuDataModel.getMenuPrice());


    }

    @Override
    public int getItemCount() {
        return restaurantMenuDataModelArrayList.size();
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public interface OnItemClickLitener {
        void onItemClick(View v,int pos,RestaurantMenuDataModel restaurantMenuDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}


