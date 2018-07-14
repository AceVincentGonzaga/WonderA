package com.wandera.wanderaowner.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.activity.OwernerRegistration;
import com.wandera.wanderaowner.datamodel.BusinessProfileModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.datamodel.UserProfileDataModel;
import com.wandera.wanderaowner.mapModel.ChatMessageMapModel;
import com.wandera.wanderaowner.mapModel.UserListMapModel;
import com.wandera.wanderaowner.mapModel.UserProfileMapModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class UserChatListRecyclerViewAdapter
        extends RecyclerView.Adapter<UserChatListRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<UserListDataModel> userListDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView userName;
        public CircleImageView userImage;
        public TextView preview;
        public ConstraintLayout container;


        public MyViewHolder(View view){
            super(view);
            userImage=(CircleImageView) view.findViewById(R.id.userImage);
            userName=(TextView) view.findViewById(R.id.userName);
            preview = (TextView) view.findViewById(R.id.preview);
            container = (ConstraintLayout) view.findViewById(R.id.container);

        }
    }

    public UserChatListRecyclerViewAdapter(Context c, ArrayList<UserListDataModel> userListDataModels){
        this.userListDataModelArrayList = userListDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.usermessageitem,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserListDataModel userListDataModel = userListDataModelArrayList.get(position);
        holder.userName.setText(userListDataModel.getUserId());
        FirebaseDatabase.getInstance().getReference().child("users").child(userListDataModel.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileMapModel userProfileMapModel  = dataSnapshot.getValue(UserProfileMapModel.class);
                holder.userName.setText(userProfileMapModel.userName);
                GlideApp.with(context).load(userProfileMapModel.userImage).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(userListDataModel.getBusinessKey())
                .child(userListDataModel.getUserId())
                .child(userListDataModel.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatMessageMapModel chatMessageMapModel = dataSnapshot.getValue(ChatMessageMapModel.class);
                holder.preview.setText(chatMessageMapModel.message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(v,position,userListDataModel);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userListDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, UserListDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


