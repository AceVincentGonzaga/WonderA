package com.wandera.wanderaowner.views;

import android.app.Dialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class MenusItemRecyclerViewAdapter
        extends RecyclerView.Adapter<MenusItemRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<MenuDataModel> categoryDataModelArrayList = new ArrayList<>();
    private Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder{
        public  ConstraintLayout constraintLayout;
        public MyViewHolder(View view){
            super(view);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.container);

        }
    }

    public MenusItemRecyclerViewAdapter(Context c, ArrayList<MenuDataModel> categoryDataModels){
        this.categoryDataModelArrayList = categoryDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MenuDataModel menuDataModel = categoryDataModelArrayList.get(position);
        if (menuDataModel.getMenuName().equals("addMenu")){
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_add_menu);
                    dialog.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return categoryDataModelArrayList.size();
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


