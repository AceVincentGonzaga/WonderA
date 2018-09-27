package com.wandera.wandera.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.RestaurantMenuCategoryDataModel;
import com.wandera.wandera.datamodel.RestaurantMenuDataModel;
import com.wandera.wandera.datamodel.TranspoDataModel;
import com.wandera.wandera.mapmodel.RestaurantMenuMapModel;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class TransportaionRecyclerViewAdapter
        extends RecyclerView.Adapter<TransportaionRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<TranspoDataModel> transpoDataModelArrayList = new ArrayList<>();

    private Context context;
    RestuarantMenusRecyclerViewAdapter restuarantMenusRecyclerViewAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView drivername;
        TextView seats;
        TextView price;
        TextView contactNumber;
        ImageView vamImage;
        ImageView callIcon;
        TextView operator;
        TextView modeOfTranspo;



        public MyViewHolder(View view){
            super(view);
            drivername = (TextView) view.findViewById(R.id.drivername);
            seats = (TextView) view.findViewById(R.id.seats);
            price = (TextView) view.findViewById(R.id.price);
            contactNumber = (TextView) view.findViewById(R.id.contactNumber);
            vamImage = (ImageView) view.findViewById(R.id.vanImage);
            modeOfTranspo = (TextView) view.findViewById(R.id.modeOfTransaction);
            operator = (TextView) view.findViewById(R.id.operator);
            callIcon = (ImageView) view.findViewById(R.id.callIcon);


        }
    }

    public TransportaionRecyclerViewAdapter(Context c, ArrayList<TranspoDataModel> transpoDataModels){
        this.transpoDataModelArrayList = transpoDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tranportation,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TranspoDataModel transpoDataModel = transpoDataModelArrayList.get(position);
        holder.drivername.setText(transpoDataModel.getDriverName());
        holder.contactNumber.setText(transpoDataModel.getContactNumber());
        holder.price.setText("₱ "+transpoDataModel.getPrice());
        holder.seats.setText("Capacity: "+transpoDataModel.getCapacity());
        GlideApp.with(context).load(transpoDataModel.getVanImg()).centerCrop().into(holder.vamImage);
        holder.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(v,position,transpoDataModel);
            }
        });
        holder.operator.setText(transpoDataModel.getOperator());
        holder.modeOfTranspo.setText(transpoDataModel.getModeOfTranspo());

    }

    @Override
    public int getItemCount() {
        return transpoDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, TranspoDataModel transpoDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


