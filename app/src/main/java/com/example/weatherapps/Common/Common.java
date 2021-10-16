package com.example.weatherapps.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String APP_ID = "edef72d95eb2a103beaa734695b8a637";
    public static Location current_location = null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm \nEEE dd MM yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

}
