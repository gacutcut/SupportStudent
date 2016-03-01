package com.camiss.supportstudent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camiss.supportstudent.Utils.ApiUtils;
import com.camiss.supportstudent.Utils.Constant;
import com.camiss.supportstudent.Utils.DataParseUtils;
import com.camiss.supportstudent.Utils.PrefUtils;

/**
 * Created by hatuananh on 1/1/16.
 */
public class ActivityLogin extends Activity implements View.OnClickListener {

    private static final String TAG = ActivityLogin.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;
    private Button mBtnClose;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private TextView mRegister;
    private SwitchCompat mIsKeepLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        mBtnClose = (Button) findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(this);
        mRegister = (TextView) findViewById(R.id.link_register_click);
        mPassword = (EditText) findViewById(R.id.loginPassword);
        mUsername = (EditText) findViewById(R.id.loginUsername);
        mIsKeepLogin = (SwitchCompat) findViewById(R.id.swIsKeepLogin);
        mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.link_register_click:
                Intent intent = new Intent(getApplicationContext(), ActivityRegister.class);
                startActivityForResult(intent, REQUEST_SIGNUP);

            default:
                break;
        }
    }

    private void login() {
        Log.d(TAG, "Start login");
        if (!validate()) {
            return;
        }
        mLogin.setEnabled(false);
        final String email = mUsername.getText().toString();
        final String password = mPassword.getText().toString();
        new LoginToSystem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, email, password);
    }

    private void onLoginSuccess(String result) {
        Toast.makeText(getApplicationContext(), R.string.login_successful, Toast.LENGTH_LONG).show();
        PrefUtils.saveString(getApplicationContext(), PrefUtils.KEY_LOGIN_USERNAME, mUsername.getText().toString());
        PrefUtils.saveInt(getApplicationContext(), PrefUtils.KEY_USER_ID, DataParseUtils.parseUserId(result));
        if (mIsKeepLogin.isChecked()) {
            PrefUtils.saveBool(getApplicationContext(), PrefUtils.KEY_IS_KEEP_LOGGED_IN, true);
        }
        setResult(RESULT_OK, null);
        mLogin.setEnabled(true);
        finish();
    }

    private void onLoginFailed(String message) {
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }

        mLogin.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;
        String email = mUsername.getText().toString();
        String password = mPassword.getText().toString();

//        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            mUsername.setError(getString(R.string.enter_valid_email));
//            valid = false;
//        } else {
//            mUsername.setError(null);
//        }
        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError(getString(R.string.not_less_than_six));
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    class LoginToSystem extends AsyncTask<String, String, String> {

        private final static String TAG = "LoginToSystem";

        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(ActivityLogin.this,
                    ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.message_authenticating));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String mess = DataParseUtils.buildRegisterData(params[0], params[1]);
            return ApiUtils.login(mess);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK && data != null) {
                String result = data.getStringExtra(Constant.userName);
                if (!TextUtils.isEmpty(result)) {
                    mUsername.setText(result);
                    mUsername.setSelection(mUsername.getText().length());
                }
            }
        }
    }
}
