package com.camiss.supportstudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.camiss.supportstudent.Fragment.RecyclerViewFragment;
import com.camiss.supportstudent.Model.Record;
import com.camiss.supportstudent.Utils.Constant;
import com.camiss.supportstudent.Utils.PrefUtils;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.List;


public class ActivityListRecord extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private Toolbar toolbar;

    public static List<Record> listData = new ArrayList<>();

    public static List<Record> listData_type1 = new ArrayList<>();
    public static List<Record> listData_type2 = new ArrayList<>();
    public static List<Record> listData_type3 = new ArrayList<>();
    public static List<Record> listData_type4 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);
        setTitle("");
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();


        listData_type1 = new ArrayList<>();
        listData_type2 = new ArrayList<>();
        listData_type3 = new ArrayList<>();
        listData_type4 = new ArrayList<>();
        for (int i = 0,size = listData.size(); i < size; i++) {
            switch (listData.get(i).getCategoryType()) {
                case Constant.CATEGORY_BEFORE_ARRIVING:
                    listData_type1.add(listData.get(i));
                    break;
                case Constant.CATEGORY_AFTER_ARRIVING:
                    listData_type2.add(listData.get(i));
                    break;
                case Constant.CATEGORY_SCHOLARSHIP:
                    listData_type3.add(listData.get(i));
                    break;
                case Constant.CATEGORY_UNIVERSITY:
                    listData_type4.add(listData.get(i));
                    break;
                default:
                    listData_type4.add(listData.get(i));
                    break;
            }
        }
        Log.i("AnhHT11", "Size = " + listData_type1.size() + " " + listData_type2.size() + " "
                + listData_type3.size() + " " + listData_type4.size());

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


        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % Constant.NUMBER_OF_TYPE) {
                    case 0:
                        return new RecyclerViewFragment(listData_type4);
                    case 1:
                        return new RecyclerViewFragment(listData_type1);
                    case 2:
                        return new RecyclerViewFragment(listData_type2);
                    case 3:
                        return new RecyclerViewFragment(listData_type3);
                    default:
                        return new RecyclerViewFragment(listData_type4);
                }
            }

            @Override
            public int getCount() {
                return Constant.NUMBER_OF_TYPE;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % Constant.NUMBER_OF_TYPE) {
                    case 0:
                        return "University information";
                    case 1:
                        return "Before leaving";
                    case 2:
                        return "After arriving";
                    case 3:
                        return "Scholarship";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.green,
                                getResources().getDrawable(R.drawable.student1));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.blue,
                                getResources().getDrawable(R.drawable.student2));
                    //"http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.cyan,
                                getResources().getDrawable(R.drawable.student3));
                    //"http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.red,
                                getResources().getDrawable(R.drawable.student4));
                    //"http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = PrefUtils.getString(getApplicationContext(), PrefUtils.KEY_LOGIN_USERNAME, null);
                if (TextUtils.isEmpty(username)) {
                    Snackbar.make(view, getResources().getText(R.string.action_ask_for_sign_in), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.sign_in), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                            }).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ActivityAddNewRecord.class);
                    startActivity(intent);
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
