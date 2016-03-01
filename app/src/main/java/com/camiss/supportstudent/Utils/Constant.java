package com.camiss.supportstudent.Utils;

import android.content.Context;

import com.camiss.supportstudent.R;

/**
 * Created by hatuananh on 12/29/15.
 */
public class Constant {
    public static final String RECORD_ID = "record_id";
    public static final String userName = "user_name";
    public static final int NUMBER_OF_TYPE = 4;
    public static final int CATEGORY_UNIVERSITY = 1;
    public static final int CATEGORY_BEFORE_ARRIVING = 2;
    public static final int CATEGORY_AFTER_ARRIVING = 3;
    public static final int CATEGORY_SCHOLARSHIP = 4;
    public static final String RECORD_CONTENT = "record_content";
    public static String EMPTY = "";
    public static final String RECORD_CATEGORY = "record_category";

    public static String getTypeName(int categoryType,Context context) {
        switch (categoryType) {
            case CATEGORY_UNIVERSITY:
                return context.getResources().getText(R.string.category_type_1).toString();
            case CATEGORY_BEFORE_ARRIVING:
                return context.getResources().getText(R.string.category_type_2).toString();
            case CATEGORY_AFTER_ARRIVING:
                return context.getResources().getText(R.string.category_type_3).toString();
            case CATEGORY_SCHOLARSHIP:
                return context.getResources().getText(R.string.category_type_4).toString();
            default:
        }
        return null;
    }
}
