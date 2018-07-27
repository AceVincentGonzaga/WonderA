package com.wandera.wanderaowner;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static void callToast(Context context,String mes){
        Toast.makeText(context,mes,Toast.LENGTH_SHORT).show();
    }
    public static String businessProfiles= "businessProfiles";

    public static void print(String message,String premessage){
        System.out.println(premessage +" :"+message);
    }

    public static String getDateToStrig(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        String getDate = c.getTime().toString().substring(4,10);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime()).toString();

        return Utils.formatTheDate(formattedDate);
    }

    private static String formatTheDate(String date){
        // "yyyy.MM.dd.HH.mm.ss" present format to be converted
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        month = Utils.getMonthInWords(month);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat datef = new SimpleDateFormat("KK:mm a");
        // you can get seconds by adding  "...:ss" to it
        datef.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String localTime = datef.format(currentLocalTime);
        System.out.println(month+" "+day+", "+year+" at "+localTime);
        return month+" "+day+", "+year+" at "+localTime;
    }

    private static String getMonthInWords(String num){
        int num_month = Integer.parseInt(num);
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return months[num_month-1];
    }

    public static String getMonth(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        String getDate = c.getTime().toString().substring(4,10);
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String formattedDate = df.format(c.getTime()).toString();

        return Utils.getMonthInWords(formattedDate);
    }

    public static String getDay(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        String getDate = c.getTime().toString().substring(4,10);
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(c.getTime()).toString();

        return formattedDate;
    }
    public static String getYear(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        String getDate = c.getTime().toString().substring(4,10);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String formattedDate = df.format(c.getTime()).toString();

        return formattedDate;
    }

    public static String RESTAURANT_DIR = "restaurant";
    public static String MENUS_DIR ="restaurant/menus";
}
