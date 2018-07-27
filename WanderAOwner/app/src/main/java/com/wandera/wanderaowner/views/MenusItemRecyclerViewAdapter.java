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
import android.widget.ImageView;
import android.widget.TextView;

import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.activity.AddMenuActivity;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class MenusItemRecyclerViewAdapter
        extends RecyclerView.Adapter<MenusItemRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<MenuDataModel> menuDataModelArrayList = new ArrayList<>();
    private Context context;
    private String categoryKey;
    private String businessKey;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public  ConstraintLayout constraintLayout;
        public ImageView menuBackGroundImage;
        public TextView menuTitle;
        public ImageView addImage;

        public MyViewHolder(View view){
            super(view);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.container);
            menuBackGroundImage = (ImageView) view.findViewById(R.id.menuBackGroundImage);
            menuTitle = (TextView) view.findViewById(R.id.menuTitle);
            addImage = (ImageView) view.findViewById(R.id.addImage);

        }
    }

    public MenusItemRecyclerViewAdapter(Context c, ArrayList<MenuDataModel> categoryDataModels,String catergoryKey,String businessKey){
        this.menuDataModelArrayList = categoryDataModels;
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
        MenuDataModel menuDataModel = menuDataModelArrayList.get(position);
        if (menuDataModel.getMenuName().equals("addMenu")){
            holder.addImage.setVisibility(View.VISIBLE);
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AddMenuActivity.class);
                    i.putExtra("categoryKey",categoryKey);
                    i.putExtra("businessKey",businessKey);
                    context.startActivity(i);

                }
            });
        }else {
            holder.addImage.setVisibility(View.GONE);
            holder.menuTitle.setText(menuDataModel.getMenuName());
            GlideApp.with(context).load(menuDataModel.getMenuIconPath()).centerCrop().into(holder.menuBackGroundImage);
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_add_menu);
                    final Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawableResource(R.color.colorTransparent);
                    window.setGravity(Gravity.CENTER);
                    dialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menuDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, UserListDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private void addMenuDialog(){

    }
}


