package com.wandera.wanderaowner;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.wandera.wanderaowner.mapModel.GetLocationActivity;

import java.util.ArrayList;
import java.util.List;

public class OwernerRegistration extends AppCompatActivity {
    List<String> categories = new ArrayList<String>();
    Spinner spinner;
    TextInputEditText inpt_name,inpt_address,input_contact;
    TextView saveProfile;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = OwernerRegistration.this;
        setContentView(R.layout.activity_owerner_registration);
        inpt_name = (TextInputEditText) findViewById(R.id.input_name);
        inpt_address = (TextInputEditText) findViewById(R.id.input_address);
        input_contact = (TextInputEditText) findViewById(R.id.input_contact);
        saveProfile = (TextView) findViewById(R.id.saveProfile);
        categories.add("Restaurant");
        categories.add("Accomodation");
        categories.add("Pasalubong Center");
        categories.add("Education");
        spinner = (Spinner) findViewById(R.id.businesType);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,categories);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()){
                    Utils.callToast(context,"incomplete");
                }else {
                    Utils.callToast(context,"Success");
                }
            }
        });
        inpt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OwernerRegistration.this, GetLocationActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean validate(){
        boolean val = true;
        if (inpt_name.getText().length()==0){
            val = false;
        }
        if (inpt_address.getText().length()==0){
            val = false;
        }
        if (input_contact.getText().length()==0){
            val = false;
        }
        return val;
    }


}
