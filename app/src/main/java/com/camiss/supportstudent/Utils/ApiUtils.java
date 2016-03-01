package com.camiss.supportstudent.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hatuananh on 1/12/16.
 */
public class ApiUtils {
    private static String BASE_URL = "http://10.0.0.52";
    private static int TIME_OUT = 10000;
    private static String TAG = ApiUtils.class.getSimpleName();
    private static String PORT = ":8888";
    public static final String WEBSITE = BASE_URL + PORT + "/CSD_CMS/public/tutorial";
    private static String EXTEND = "/my_api/api/";
    private static String API_URL = BASE_URL + PORT + EXTEND;
    private static String GET_UNIVERSITY_URL = API_URL + "getUniversities";
    private static String LOGIN_URL = API_URL + "userLogin";
    private static String ADD_RECORD_URL = API_URL + "addRecord";
    private static String REGISTER_URL = API_URL + "userRegister";
    private static String ADD_UNIVERSITY_URL = API_URL + "addUniversity";
    private static String GET_RECORD = API_URL + "getRecord/";

    private static String METHOD_GET = "GET";
    private static String METHOD_POST = "POST";
    private static String METHOD_PUT = "PUT";


    public static String getUniversityList() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GET_UNIVERSITY_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_GET);
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            int statusCode = connection.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == statusCode) {
                String responseData = convertInputStreamToString(connection.getInputStream());
                Log.d(TAG, "get university response data = " + responseData);
                return responseData;
            } else {
                String errorData = convertInputStreamToString(connection.getErrorStream());
                Log.w(TAG, "get university response data error = " + errorData);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Login function exception" + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String login(String message) {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            URL url = new URL(LOGIN_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_POST);
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(message);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();
            int statusCode = connection.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == statusCode) {
                String responseData = convertInputStreamToString(connection.getInputStream());
                Log.d(TAG, "login response data = " + responseData);
                return responseData;
            } else {
                Log.w(TAG, "error = " + statusCode);
                String errorData = convertInputStreamToString(connection.getErrorStream());
                Log.w(TAG, "login response data error = " + errorData);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Login function exception: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getRecord(String uniID) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GET_RECORD + uniID);
            Log.i("AnhHT11", "URL = " + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_GET);
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == statusCode) {
                String responseData = convertInputStreamToString(connection.getInputStream());
                Log.d(TAG, "Get record response data = " + responseData);
                return responseData;
            } else {
                Log.w(TAG, "error = " + statusCode);
                String errorData = convertInputStreamToString(connection.getErrorStream());
                Log.w(TAG, "Get record response data error = " + errorData);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Get record function exception: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String register(String message) {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            URL url = new URL(REGISTER_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_PUT);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(message);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();
            int statusCode = connection.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == statusCode) {
                String responseData = convertInputStreamToString(connection.getInputStream());
                Log.d(TAG, "Register response data = " + responseData);
                return responseData;
            } else {
                Log.w(TAG, "error = " + statusCode);
                String errorData = convertInputStreamToString(connection.getErrorStream());
                Log.w(TAG, "Register response data error = " + errorData);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Register function exception: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String addUniversity(String message) {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            URL url = new URL(ADD_UNIVERSITY_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_POST);
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(message);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();
            int statusCode = connection.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == statusCode) {
                String responseData = convertInputStreamToString(connection.getInputStream());
                Log.d(TAG, "Add university response data = " + responseData);
                return responseData;
            } else {
                Log.w(TAG, "error = " + statusCode);
                String errorData = convertInputStreamToString(connection.getErrorStream());
                Log.w(TAG, "Add university response data error = " + errorData);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Add university function exception: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String convertInputStreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder("");
        if (in != null) {
            InputStreamReader is = null;
            BufferedReader br = null;
            String line;
            try {
                is = new InputStreamReader(in, "UTF-8");
                br = new BufferedReader(is);
                line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                Log.e(TAG, "" + e.getMessage());
                return "";
            } finally {
                try {
                    if (br != null) br.close();
                    if (is != null) is.close();
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "" + e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    public static String addRecord(String message) {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            URL url = new URL(ADD_RECORD_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_POST);
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(message);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();
            int statusCode = connection.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == statusCode) {
                String responseData = convertInputStreamToString(connection.getInputStream());
                Log.d(TAG, "login response data = " + responseData);
                return responseData;
            } else {
                Log.w(TAG, "error = " + statusCode);
                String errorData = convertInputStreamToString(connection.getErrorStream());
                Log.w(TAG, "login response data error = " + errorData);
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Login function exception: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
