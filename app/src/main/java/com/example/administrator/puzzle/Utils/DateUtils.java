package com.example.administrator.puzzle.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Android shnu
 * Created by 140153815cyk on 2017/5/24.
 */

public class DateUtils {
    private static final String TAG ="test";

    public static String getDate(){
        //获取当前日期
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        Log.d(TAG, "write_db: "+date);
        return date;
    }
}
