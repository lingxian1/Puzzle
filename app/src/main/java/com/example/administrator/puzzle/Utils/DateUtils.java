package com.example.administrator.puzzle.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Android shnu
 * Created by 140153815cyk on 2017/5/24.
 */

public class DateUtils {
    /**
     * 获取当前日期 返回string 形如2017-01-01
     * @return data
     */
    public static String getDate(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        return date;
    }
}
