package com.camiss.supportstudent.Utils;

import android.net.Uri;
import android.util.Log;

import com.camiss.supportstudent.Model.Record;
import com.camiss.supportstudent.Model.University;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hatuananh on 1/12/16.
 */
public class DataParseUtils {
    private static final String TAG = DataParseUtils.class.getSimpleName();

    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public static final String STATUS_SUCCESS = "success";
    public static final String USER_ID = "user_id";

    private static final String UNIVERSITY_ID = "idUniversity";
    private static final String UNIVERSITY_NAME = "UniversityName";
    private static final String UNIVERSITY_ADDRESS = "UniversityAddress";
    private static final String UNIVERSITY_PHONE = "UniversityPhoneNumber";
    private static final String UNIVERSITY_LOGO = "UniversityLogoURL";
    private static final String UNIVERSITY_WEBSITE = "UniversityWebsite";


    private static final String UNIVERSITY_INPUT_NAME = "university_name";
    private static final String UNIVERSITY_INPUT_ADDRESS = "university_address";
    private static final String UNIVERSITY_INPUT_PHONE = "university_phone";
    private static final String UNIVERSITY_INPUT_WEBSITE = "university_website";
    private static final String UNIVERSITY_INPUT_LOGO = "university_logo";

    private static final String RECORD_INFO = "record_info";
    private static final String RECORD_HISTORY_ID = "history_id";
    private static final String RECORD_RECORD_ID = "record_id";
    private static final String RECORD_CATEGORY_TYPE = "category_type";
    private static final String RECORD_STATUS = "status";
    private static final String RECORD_USER_ID = "user_id";
    private static final String RECORD_LIKES = "likes";
    private static final String RECORD_DISLIKES = "dislikes";
    private static final String RECORD_CONTENT = "content";


    private static final String ADD_RECORD_UNIVERSITY = "university_id";
    private static final String ADD_RECORD_CATEGORY = "category_type";
    private static final String ADD_RECORD_USER_ID = "user_id";
    private static final String ADD_RECORD_RECORD_ID = "record_id";
    private static final String ADD_RECORD_CONTENT = "content";

    public static String USER_NAME = "user_name";
    public static String USER_PASSWORD = "password";
    public static String USER_EMAIL = "user_email";


    public static List<University> parseUniversityList(String result) {
        int id;
        String uniName, uniAddress, uniPhone, uniLogoURL, uniWebsite;
        List<University> universities = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(MESSAGE);
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject objectUniversity = jsonArray.getJSONObject(i);
                id = objectUniversity.getInt(UNIVERSITY_ID);
                uniName = objectUniversity.getString(UNIVERSITY_NAME);
                uniAddress = objectUniversity.getString(UNIVERSITY_ADDRESS);
                uniPhone = objectUniversity.getString(UNIVERSITY_PHONE);
                uniLogoURL = objectUniversity.getString(UNIVERSITY_LOGO);
                uniWebsite = objectUniversity.getString(UNIVERSITY_WEBSITE);
                universities.add(new University(id, uniName, uniAddress, uniPhone, uniLogoURL, uniWebsite));
            }

        } catch (JSONException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return universities;
    }

    public static String parseLoginInformaiton(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(STATUS);
            if (status.equals(STATUS_SUCCESS)) {
                return STATUS_SUCCESS;
            } else {
                return jsonObject.getString(MESSAGE);
            }

        } catch (JSONException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return null;
    }

    public static String parseRegisterInformaiton(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(STATUS);
            if (status.equals(STATUS_SUCCESS)) {
                return STATUS_SUCCESS;
            } else {
                return jsonObject.getString(MESSAGE);
            }

        } catch (JSONException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return null;
    }

    public static List<Record> parseRecord(String result) {
        List<Record> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(STATUS);
            if (status.equals(STATUS_SUCCESS)) {
                JSONArray array = jsonObject.getJSONArray(MESSAGE);
                if (array != null) {
                    for (int i = 0, size = array.length(); i < size; i++) {
                        JSONObject object = array.getJSONObject(i);
                        if (object != null) {
                            JSONObject temp = object.getJSONObject(RECORD_INFO);
                            list.add(new Record(temp.getString(RECORD_HISTORY_ID),
                                    object.getString(RECORD_RECORD_ID),
                                    temp.getInt(RECORD_CATEGORY_TYPE),
                                    temp.getString(RECORD_STATUS),
                                    temp.getString(RECORD_USER_ID),
                                    temp.getInt(RECORD_LIKES),
                                    temp.getInt(RECORD_DISLIKES),
                                    temp.getString(RECORD_CONTENT)));

                        }
                    }
                }
            }
        } catch (JSONException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return list;
    }

    public static String parseAddUniversityInformaiton(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(STATUS);
            if (status.equals(STATUS_SUCCESS)) {
                return STATUS_SUCCESS;
            } else {
                return jsonObject.getString(MESSAGE);
            }

        } catch (JSONException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return null;
    }

    public static int parseUserId(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(STATUS);
            if (status.equals(STATUS_SUCCESS)) {
                JSONObject jsonMessage = jsonObject.getJSONObject(MESSAGE);
                return jsonMessage.getInt(USER_ID);
            } else {
                return 0;
            }
        } catch (JSONException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return 0;
    }

    public static String buildRegisterData(String username, String password) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(DataParseUtils.USER_NAME, username)
                .appendQueryParameter(DataParseUtils.USER_PASSWORD, password);
        return builder.build().getEncodedQuery();
    }

    public static String buildRegisterData(String username, String password, String email) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(DataParseUtils.USER_NAME, username)
                .appendQueryParameter(DataParseUtils.USER_PASSWORD, password)
                .appendQueryParameter(DataParseUtils.USER_EMAIL, email);
        return builder.build().getEncodedQuery();
    }

    public static String buildUniversityData(String name, String address, String phone, String website, String logo) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(DataParseUtils.UNIVERSITY_INPUT_NAME, name)
                .appendQueryParameter(DataParseUtils.UNIVERSITY_INPUT_ADDRESS, address)
                .appendQueryParameter(DataParseUtils.UNIVERSITY_INPUT_PHONE, phone)
                .appendQueryParameter(DataParseUtils.UNIVERSITY_INPUT_WEBSITE, website)
                .appendQueryParameter(DataParseUtils.UNIVERSITY_INPUT_LOGO, logo);
        return builder.build().getEncodedQuery();
    }

    public static String buildAddRecordData(int uniID, int categoryType, int userID, int mRecordID, String content) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(DataParseUtils.ADD_RECORD_UNIVERSITY, uniID + "")
                .appendQueryParameter(DataParseUtils.ADD_RECORD_CATEGORY, categoryType + "")
                .appendQueryParameter(DataParseUtils.ADD_RECORD_USER_ID, userID + "")
                .appendQueryParameter(DataParseUtils.ADD_RECORD_RECORD_ID, mRecordID + "")
                .appendQueryParameter(DataParseUtils.ADD_RECORD_CONTENT, content);
        return builder.build().getEncodedQuery();
    }
}
