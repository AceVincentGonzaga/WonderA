package com.wandera.wandera.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wandera.wandera.Municipality;
import com.wandera.wandera.R;
import com.wandera.wandera.activity.BrowseBusinesses;
import com.wandera.wandera.activity.PhraseBookActivity;
import com.wandera.wandera.datamodel.MunicipalityDataModel;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class MunicipalityRecyclerViewAdapter
        extends RecyclerView.Adapter<MunicipalityRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<MunicipalityDataModel> municipalityDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView municipality;



        public MyViewHolder(View view){
            super(view);

            municipality =(TextView) view.findViewById(R.id.municipalityName);



        }
    }

    public MunicipalityRecyclerViewAdapter(Context c, ArrayList<MunicipalityDataModel> municipalityDataModels){
        this.municipalityDataModelArrayList = municipalityDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.municipality_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MunicipalityDataModel municipalityDataModel = municipalityDataModelArrayList.get(position);
        holder.municipality.setText(municipalityDataModel.getMunicipality());
        holder.municipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BrowseBusinesses.class);
                i.putExtra("municipalityKey",municipalityDataModel.getKey());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return municipalityDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


