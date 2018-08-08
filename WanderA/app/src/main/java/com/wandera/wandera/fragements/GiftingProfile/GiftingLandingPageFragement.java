package com.wandera.wandera.fragements.GiftingProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wandera.wandera.R;

public class GiftingLandingPageFragement extends Fragment {

    public GiftingLandingPageFragement(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_page, container, false);
    }
}
