package com.camiss.supportstudent.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by hatuananh on 12/31/15.
 */
public class Pixel {

    public static int dpToPx(int dp,Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
