package com.wandera.wandera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class BussinessListRecyclerViewAdapter extends RecyclerView.Adapter<BussinessListRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<BusinessProfileModel> businessProfileModelArraylist = new ArrayList<>();
    private Context context;
    String posterPrePath = "https://image.tmdb.org/t/p/w500/";

    public class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView businessName;


        public MyViewHolder(View view){
            super(view);
            businessName  = (TextView) view.findViewById(R.id.businessItemName);

        }
    }

    public BussinessListRecyclerViewAdapter(Context c, ArrayList<BusinessProfileModel> businessProfileModel){
        this.businessProfileModelArraylist = businessProfileModel;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.businessName.setText(businessProfileModelArraylist.get(position).getName());
        //GlideApp.with(context).load(posterPrePath+moviesModel.getPoster_path()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return businessProfileModelArraylist.size();
    }
    public interface OnItemClickLitener {


    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


