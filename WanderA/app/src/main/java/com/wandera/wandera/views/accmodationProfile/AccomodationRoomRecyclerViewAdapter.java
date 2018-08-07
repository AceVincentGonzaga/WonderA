package com.wandera.wandera.views.accmodationProfile;

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
import com.wandera.wandera.datamodel.RoomDataModel;
import com.wandera.wandera.mapmodel.RoomMapModel;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class AccomodationRoomRecyclerViewAdapter
        extends RecyclerView.Adapter<AccomodationRoomRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<RoomDataModel> roomDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView roomImage;
        TextView roomName,roomPrice;

        public MyViewHolder(View view){
            super(view);
            roomImage = (ImageView) view.findViewById(R.id.roomImage);
            roomName = (TextView) view.findViewById(R.id.roomName);
            roomPrice = (TextView) view.findViewById(R.id.roomPrice);
        }
    }

    public AccomodationRoomRecyclerViewAdapter(Context c, ArrayList<RoomDataModel> roomDataModels){
        this.roomDataModelArrayList = roomDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accomodation_room,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RoomDataModel roomDataModel = roomDataModelArrayList.get(position);
        GlideApp.with(context).load(roomDataModel.getRoomImage()).centerCrop().into(holder.roomImage);
        holder.roomName.setText(roomDataModel.getRoomName());
        holder.roomPrice.setText("₱ "+roomDataModel.getRoomPrice());

    }

    @Override
    public int getItemCount() {
        return roomDataModelArrayList.size();
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public interface OnItemClickLitener {
        void onItemClick(View v, int pos, RestaurantMenuDataModel restaurantMenuDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}


