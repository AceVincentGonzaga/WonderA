package com.wandera.wanderaowner.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.GlideApp;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.datamodel.MenuDataModel;
import com.wandera.wanderaowner.datamodel.RoomDataModel;
import com.wandera.wanderaowner.datamodel.UserListDataModel;
import com.wandera.wanderaowner.mapModel.CategoryMapModel;
import com.wandera.wanderaowner.mapModel.MenuMapModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class RoomsRecyclerViewAdapter
        extends RecyclerView.Adapter<RoomsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<RoomDataModel> roomDataModelArrayList = new ArrayList<>();
    private Context context;
    private ArrayList<String> vis = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView roomImage;
        TextView roomName,roomPrice,roomDes;
        ConstraintLayout constraintLayout3;
        ImageView delete,update;

        public MyViewHolder(View view){
            super(view);
            roomImage = (ImageView) view.findViewById(R.id.menuBackGroundImage);
            roomPrice = (TextView) view.findViewById(R.id.roomPrice);
            roomDes = (TextView) view.findViewById(R.id.roomDes);
            roomName = (TextView) view.findViewById(R.id.roomName);
            constraintLayout3 = (ConstraintLayout) view.findViewById(R.id.constraintLayout3);
            delete = (ImageView) view.findViewById(R.id.delete);
            update = (ImageView) view.findViewById(R.id.update);

        }
    }

    public RoomsRecyclerViewAdapter(Context c, ArrayList<RoomDataModel> roomDataModels){
        this.roomDataModelArrayList = roomDataModels;
        this.context =c;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_rooms_accomodations,parent,false);

        for (int i = 0;i<roomDataModelArrayList.size();i++){
            vis.add("false");
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RoomDataModel roomDataModel = roomDataModelArrayList.get(position);
        GlideApp.with(context).load(roomDataModel.getRoomImage()).centerCrop().into(holder.roomImage);
        holder.roomName.setText(roomDataModel.getRoomName());
        holder.roomPrice.setText("₱ "+roomDataModel.getRoomPrice());
        holder.roomDes.setText(roomDataModel.getRoomDescription());
        holder.constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return roomDataModelArrayList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, UserListDataModel userListDataModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private void dialog(int pos){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_accomodation_menus_update_delete);

        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

}


