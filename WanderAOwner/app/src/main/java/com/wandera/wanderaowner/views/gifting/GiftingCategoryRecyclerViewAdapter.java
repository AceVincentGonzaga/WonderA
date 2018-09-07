package com.wandera.wanderaowner.views.gifting;

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
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.MenuMapModel;
import com.wandera.wanderaowner.views.MenusItemRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class GiftingCategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<GiftingCategoryRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<CategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryItemName;
        RecyclerView menuItems;
        ImageView addImage;

        public MyViewHolder(View view){
            super(view);

            categoryItemName = (TextView) view.findViewById(R.id.categoryItem);
            menuItems = (RecyclerView) view.findViewById(R.id.menuItems);


        }
    }

    public GiftingCategoryRecyclerViewAdapter(Context c, ArrayList<CategoryDataModel> categoryDataModels){
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
        final ArrayList<MenuDataModel> menuDataModelArrayList = new ArrayList<>();

        final GiftingMenusItemRecyclerViewAdapter menusItemRecyclerViewAdapter = new GiftingMenusItemRecyclerViewAdapter(context,menuDataModelArrayList,categoryDataModel.getKey(),categoryDataModel.getBusinessKey());
        holder.menuItems.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        holder.menuItems.setAdapter(menusItemRecyclerViewAdapter);
        menusItemRecyclerViewAdapter.notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference().child(Utils.MENUS_DIR).child(categoryDataModel.getBusinessKey()).child(categoryDataModel.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MenuMapModel menuMapModel = dataSnapshot1.getValue(MenuMapModel.class);
                    MenuDataModel menuDataModel = new MenuDataModel();
                    menuDataModel.setMenuName(menuMapModel.menuName);
                    menuDataModel.setMenuIconPath(menuMapModel.menuIconPath);
                    menuDataModel.setMenuPrice(menuMapModel.menuPrice);
                    menuDataModel.setKey(menuMapModel.key);

                    menuDataModelArrayList.add(menuDataModel);
                }
                MenuDataModel menuDataModel = new MenuDataModel();
                menuDataModel.setMenuName("addMenu");
                menuDataModelArrayList.add(menuDataModel);
                menusItemRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


