package com.wandera.wandera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Browse extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    DatabaseReference databaseReference;
    Context context;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<BusinessProfileModel> businessList = new ArrayList<>();
    private TextView selectMunicipality;
    RecyclerView businessListRV;
    BussinessListRecyclerViewAdapter bussinessListRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        context = this;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        businessListRV = (RecyclerView) findViewById(R.id.businessListRV);
        selectMunicipality = (TextView)findViewById(R.id.selectMunicipality);
        bussinessListRecyclerViewAdapter = new BussinessListRecyclerViewAdapter(Browse.this,businessList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        businessListRV.setLayoutManager(layoutManager);
        businessListRV.setAdapter(bussinessListRecyclerViewAdapter);

        databaseReference.child(Utils.businessProfiel).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    BusinessProfileMapModel businessProfileMapModel = dataSnapshot1.getValue(BusinessProfileMapModel.class);
                    BusinessProfileModel businessProfileModel = new BusinessProfileModel();
                    businessProfileModel.setName(businessProfileMapModel.name);
                    businessProfileModel.setKey(businessProfileMapModel.key);
                    businessList.add(businessProfileModel);
                }
                bussinessListRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    bussinessListRecyclerViewAdapter.setOnItemClickListener(new BussinessListRecyclerViewAdapter.OnItemClickLitener() {
        @Override
        public void onItemClick(View view, int position, BusinessProfileModel businessProfileModelArraylist) {
            Intent i = new Intent(context,ChatActivity.class);
            i.putExtra("key",businessProfileModelArraylist.getKey());
            startActivity(i);

        }
    });
        selectMunicipality.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             selectMunicipalityDialog();

    }
});
    }
    private void selectMunicipalityDialog(){
        final CharSequence[] items = {"Bugasong", "Laua-an", "Barbaza", "Tibiao", "Culasi", "Sebaste", "Pandan", "Libertad", "Caluya"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Municipality");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                selectMunicipality.setText(items[item]);
                dialog.dismiss();
            }
        });
        builder.show();

    }

}
