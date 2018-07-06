//package com.wandera.wanderaowner.views;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.constraint.ConstraintLayout;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.wandera.wanderaowner.R;
//import com.wandera.wanderaowner.datamodel.BusinessProfileModel;
//import com.wandera.wanderaowner.datamodel.UserListDataModel;
//
//import java.util.ArrayList;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<BussinessListRecyclerViewAdapter.MyViewHolder> {
//    private ArrayList<UserListDataModel>userListDataModelArrayList= new ArrayList<>();
//    public Context context;
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        public TextView userName;
//        public CircleImageView userImage;
//
//        public MyViewHolder(View view) {
//            super(view);
//            userImage=(CircleImageView) view.findViewById(R.id.userImage);
//            userName=(TextView) view.findViewById(R.id.userName);
//        }
//
//    }
//    public UserListRecyclerViewAdapter(Context c, ArrayList<UserListDataModel> userListDataModels){
//        this.userListDataModelArrayList = userListDataModels;
//        this.context =c;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.usermessageitem,parent,false);
//
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final UserListRecyclerViewAdapter.MyViewHolder holder, final int position) {
//
//    }
//    @Override
//    public int getItemCount() {
//        return userListDataModelArrayList.size();
//    }
//
//    public interface OnItemClickLitener {
//        void onItemClick(View view, int position, UserListDataModel userListDataModel);
//
//    }
//
//    public void setOnItemClickListener(UserListRecyclerViewAdapter.OnItemClickLitener mOnItemClickLitener) {
//        this.mOnItemClickLitener = mOnItemClickLitener;
//    }
//}
