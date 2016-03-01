package com.camiss.supportstudent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.camiss.supportstudent.Model.University;
import com.camiss.supportstudent.Model.UniversityAdapter;
import com.camiss.supportstudent.Utils.ApiUtils;
import com.camiss.supportstudent.Utils.DataParseUtils;
import com.camiss.supportstudent.Utils.PrefUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityUniversityList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView rv;
    private static final int REQUEST_ADD_NEW = 1;
    private static final int REQUEST_LOGIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    Intent intent = new Intent(getApplicationContext(), ActivityAddNewUniversity.class);
                    startActivityForResult(intent, REQUEST_ADD_NEW);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager lnManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(lnManager);
        updateUniversity();
    }

    private void updateUniversity() {
        new GetUniversityList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO : query is the text from the search view
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO : newText is the text from the search view
                // (event triggered every time the text changes)
                if (newText.isEmpty()) {
                    ((UniversityAdapter) rv.getAdapter()).flushFilter();
                } else {
                    ((UniversityAdapter) rv.getAdapter()).setFilter(newText);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openTutorial();
            return true;
        }
        if (id == R.id.action_user) {
            String username = PrefUtils.getString(getApplicationContext(), PrefUtils.KEY_LOGIN_USERNAME, null);
            if (TextUtils.isEmpty(username)) {
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), R.string.logout_sucessfull, Toast.LENGTH_LONG).show();
                logoutUser();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTutorial() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUtils.WEBSITE));
        startActivity(browserIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aboutus) {
            Toast.makeText(getApplicationContext(), "Student of Complex System Design class - UNICAM", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_tutorial) {
            openTutorial();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class GetUniversityList extends AsyncTask<String, String, List<University>> {

        private final static String TAG = "GetListOfUniversity";

        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(ActivityUniversityList.this);
            progressDialog.setMessage(getString(R.string.message_loading));
            progressDialog.show();
        }

        @Override
        protected List<University> doInBackground(String... params) {
            String result = ApiUtils.getUniversityList();
            if (!TextUtils.isEmpty(result)) {
                return DataParseUtils.parseUniversityList(result);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<University> listUniversity) {
            super.onPostExecute(listUniversity);
            if (listUniversity != null && listUniversity.size() > 0) {
                Collections.sort(listUniversity, new CustomComparator());
                UniversityAdapter adapter = new UniversityAdapter(listUniversity, ActivityUniversityList.this);
                rv.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), R.string.message_can_not_get_information, Toast.LENGTH_LONG).show();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (ActivityUniversityList.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                    return;
                }
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NEW) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateUniversity();
            }
        }
    }

    public class CustomComparator implements Comparator<University> {
        @Override
        public int compare(University uni1, University uni2) {
            return uni1.getUniName().toLowerCase().compareTo(uni2.getUniName().toLowerCase());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean isKeepLoggedIn = PrefUtils.getBool(getApplicationContext(), PrefUtils.KEY_IS_KEEP_LOGGED_IN, false);
        if (!isKeepLoggedIn) {
            logoutUser();
        }
    }

    private void logoutUser() {
        PrefUtils.removeKey(getApplicationContext(), PrefUtils.KEY_IS_KEEP_LOGGED_IN);
        PrefUtils.removeKey(getApplicationContext(), PrefUtils.KEY_LOGIN_USERNAME);
        PrefUtils.removeKey(getApplicationContext(), PrefUtils.KEY_USER_ID);
    }
}
