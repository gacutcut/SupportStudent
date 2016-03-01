package com.camiss.supportstudent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camiss.supportstudent.Utils.ApiUtils;
import com.camiss.supportstudent.Utils.Constant;
import com.camiss.supportstudent.Utils.DataParseUtils;

/**
 * Created by hatuananh on 1/1/16.
 */
public class ActivityRegister extends Activity implements View.OnClickListener {

    private static final String TAG = ActivityRegister.class.getSimpleName();
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    private Button mRegister;
    private TextView mLogin;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        mName = (EditText) findViewById(R.id.registerUsername);
        mEmail = (EditText) findViewById(R.id.registerEmail);
        mPassword = (EditText) findViewById(R.id.registerPassword);
        mPasswordConfirm = (EditText) findViewById(R.id.registerConfirmPassword);
        mRegister = (Button) findViewById(R.id.btn_register);
        mLogin = (TextView) findViewById(R.id.link_signin_click);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                closeActivity();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.link_signin_click:
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
                closeActivity();
                break;
            default:
                break;
        }
        Log.d(TAG, "Click on ID: " + v.getId());
        return;
    }

    private void register() {
        if (!validate()) {
            onRegisterfail(null);
            return;
        }
        String username = mName.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();
        new RegisterToSystem().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, username, password, email);

    }

    private void onRegisterSuccess(String message) {
        Toast.makeText(getApplicationContext(), getString(R.string.successful), Toast.LENGTH_LONG).show();
        Intent data = new Intent();
        data.putExtra(Constant.userName, mName.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    private void onRegisterfail(String message) {
        Toast.makeText(getApplicationContext(), "Can not register", Toast.LENGTH_LONG).show();
    }

    private boolean validate() {
        boolean valid = true;
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String password_confirm = mPasswordConfirm.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mName.setError(getString(R.string.name_have_more_than_three_character));
            valid = false;
        } else {
            mName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError(getResources().getString(R.string.enter_valid_email));
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError(getString(R.string.not_less_than_six));
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (password_confirm.isEmpty() || password_confirm.length() < 6) {
            mPasswordConfirm.setError(getString(R.string.not_less_than_six));
            valid = false;
        } else {


            mPasswordConfirm.setError(null);
        }
        if (!password.equals(password_confirm)) {
            mPasswordConfirm.setError(getString(R.string.password_not_match));
            valid = false;
        } else {
            mPasswordConfirm.setError(null);
        }
        return valid;
    }

    private void closeActivity() {
        finish();
    }

    class RegisterToSystem extends AsyncTask<String, String, String> {

        private final static String TAG = "LoginToSystem";

        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(ActivityRegister.this,
                    ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.message_authenticating));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String mess = DataParseUtils.buildRegisterData(params[0], params[1], params[2]);
            return ApiUtils.register(mess);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "return message: " + s);
            if (TextUtils.isEmpty(s)) {
                onRegisterfail(null);
            } else {
                String result = DataParseUtils.parseRegisterInformaiton(s);
                if (result.equals(DataParseUtils.STATUS_SUCCESS)) {
                    onRegisterSuccess(s);
                } else {
                    onRegisterfail(result);
                }
            }
            progressDialog.dismiss();
        }
    }
}

