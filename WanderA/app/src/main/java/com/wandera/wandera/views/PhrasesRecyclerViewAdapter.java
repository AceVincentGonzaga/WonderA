package com.wandera.wandera.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;
import com.wandera.wandera.datamodel.PhraseDataModel;
import com.wandera.wandera.mapmodel.PhraseBookCategoryMapModel;

import java.util.ArrayList;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class PhrasesRecyclerViewAdapter
        extends RecyclerView.Adapter<PhrasesRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<PhraseDataModel> phraseDataModelArrayList = new ArrayList<>();
    private Context context;
    private String categoryKey;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textEnglish;
        public TextView textKaraya,categoryName;

        public MyViewHolder(View view){
            super(view);

            textEnglish =(TextView) view.findViewById(R.id.textEnglish);
            textKaraya = (TextView) view.findViewById(R.id.textKaraya);

        }
    }

    public PhrasesRecyclerViewAdapter(Context c, ArrayList<PhraseDataModel> categoryDataModels,String categoryKey){
        this.phraseDataModelArrayList = categoryDataModels;
        this.context =c;
        this.categoryKey = categoryKey;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phrases_list_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PhraseDataModel phraseDataModel = phraseDataModelArrayList.get(position);
        holder.textEnglish.setText(phraseDataModel.getEnglish());
        holder.textKaraya.setText(phraseDataModel.getKaraya());



    }

    @Override
    public int getItemCount() {
        return phraseDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;

    }
}


