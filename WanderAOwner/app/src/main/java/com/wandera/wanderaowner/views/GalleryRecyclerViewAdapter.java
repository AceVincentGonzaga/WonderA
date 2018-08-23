package com.wandera.wanderaowner.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.activity.restaurant.AddGalleryActivity;
import com.wandera.wanderaowner.activity.restaurant.AddMenuActivity;
import com.wandera.wanderaowner.datamodel.GalleryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class GalleryRecyclerViewAdapter
        extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<GalleryDataModel> galleryDataModelArrayList = new ArrayList<>();
    private Context context;
    private String categoryKey;
    private String businessKey;



    public class MyViewHolder extends RecyclerView.ViewHolder{
      public ImageView addImage;
      public ImageView galleryImage;

        public MyViewHolder(View view){
            super(view);
            addImage = (ImageView) view.findViewById(R.id.addImage);
            galleryImage = (ImageView) view.findViewById(R.id.galleryImage);

        }
    }

    public GalleryRecyclerViewAdapter(Context c, ArrayList<GalleryDataModel> galleryDataModels,String businessKey){
        this.context = c;
        this.galleryDataModelArrayList = galleryDataModels;
        this.businessKey = businessKey;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GalleryDataModel galleryDataModel = galleryDataModelArrayList.get(position);

          try{
              if (galleryDataModel.getKey().equals("nokey")){
                  holder.addImage.setVisibility(View.VISIBLE);
                  holder.addImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          mOnItemClickLitener.onItemClick(v,position,galleryDataModel);
                      }
                  });
              }else {
                  holder.addImage.setVisibility(View.GONE);
                  GlideApp.with(context).load(galleryDataModel.getImagePath()).centerCrop().into(holder.galleryImage);
              }
          }catch (NullPointerException e){

          }

    }

    @Override
    public int getItemCount() {
        return galleryDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, GalleryDataModel galleryDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private void addMenuDialog(){

    }
}


