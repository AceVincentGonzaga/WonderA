package com.wandera.wanderaowner.views;

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
import com.wandera.wanderaowner.datamodel.PermitImageDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.MenuMapModel;
import com.wandera.wanderaowner.mapModel.PermitItemMapModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class PermitCategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<PermitCategoryRecyclerViewAdapter.MyViewHolder> {
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

    public PermitCategoryRecyclerViewAdapter(Context c, ArrayList<CategoryDataModel> categoryDataModels){
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
        final CategoryDataModel categoryDataModel = categoryDataModelArrayList.get(position);
        holder.categoryItemName.setText(categoryDataModel.getCategory());
        final ArrayList<PermitImageDataModel> permitImageDataModels = new ArrayList<>();

        final PermitsItemRecyclerViewAdapter permitsItemRecyclerViewAdapter = new PermitsItemRecyclerViewAdapter(context,permitImageDataModels,categoryDataModel.getKey(),categoryDataModel.getBusinessKey());
        holder.menuItems.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        holder.menuItems.setAdapter(permitsItemRecyclerViewAdapter);
        permitsItemRecyclerViewAdapter.notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference().child(Utils.PERMIT_DIR).child(categoryDataModel.getBusinessKey()).child(categoryDataModel.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                permitImageDataModels.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    PermitImageDataModel permitImageDataModel = new PermitImageDataModel();
                    PermitItemMapModel permitItemMapModel = dataSnapshot1.getValue(PermitItemMapModel.class);
                    permitImageDataModel.setImageUrl(permitItemMapModel.imageUrl);
                    permitImageDataModel.setBusinessKey(permitItemMapModel.businessKey);
                    permitImageDataModel.setKey(permitItemMapModel.key);
                    permitImageDataModel.setCategoryKey(permitItemMapModel.categoryKey);
                    permitImageDataModels.add(permitImageDataModel);
                    Utils.print(categoryDataModel.getBusinessKey(),categoryDataModel.getKey());
                }
                PermitImageDataModel permitImageDataModel = new PermitImageDataModel();
                permitImageDataModel.setImageUrl("addMenu");
                permitImageDataModels.add(permitImageDataModel);
                permitsItemRecyclerViewAdapter.notifyDataSetChanged();
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


