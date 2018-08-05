package com.wandera.wandera.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.ChatDataModel;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.UserProfileMapModel;
import com.wandera.wandera.datamodel.BusinessProfileModel;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ChatDataModel> chatDataModels = new ArrayList<>();
    private Context context;
    FirebaseAuth mauth;

    public class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView msg;
    public ImageView user_image;
    public TextView time;
    public ImageView user_imageOther;
    public TextView messageOther;
    public TextView timeOther;


        public MyViewHolder(View view){
            super(view);
            msg = (TextView) view.findViewById(R.id.message);
            user_image = (ImageView) view.findViewById(R.id.user_image);
            time = (TextView)view.findViewById(R.id.time);
            user_imageOther = (ImageView)view.findViewById(R.id.user_imageOther);
            messageOther= (TextView)view.findViewById(R.id.messageOther);
            timeOther= (TextView)view.findViewById(R.id. timeOther);
        }
    }

    public ChatListRecyclerViewAdapter(Context c, ArrayList<ChatDataModel> chatDataModels){
        this.chatDataModels = chatDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChatDataModel chatDataModel  = chatDataModels.get(position);

        if (chatDataModel.getSenderId().equals(chatDataModel.getUserId())){
            holder.msg.setText(chatDataModels.get(position).getMessage());
            holder.time.setText(chatDataModels.get(position).getTimeStamp());

            FirebaseDatabase.getInstance().getReference().child("users").
                    child(chatDataModels.get(position).getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                    UserProfileMapModel userProfileMapModel = dataSnapshot.getValue(UserProfileMapModel.class);
                    GlideApp.with(context).load(userProfileMapModel.userImage).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.user_image);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.messageOther.setVisibility(View.GONE);
            holder.timeOther.setVisibility(View.GONE);
            holder.user_imageOther.setVisibility(View.GONE);
            holder.msg.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.user_image.setVisibility(View.VISIBLE);

        }
        else {
            holder.messageOther.setText(chatDataModels.get(position).getMessage());
            holder.timeOther.setText(chatDataModels.get(position).getTimeStamp());
            holder.timeOther.setVisibility(View.VISIBLE);
            holder.messageOther.setVisibility(View.VISIBLE);
            holder.user_imageOther.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference().child("businessProfiles").child(chatDataModel.getBusinessId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                    GlideApp.with(context).load(businessProfileMapModel.restoProfileImagePath).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.user_imageOther);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.msg.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.user_image.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return chatDataModels.size();
    }
    public interface OnItemClickLitener {
        void onItemClick(View view, int position, BusinessProfileModel businessProfileModelArraylist);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


