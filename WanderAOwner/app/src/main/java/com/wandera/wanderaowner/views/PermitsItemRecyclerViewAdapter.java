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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.activity.AddPermitItemGalleryActivity;
import com.wandera.wanderaowner.activity.restaurant.AddMenuActivity;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.PermitImageDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class PermitsItemRecyclerViewAdapter
        extends RecyclerView.Adapter<PermitsItemRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<PermitImageDataModel> permitImageDataModels  = new ArrayList<>();
    private Context context;
    private String categoryKey;
    private String businessKey;



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public  ConstraintLayout constraintLayout;
        public ImageView menuBackGroundImage;
        public TextView menuTitle;
        public ImageView addImage;
        public TextView menuPrice;
        public RatingBar menuRating;

        public MyViewHolder(View view){
            super(view);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.container);
            menuBackGroundImage = (ImageView) view.findViewById(R.id.menuBackGroundImage);
            menuTitle = (TextView) view.findViewById(R.id.menuTitle);
            addImage = (ImageView) view.findViewById(R.id.addImage);
            menuPrice = (TextView) view.findViewById(R.id.menuPrice);
            menuRating = (RatingBar) view.findViewById(R.id.menuRating);

        }
    }

    public PermitsItemRecyclerViewAdapter(Context c, ArrayList<PermitImageDataModel> permitImageDataModels, String catergoryKey, String businessKey){
        this.permitImageDataModels = permitImageDataModels;
        this.context =c;
        this.categoryKey = catergoryKey;
        this.businessKey = businessKey;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PermitImageDataModel permitImageDataModel = permitImageDataModels.get(position);
        if (permitImageDataModel.getImageUrl().equals("addMenu")){
            holder.addImage.setVisibility(View.VISIBLE);
            holder.menuTitle.setVisibility(View.GONE);
            holder.menuPrice.setVisibility(View.GONE);
            holder.menuBackGroundImage.setVisibility(View.GONE);
            holder.menuRating.setVisibility(View.GONE);
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AddPermitItemGalleryActivity.class);
                    i.putExtra("categoryKey",categoryKey);
                    i.putExtra("businessKey",businessKey);
                    context.startActivity(i);
                }
            });
        }else {

            holder.menuBackGroundImage.setVisibility(View.VISIBLE);
            holder.addImage.setVisibility(View.GONE);
            holder.menuTitle.setVisibility(View.GONE);
            holder.menuPrice.setVisibility(View.GONE);
            holder.menuRating.setVisibility(View.GONE);
            GlideApp.with(context).load(permitImageDataModel.getImageUrl()).centerCrop().into(holder.menuBackGroundImage);

        }
    }

    @Override
    public int getItemCount() {
        return permitImageDataModels.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PermitImageDataModel permitImageDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private void addMenuDialog(){

    }
}


