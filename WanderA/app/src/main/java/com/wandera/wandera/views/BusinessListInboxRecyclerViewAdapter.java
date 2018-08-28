package com.wandera.wandera.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.mapmodel.BusinessProfileMapModel;
import com.wandera.wandera.ChatActivity;
import com.wandera.wandera.ChatMessageMapModel;
import com.wandera.wandera.GlideApp;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.PhraseCategoryDataModel;
import com.wandera.wandera.datamodel.UserListDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class BusinessListInboxRecyclerViewAdapter
        extends RecyclerView.Adapter<BusinessListInboxRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<UserListDataModel> userListDataModelArrayList = new ArrayList<>();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView businessName;
        public ConstraintLayout container;
        public CircleImageView businessProfielImage;
        public TextView lastMessage;


        public MyViewHolder(View view){
            super(view);
            businessProfielImage = (CircleImageView) view.findViewById(R.id.businessProfielImage);
            businessName =(TextView) view.findViewById(R.id.businessName);
            container = (ConstraintLayout) view.findViewById(R.id.container);
            lastMessage = (TextView) view.findViewById(R.id.lastMessage);



        }
    }

    public BusinessListInboxRecyclerViewAdapter(Context c, ArrayList<UserListDataModel> userListDataModels){
        this.userListDataModelArrayList = userListDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_chats_list,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserListDataModel userListDataModel = userListDataModelArrayList.get(position);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(context, ChatActivity.class);
                i.putExtra("key",userListDataModel.getBusinessKey());
                context.startActivity(i);
            }
        });

        FirebaseDatabase.getInstance().getReference().child(Utils.businessProfiel).child(userListDataModel.getBusinessKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               try {
                   BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                   holder.businessName.setText(businessProfileMapModel.name);
                   GlideApp.with(context).load(businessProfileMapModel.restoProfileImagePath).centerCrop().into(holder.businessProfielImage);
               }catch (NullPointerException e){

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child(Utils.chats)
                .child(userListDataModel.getBusinessKey())
                .child(userListDataModel.getUserId()).child(userListDataModel.getKey())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatMessageMapModel messageMapModel = dataSnapshot.getValue(ChatMessageMapModel.class);
                holder.lastMessage.setText(messageMapModel.message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return userListDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, PhraseCategoryDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


