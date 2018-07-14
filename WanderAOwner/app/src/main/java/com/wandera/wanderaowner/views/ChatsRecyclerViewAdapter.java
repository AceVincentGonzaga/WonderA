package com.wandera.wanderaowner.views;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.datamodel.ChatsDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.ChatMessageMapModel;
import com.wandera.wanderaowner.mapModel.UserProfileMapModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class ChatsRecyclerViewAdapter
        extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ChatsDataModel> chatsDataModelArrayList = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase;

    public class MyViewHolder extends RecyclerView.ViewHolder{
       TextView message;
       TextView time;
       ImageView image_view;
       ImageView image_viewOther;
       TextView messageOther;
       TextView timeOther;


        public MyViewHolder(View view){
            super(view);

            message=(TextView) view.findViewById(R.id.message);
             time= (TextView) view.findViewById(R.id.time);
            image_view=(ImageView)view.findViewById(R.id.user_image);
            image_viewOther=(ImageView)view.findViewById(R.id.user_imageOther);
            messageOther = (TextView) view.findViewById(R.id.messageOther);
            timeOther = (TextView)view.findViewById(R.id.timeOther);
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
       try {
           holder.message.setText(chatsDataModelArrayList.get(position).getMessage());
            holder.time.setText(chatsDataModelArrayList.get(position).getTimeStamp());


       }catch (NullPointerException e){
           System.out.print(e);
       }
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(chatsDataModelArrayList.get(position).getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfileMapModel userProfileMapModel= dataSnapshot.getValue(UserProfileMapModel.class);
                    GlideApp.with(context).load(userProfileMapModel.userImage).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.image_view);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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


