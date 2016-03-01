package com.camiss.supportstudent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camiss.supportstudent.Model.Record;
import com.camiss.supportstudent.Utils.ApiUtils;
import com.camiss.supportstudent.Utils.Constant;
import com.camiss.supportstudent.Utils.DataParseUtils;
import com.camiss.supportstudent.Utils.PrefUtils;

/**
 * Created by hatuananh on 2/21/16.
 */
public class ActivityAddNewRecord extends Activity implements View.OnClickListener {

    private static final String TAG = ActivityAddNewRecord.class.getSimpleName();
    private Button mButtonClose;
    private Button mButtonSave;
    private Record mSave;
    private EditText mEdtRecordContent;
    private TextView mEdtUniName;
    private TextView mTvTitle;
    private AppCompatSpinner mSpin;
    private int mRecordID = 0;
    private int mCategoryID = 0;
    private String mRecordContent = Constant.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_new_record);
        mButtonClose = (Button) findViewById(R.id.btnClose);
        mButtonSave = (Button) findViewById(R.id.btnSave);
        mEdtRecordContent = (EditText) findViewById(R.id.edtRecordContent);
        mSpin = (AppCompatSpinner) findViewById(R.id.spinType);
        mEdtUniName = (TextView) findViewById(R.id.tvUniName);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mButtonSave.setOnClickListener(this);
        mButtonClose.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            mRecordID = intent.getIntExtra(Constant.RECORD_ID, 0);
            mCategoryID = intent.getIntExtra(Constant.RECORD_CATEGORY, 0);
            mRecordContent = intent.getStringExtra(Constant.RECORD_CONTENT);
        }
        if (!TextUtils.isEmpty(mRecordContent)) {
            mTvTitle.setText(R.string.update_information);
            mEdtRecordContent.setText(mRecordContent);
        }
        String uniname = PrefUtils.getString(getApplicationContext(), PrefUtils.KEY_UNIVERSITY_NAME, Constant.EMPTY);
        if (mEdtUniName != null && !TextUtils.isEmpty(uniname)) {
            mEdtUniName.setText(uniname);
            mEdtUniName.setTextColor(Color.BLACK);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnSave:
                saveInformation();
                break;
            default:
                break;
        }
    }

    private void saveInformation() {
        Log.d(TAG, "Start save Information");
        if (!validate()) {
            return;
        }
        mButtonSave.setEnabled(false);
        final String content = mEdtRecordContent.getText().toString();
        new UpdateToSystem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR
                , content, mSpin.getSelectedItemPosition() + "");
    }

    private boolean validate() {
        boolean valid = true;
        String content = mEdtRecordContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mEdtRecordContent.setError(getString(R.string.can_not_empty));
            valid = false;
        } else {
            mEdtRecordContent.setError(null);
        }
        return valid;
    }

    class UpdateToSystem extends AsyncTask<String, String, String> {

        private final static String TAG = "LoginToSystem";

        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(ActivityAddNewRecord.this,
                    ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.message_saving));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int userID = PrefUtils.getInt(getApplicationContext(), PrefUtils.KEY_USER_ID, 0);
            int uniID = PrefUtils.getInt(getApplicationContext(), PrefUtils.KEY_UNIVERSITY_ID, 0);
            int categoryType;
            if (mCategoryID == 0) {
                categoryType = Integer.parseInt(params[1]) + 1;
            } else {
                categoryType = mCategoryID;
            }
            String mess = DataParseUtils.buildAddRecordData(uniID, categoryType, userID, mRecordID, params[0]);
            return ApiUtils.addRecord(mess);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "return message: " + s);
            if (TextUtils.isEmpty(s)) {
                onLoginFailed(null);
            } else {
                String result = DataParseUtils.parseLoginInformaiton(s);
                if (result.equals(DataParseUtils.STATUS_SUCCESS)) {
                    onLoginSuccess(s);
                } else {
                    onLoginFailed(result);
                }
            }
            progressDialog.dismiss();
        }

        private void onLoginSuccess(String result) {
            Toast.makeText(getApplicationContext(), R.string.save_record_successful, Toast.LENGTH_LONG).show();
            mButtonSave.setEnabled(true);
            finish();
        }

        private void onLoginFailed(String message) {
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(getBaseContext(), R.string.save_record_failed, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
            mButtonSave.setEnabled(true);
        }
    }
}
