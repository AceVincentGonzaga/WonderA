package com.wandera.wandera.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.datamodel.GalleryDataModel;

import java.util.ArrayList;

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

            galleryImage = (ImageView) view.findViewById(R.id.galleryImage);

        }
    }

    public GalleryRecyclerViewAdapter(Context c, ArrayList<GalleryDataModel> galleryDataModels, String businessKey){
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

        GlideApp.with(context).load(galleryDataModel.getImagePath()).centerCrop().into(holder.galleryImage);

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


