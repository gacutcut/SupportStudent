
package com.camiss.supportstudent.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Ha Tuan Anh
 */
public final class PrefUtils {

    /**
     * preference name
     */
    private static final String PREF_NAME = "studentsupport_pref";

    public static final String KEY_LOGIN_USERNAME = "key_login_username";
    public static final String KEY_IS_KEEP_LOGGED_IN = "key_is_keep_logged_in";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_UNIVERSITY_ID = "key_university_id";
    public static final String KEY_UNIVERSITY_NAME = "key_university_name";

    private PrefUtils() {
    }

    /**
     * get preferences
     *
     * @param context context
     * @return SharedPreferences
     */
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * get editor
     *
     * @param context context
     * @return editor
     */
    private static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    /**
     * save string to shared preferences
     *
     * @param context context
     * @param key     key
     * @param value   need save
     */
    public static void saveString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    /**
     * get string from shared preferences
     *
     * @param context  context
     * @param key      key
     * @param defValue default value
     * @return value of key
     */
    public static String getString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    /**
     * save int to shared preferences
     *
     * @param context context
     * @param key     key
     * @param value   need save
     */
    public static void saveInt(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    /**
     * get int from shared preferences
     *
     * @param context  context
     * @param key      key
     * @param defValue default value
     * @return value of key
     */
    public static int getInt(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    /**
     * save boolean to shared preferences
     *
     * @param context context
     * @param key     key
     * @param value   need save
     */
    public static void saveBool(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    /**
     * get boolean from shared preferences
     *
     * @param context  context
     * @param key      key
     * @param defValue default value
     * @return value of key
     */
    public static boolean getBool(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    /**
     * save float to shared preferences
     *
     * @param context context
     * @param key     key
     * @param value   need save
     */
    public static void saveFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    /**
     * get float from shared preferences
     *
     * @param context  context
     * @param key      key
     * @param defValue default value
     * @return value of key
     */
    public static float getFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    /**
     * save long to shared preferences
     *
     * @param context context
     * @param key     key
     * @param value   need save
     */
    public static void saveLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    /**
     * get long from shared preferences
     *
     * @param context  context
     * @param key      key
     * @param defValue default value
     * @return value of key
     */
    public static long getLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    /**
     * Remove key preference
     */
    public static boolean removeKey(Context context, String key) {
        return getEditor(context).remove(key).commit();
    }
}
