package com.wandera.wandera.views.ratingAndComments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.UserProfileMapModel;
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;
import com.wandera.wandera.datamodel.RatingCommentDataModel;
import com.wandera.wandera.mapmodel.UserListMapModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class RatingsRecyclerViewAdapter
        extends RecyclerView.Adapter<RatingsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<RatingCommentDataModel> ratingCommentDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView accountName;
        public CircleImageView userAccountImage;
        public TextView comment;
        public RatingBar rating;



        public MyViewHolder(View view){
            super(view);

            accountName = (TextView) view.findViewById(R.id.accountName);
            userAccountImage = (CircleImageView) view.findViewById(R.id.userAccountImage);
            comment = (TextView) view.findViewById(R.id.comment);
            rating = (RatingBar) view.findViewById(R.id.rating);



        }
    }

    public RatingsRecyclerViewAdapter(Context c, ArrayList<RatingCommentDataModel> ratingCommentDataModels){
        this.ratingCommentDataModelArrayList = ratingCommentDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rating,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RatingCommentDataModel ratingCommentMapModel = ratingCommentDataModelArrayList.get(position);
        holder.comment.setText(ratingCommentMapModel.getComment());
        holder.rating.setRating(ratingCommentMapModel.getRating());

        FirebaseDatabase.getInstance().getReference().child("users").child(ratingCommentMapModel.getAccountId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileMapModel userProfileMapModel = dataSnapshot.getValue(UserProfileMapModel.class);
                holder.accountName.setText(userProfileMapModel.userName);
                GlideApp.with(context).load(userProfileMapModel.userImage).into(holder.userAccountImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ratingCommentDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


