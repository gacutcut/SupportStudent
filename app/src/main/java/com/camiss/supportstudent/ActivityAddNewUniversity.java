package com.camiss.supportstudent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.camiss.supportstudent.Utils.ApiUtils;
import com.camiss.supportstudent.Utils.DataParseUtils;
import com.camiss.supportstudent.Utils.PrefUtils;

/**
 * Created by hatuananh on 1/14/16.
 */
public class ActivityAddNewUniversity extends Activity implements View.OnClickListener {
    private static final String TAG = ActivityAddNewUniversity.class.getSimpleName();
    private Button mClose, mSave;
    private EditText mName, mLogo, mWebsite, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_new_university);
        mClose = (Button) findViewById(R.id.btnClose);
        mSave = (Button) findViewById(R.id.btnSave);
        mName = (EditText) findViewById(R.id.edtUniName);
        mLogo = (EditText) findViewById(R.id.edtUniLogo);
        mWebsite = (EditText) findViewById(R.id.edtUniWebsite);
        mPhone = (EditText) findViewById(R.id.edtUniPhone);
        mClose.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnSave:
                addNewUniversity();
                break;
            default:
                break;
        }
    }

    private void addNewUniversity() {
        Log.d(TAG, "Start add new ");

        if (!validate()) {
            return;
        }
        mSave.setEnabled(false);
        final String name = mName.getText().toString();
        final String phone = mPhone.getText().toString();
        final String website = mWebsite.getText().toString();
        final String logo = mLogo.getText().toString();
        new AddNewUniversity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, name, phone, website, logo);
    }

    private boolean validate() {
        boolean valid = true;
        final String name = mName.getText().toString();
        final String website = mWebsite.getText().toString();
        final String phone = mPhone.getText().toString();
        final String logo = mLogo.getText().toString();

        //check for name
        if (name.isEmpty()) {
            mName.setError(getString(R.string.can_not_empty));
            valid = false;
        } else {
            mName.setError(null);
        }
        if (website.isEmpty() || !Patterns.WEB_URL.matcher(website).matches()) {
            mWebsite.setError(getString(R.string.enter_valid_url));
            valid = false;
        } else {
            mWebsite.setError(null);
        }
        if (logo.isEmpty() || !Patterns.WEB_URL.matcher(logo).matches()) {
            mLogo.setError(getString(R.string.enter_valid_url));
            valid = false;
        } else {
            mLogo.setError(null);
        }
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError(getString(R.string.enter_valid_phone_number));
            valid = false;
        } else {
            mPhone.setError(null);
        }
        return valid;
    }

    class AddNewUniversity extends AsyncTask<String, String, String> {

        private final static String TAG = "LoginToSystem";

        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(ActivityAddNewUniversity.this,
                    ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.message_authenticating));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String mess = DataParseUtils.buildUniversityData(params[0], "empty", params[1], params[2], params[3]);
            return ApiUtils.addUniversity(mess);
        }

        @Override
        protected void onPostExecute(String returnMessage) {
            super.onPostExecute(returnMessage);
            Log.d(TAG, "return message: " + returnMessage);
            if (TextUtils.isEmpty(returnMessage)) {
                onAddFailed(null);
            } else {
                String result = DataParseUtils.parseLoginInformaiton(returnMessage);
                if (result.equals(DataParseUtils.STATUS_SUCCESS)) {
                    onAddSuccess(returnMessage);
                } else {
                    onAddFailed(result);
                }
            }
            progressDialog.dismiss();
        }
    }

    private void onAddSuccess(String message) {
        Toast.makeText(getApplicationContext(), R.string.successful, Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, null);
        mSave.setEnabled(true);
        finish();
    }

    private void onAddFailed(String message) {
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getBaseContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }

        mSave.setEnabled(true);
    }
}
