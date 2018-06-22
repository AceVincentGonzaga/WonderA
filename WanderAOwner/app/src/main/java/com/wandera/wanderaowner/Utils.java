package com.wandera.wanderaowner;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void callToast(Context context,String mes){
        Toast.makeText(context,mes,Toast.LENGTH_SHORT).show();
    }
}
