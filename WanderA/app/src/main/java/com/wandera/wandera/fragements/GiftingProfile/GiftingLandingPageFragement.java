package com.wandera.wandera.fragements.GiftingProfile;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.wandera.wandera.R;

import java.util.HashMap;
import java.util.Map;

public class GiftingLandingPageFragement extends Fragment {

    public GiftingLandingPageFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_giftcenter_landing_page, container, false);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
