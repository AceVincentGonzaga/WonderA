package com.wandera.wandera.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.activity.businessProfiles.AccomodationProfileBotNav;
import com.wandera.wandera.activity.businessProfiles.GiftingProfileBotNav;
import com.wandera.wandera.activity.businessProfiles.RestaurantProfileBotNav;
import com.wandera.wandera.activity.businessProfiles.TourisSpotsProfileBotNav;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class BusinessBrowseRecyclerViewAdapter
        extends RecyclerView.Adapter<BusinessBrowseRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<BusinessProfileModel> businessProfileModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView restaurantName;
        CircleImageView restoIcon;



        public MyViewHolder(View view){
            super(view);
            restoIcon = (CircleImageView) view.findViewById(R.id.restoIcon);
            restaurantName =(TextView) view.findViewById(R.id.restaurantName);

        }
    }

    public BusinessBrowseRecyclerViewAdapter(Context c, ArrayList<BusinessProfileModel> businessProfileModels){
        this.businessProfileModelArrayList = businessProfileModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_on_browse_resto,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BusinessProfileModel businessProfileModel = businessProfileModelArrayList.get(position);
        holder.restaurantName.setText(businessProfileModel.getName());
        GlideApp.with(context).load(businessProfileModel.getRestoProfileImagePath()).centerCrop().into(holder.restoIcon);
        holder.restoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessProfileModel.getBusinessType().equals(Utils.bTypeResto)){
                    Intent i = new Intent(context, RestaurantProfileBotNav.class);
                    i.putExtra("businessKey",businessProfileModel.getKey());
                    context.startActivity(i);
                }
                if (businessProfileModel.getBusinessType().equals(Utils.bTypeAccomodations)){
                    Intent i = new Intent(context, AccomodationProfileBotNav.class);
                    i.putExtra("businessKey",businessProfileModel.getKey());
                    context.startActivity(i);
                }
                if (businessProfileModel.getBusinessType().equals(Utils.bTypeGiftingCenter)){
                    Intent i = new Intent(context, GiftingProfileBotNav.class);
                    i.putExtra("businessKey",businessProfileModel.getKey());
                    context.startActivity(i);
                }
                if (businessProfileModel.getBusinessType().equals(Utils.bTypeHotSpots)){
                    Intent i = new Intent(context, TourisSpotsProfileBotNav.class);
                    i.putExtra("businessKey",businessProfileModel.getKey());
                    context.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return businessProfileModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


