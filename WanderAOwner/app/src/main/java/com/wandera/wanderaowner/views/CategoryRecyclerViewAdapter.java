package com.wandera.wanderaowner.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.ChatMessageMapModel;
import com.wandera.wanderaowner.mapModel.UserProfileMapModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class CategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<CategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryItemName;
        RecyclerView menuItems;

        public MyViewHolder(View view){
            super(view);

            categoryItemName = (TextView) view.findViewById(R.id.categoryItem);
            menuItems = (RecyclerView) view.findViewById(R.id.menuItems);

        }
    }

    public CategoryRecyclerViewAdapter(Context c, ArrayList<CategoryDataModel> categoryDataModels){
        this.categoryDataModelArrayList = categoryDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CategoryDataModel categoryDataModel = categoryDataModelArrayList.get(position);
        holder.categoryItemName.setText(categoryDataModel.getCategory());
        ArrayList<MenuDataModel> menuDataModelArrayList = new ArrayList<>();
        MenuDataModel menuDataModel = new MenuDataModel();
        menuDataModel.setMenuName("addMenu");
        menuDataModelArrayList.add(menuDataModel);
        MenusItemRecyclerViewAdapter menusItemRecyclerViewAdapter = new MenusItemRecyclerViewAdapter(context,menuDataModelArrayList);
        holder.menuItems.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        holder.menuItems.setAdapter(menusItemRecyclerViewAdapter);
        menusItemRecyclerViewAdapter.notifyDataSetChanged();
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
}


