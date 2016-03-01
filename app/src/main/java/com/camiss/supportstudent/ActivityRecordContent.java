package com.camiss.supportstudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.camiss.supportstudent.Model.Record;
import com.camiss.supportstudent.Utils.Constant;
import com.camiss.supportstudent.Utils.PrefUtils;


public class ActivityRecordContent extends AppCompatActivity implements View.OnClickListener{

    public static String sUniversityName = "";
    public static Record sRecord;
    private TextView mTvLike;
    private TextView mTvDislike;
    private TextView mTvContent;
    private ImageView mImgUpdate;
    private TextView tvType;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
            toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24dp);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_24dp);
        }

        mTvContent = (TextView) findViewById(R.id.tvContent);
        mTvLike = (TextView) findViewById(R.id.textLabel);
        mTvDislike = (TextView) findViewById(R.id.textLabel2);
        mImgUpdate = (ImageView) findViewById(R.id.imageLabel3);
        tvType = (TextView) findViewById(R.id.tvType);
        mImgUpdate.setOnClickListener(this);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(sUniversityName);
        mTvContent.setText(Html.fromHtml(sRecord.getContent()));
        mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
        mTvLike.setText(sRecord.getLikes() + "");
        mTvDislike.setText(sRecord.getDislikes() + "");
        Log.i("AnhHT11", "ID = " + sRecord.getRecordID());
        tvType.setText(Constant.getTypeName(sRecord.getCategoryType(),getApplicationContext()));
//        if (data != null && !TextUtils.isEmpty(data.getStringExtra(Constant.userName))) {
//            collapsingToolbar.setTitle(data.getStringExtra(Constant.userName));
//        } else {
//            collapsingToolbar.setTitle(sUniversityName);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageLabel3:
                String username = PrefUtils.getString(getApplicationContext(), PrefUtils.KEY_LOGIN_USERNAME, null);
                if (TextUtils.isEmpty(username)) {
                    Snackbar.make(v, getResources().getText(R.string.action_ask_for_sign_in), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.sign_in), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                            }).show();
                } else {
                    Intent intent = new Intent(getBaseContext(), ActivityAddNewRecord.class);
                    intent.putExtra(Constant.RECORD_ID, Integer.parseInt(sRecord.getRecordID()));
                    intent.putExtra(Constant.RECORD_CATEGORY, sRecord.getCategoryType());
                    intent.putExtra(Constant.RECORD_CONTENT, sRecord.getContent());
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.toolbar:
                finish();
                break;
            default:
                break;
        }
    }
}
