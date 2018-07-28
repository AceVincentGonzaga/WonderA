package com.wandera.wandera.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wandera.wandera.R;
import com.wandera.wandera.activity.PhraseBookActivity;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;


import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class PhraseCategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<PhraseCategoryRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<PhraseCategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;



        public MyViewHolder(View view){
            super(view);

            categoryName =(TextView) view.findViewById(R.id.categoryName);



        }
    }

    public PhraseCategoryRecyclerViewAdapter(Context c, ArrayList<PhraseCategoryDataModel> categoryDataModels){
        this.categoryDataModelArrayList = categoryDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phrase_category_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PhraseCategoryDataModel categoryDataModel = categoryDataModelArrayList.get(position);
        holder.categoryName.setText(categoryDataModel.getCategory());
        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PhraseBookActivity.class);
                i.putExtra("categoryKey",categoryDataModel.getKey());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


