package com.wandera.wandera.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wandera.wandera.R;
import com.wandera.wandera.Utils;
import com.wandera.wandera.datamodel.PhraseDataModel;
import com.wandera.wandera.mapmodel.PhraseBookCategoryMapModel;
import com.wandera.wandera.mapmodel.PhrasesMapModel;
import com.wandera.wandera.views.PhrasesRecyclerViewAdapter;

import java.util.ArrayList;

public class PhraseBookActivity extends AppCompatActivity {
    RecyclerView phrasesList;
    PhrasesRecyclerViewAdapter phrasesRecyclerViewAdapter;
    ArrayList<PhraseDataModel> phraseDataModelArrayList =  new ArrayList<>();
    Context context;
    DatabaseReference databaseReference;
    String categorykey;
    TextView categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_book);
        context = PhraseBookActivity.this;

        phrasesList = (RecyclerView) findViewById(R.id.phrasesList);
        categorykey = getIntent().getExtras().getString("categoryKey");
        categoryName = (TextView) findViewById(R.id.categoryName);
        phrasesRecyclerViewAdapter = new PhrasesRecyclerViewAdapter(context,phraseDataModelArrayList,categorykey);
        phrasesList.setLayoutManager(new LinearLayoutManager(context));
        phrasesList.setAdapter(phrasesRecyclerViewAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Utils.PHRASES_DIR).child(categorykey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phraseDataModelArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    PhraseDataModel phraseDataModel = new PhraseDataModel();
                    PhrasesMapModel phrasesMapModel = dataSnapshot1.getValue(PhrasesMapModel.class);
                    phraseDataModel.setCategory(phrasesMapModel.category);
                    phraseDataModel.setEnglish(phrasesMapModel.english);
                    phraseDataModel.setKaraya(phrasesMapModel.karaya);
                    phraseDataModel.setKey(phrasesMapModel.key);
                    phraseDataModelArrayList.add(phraseDataModel);
                }
                phrasesRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child(Utils.PHRASE_CATEGORY_DIR).child(categorykey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PhraseBookCategoryMapModel phraseBookCategoryMapModel = dataSnapshot.getValue(PhraseBookCategoryMapModel.class);
                categoryName.setText(phraseBookCategoryMapModel.category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
