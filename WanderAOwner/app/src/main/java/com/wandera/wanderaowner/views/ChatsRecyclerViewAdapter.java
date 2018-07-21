package com.wandera.wanderaowner.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.activity.BusinessProfile;
import com.wandera.wanderaowner.datamodel.ChatsDataModel;
import com.wandera.wanderaowner.mapModel.BusinessProfileMapModel;
import com.wandera.wanderaowner.mapModel.UserProfileMapModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class ChatsRecyclerViewAdapter
        extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ChatsDataModel> chatsDataModelArrayList = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase;

    public class MyViewHolder extends RecyclerView.ViewHolder{

       TextView time;
       ImageView user_image;
       ImageView user_imageOther;
       TextView messageOther;
       TextView timeOther;
       TextView msg;


        public MyViewHolder(View view){
            super(view);


             time= (TextView) view.findViewById(R.id.time);
            user_image=(ImageView)view.findViewById(R.id.user_image);
            user_imageOther=(ImageView)view.findViewById(R.id.user_imageOther);
            messageOther = (TextView) view.findViewById(R.id.messageOther);
            timeOther = (TextView)view.findViewById(R.id.timeOther);
            msg= (TextView)view.findViewById(R.id.msg);
        }
    }

    public ChatsRecyclerViewAdapter(Context c, ArrayList<ChatsDataModel> chatsDataModels){
        this.chatsDataModelArrayList = chatsDataModels;
        this.context =c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChatsDataModel chatsDataModel = chatsDataModelArrayList.get(position);

        if (chatsDataModel.getSenderId().equals(chatsDataModel.getBusinessId())) {
            //for business owner
            holder.msg.setText(chatsDataModelArrayList.get(position).getMessage());
            holder.time.setText(chatsDataModelArrayList.get(position).getTimeStamp());

            mDatabase = FirebaseDatabase.getInstance().getReference();
            holder.messageOther.setVisibility(View.GONE);
            holder.timeOther.setVisibility(View.GONE);
            holder.user_imageOther.setVisibility(View.GONE);
            holder.msg.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.user_image.setVisibility(View.VISIBLE);
            FirebaseDatabase.getInstance().getReference().child("businessProfiles").child(chatsDataModel.getBusinessId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot.getValue(BusinessProfileMapModel.class);
                    GlideApp.with(context).load(businessProfileMapModel.restoProfileImagePath).into(holder.user_image);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            //for user
            holder.messageOther.setText(chatsDataModelArrayList.get(position).getMessage());
            holder.timeOther.setText(chatsDataModelArrayList.get(position).getTimeStamp());
            holder.timeOther.setVisibility(View.VISIBLE);
            holder.messageOther.setVisibility(View.VISIBLE);
            holder.user_imageOther.setVisibility(View.VISIBLE);
            holder.msg.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.user_image.setVisibility(View.GONE);
            FirebaseDatabase.getInstance().getReference().child("users").child(chatsDataModel.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfileMapModel profileMapModel = dataSnapshot.getValue(UserProfileMapModel.class);
                    GlideApp.with(context).load(profileMapModel.userImage).into(holder.user_imageOther);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


//        try {
//           holder.msg.setText(chatsDataModelArrayList.get(position).getMessage());
//            holder.time.setText(chatsDataModelArrayList.get(position).getTimeStamp());
//
//
//       }catch (NullPointerException e){
//           System.out.print(e);
//       }



    }

    @Override
    public int getItemCount() {
        return chatsDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, ChatsDataModel chatsDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


