package com.wandera.wanderaowner.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wanderaowner.R;
import com.wandera.wanderaowner.Utils;
import com.wandera.wanderaowner.datamodel.CategoryDataModel;
import com.wandera.wanderaowner.mapModel.CategoryMapModel;
import com.wandera.wanderaowner.views.PermitCategoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagePermits extends AppCompatActivity {
    DatabaseReference mDatabase;
    Context c;
    String businessKey;
    TextView addPermit;
    RecyclerView pemitCategoryList;
    PermitCategoryRecyclerViewAdapter permitCategoryRecyclerViewAdapter;
    ArrayList<CategoryDataModel> categoryDataModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_permits);

        c = ManagePermits.this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pemitCategoryList = (RecyclerView) findViewById(R.id.permitList);
        businessKey = getIntent().getExtras().getString("key");
        addPermit = (TextView) findViewById(R.id.addPermit);
        addPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPermitName();
            }
        });
        getPermitCategory();
    }

    void getPermitCategory(){
        mDatabase.child(Utils.PERMIT_CAT_DIR).child(businessKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CategoryDataModel categoryDataModel = new CategoryDataModel();
                    CategoryMapModel categoryMapModel = dataSnapshot1.getValue(CategoryMapModel.class);
                    categoryDataModel.setKey(categoryMapModel.key);
                    categoryDataModel.setCategory(categoryMapModel.category);
                    categoryDataModel.setBusinessKey(categoryMapModel.businessKey);
                    categoryDataModelArrayList.add(categoryDataModel);
                }
                permitCategoryRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        permitCategoryRecyclerViewAdapter =  new PermitCategoryRecyclerViewAdapter(c,categoryDataModelArrayList);
        pemitCategoryList.setLayoutManager(new LinearLayoutManager(c));
        pemitCategoryList.setAdapter(permitCategoryRecyclerViewAdapter);
    }
    private void addPermitName(){

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_category_dialog);
        final TextInputEditText inputCategory = (TextInputEditText) dialog.findViewById(R.id.inputCategory);
        TextView bntSave = (TextView) dialog.findViewById(R.id.btnSave);

        bntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCategory.getText().toString().trim().length()>0){
                    final String key = mDatabase.child("permitCategory").child(businessKey).push().getKey();
                    CategoryMapModel categoryMapModel = new CategoryMapModel(inputCategory.getText().toString(),key,businessKey);
                    Map<String,Object> value = categoryMapModel.toMap();
                    Map<String,Object> childUpdates=new HashMap<>();
                    childUpdates.put(key,value);
                    mDatabase.child("permitCategory").child(businessKey).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }
}
