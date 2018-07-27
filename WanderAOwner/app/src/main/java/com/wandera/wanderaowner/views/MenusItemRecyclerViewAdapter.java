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
import com.wandera.wanderaowner.activity.AddMenuActivity;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        final MenuDataModel menuDataModel = menuDataModelArrayList.get(position);
        if (menuDataModel.getMenuName().equals("addMenu")){
            holder.addImage.setVisibility(View.VISIBLE);
            holder.menuTitle.setVisibility(View.GONE);
            holder.menuPrice.setVisibility(View.GONE);
            holder.menuRating.setVisibility(View.GONE);
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
                    TextView menuTitle = (TextView) dialog.findViewById(R.id.menuTitle);
                    Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
                    menuTitle.setText(menuDataModel.getMenuName());
                    CircleImageView menuIcon = (CircleImageView) dialog.findViewById(R.id.menuIcon);
                    GlideApp.with(context).load(menuDataModel.getMenuIconPath()).centerCrop().into(menuIcon);
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
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


