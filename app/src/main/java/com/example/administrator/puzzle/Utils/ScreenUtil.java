package com.example.administrator.puzzle.Utils;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


public class ScreenUtil {
    /**
     * 获取屏幕参数
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕density
     * @param context
     * @return
     */
    public static float getDeviceDensity(Context context){
        DisplayMetrics metrics =new DisplayMetrics();
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    public static int getScreenWidthDpi(Activity a) {
        WindowManager windowManager = (WindowManager) a.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;//屏幕宽度pixels
        float density = displayMetrics.density;
        int srceenWidth = (int) (width / density);
        return srceenWidth;
    }

    public static int getScreenHightDpi(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;//屏幕宽度pixels
        float density = displayMetrics.density;
        int srceenHeight = (int) (height / density);
        return srceenHeight;
    }

    /**
     * 将px值转换为dip或dp值,保证尺寸大小不变
     *
     * @param pxValue
     * @param context scale
     *                DisplayMetrics中属性density
     * @return
     **/
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值,保证尺寸大小不变
     *
     * @param dipValue
     * @param context  scale
     *                 DisplayMetrics中属性density2
     * @return
     **/
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值,保证文字大小不变
     *
     * @param pxValue
     * @param context scale
     *                DisplayMetrics类中属性scaledDensity
     * @return
     **/
    public static int px2sp(Context context, float pxValue) {
        final float fontscale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontscale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值,保证尺寸大小不变
     *
     * @param spValue
     * @param context scale
     *                DisplayMetrics类中属性scaledDensity
     * @return
     **/
    public static int sp2px(Context context, float spValue) {
        final float fontscale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontscale + 0.5f);
    }


}
