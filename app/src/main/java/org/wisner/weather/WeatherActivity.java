package org.wisner.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.wisner.weather.data.Conditions;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_CITY = 777;
    private static final int PICK_BACKGROUND = 11;
    public static final String CITY = "CITY";
    public static final String SUMMARY = "SUMMARY";
    public static final String TEMPURATURE = "TEMPURATURE";
    public static final IntentFilter WEATHER_BROADCAST_FILTER = new IntentFilter(WeatherDownloadService.BROADCAST_WEATHER_CONDITIONS);
    private Conditions conditions;
    private BroadcastReceiver weatherUpdateBroadcastReceiver;
    private WeatherUpdateRequester weatherUpdateRequester;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCityPicker();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        uiHandler = new Handler(Looper.myLooper());
        weatherUpdateRequester = new WeatherUpdateRequester(this);
        weatherUpdateBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Conditions conditions = Conditions.fromIntent(intent);
                setWeatherInUI(conditions);
            }
        };
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.conditions = new Conditions();
        conditions.setCity(savedInstanceState.getString(CITY));
        conditions.setSummary(savedInstanceState.getString(SUMMARY));
        conditions.setTempurature(savedInstanceState.getString(TEMPURATURE));
        setWeatherInUI(conditions);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (conditions == null) {
            return;
        }
        outState.putString(CITY, conditions.getCity());
        outState.putString(SUMMARY, conditions.getSummary());
        outState.putString(TEMPURATURE, conditions.getTempurature());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(weatherUpdateBroadcastReceiver, WEATHER_BROADCAST_FILTER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(weatherUpdateBroadcastReceiver);
    }

    private void showCityPicker() {
        startActivityForResult(new Intent(this, PickCityActivity.class), PICK_CITY);
    }

    private void showCurrentWeather(Intent cityInfo) {
        String city = cityInfo.getStringExtra(PickCityActivity.EXTRA_CITY);
        String state = cityInfo.getStringExtra(PickCityActivity.EXTRA_STATE);
        showCurrentWeather(city, state);
    }

    private void showCurrentWeather(final String city, final String stateCode) {
        weatherUpdateRequester.requestUpdateWeatherIntent(city, stateCode);
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCurrentWeather(city, stateCode);
            }
        }, 2000);
    }

    @VisibleForTesting
    void setWeatherInUI(Conditions conditions) {
        this.conditions = conditions;
        setText(R.id.city, conditions.getCity());
        setText(R.id.summary, conditions.getSummary());
        setText(R.id.temperature, conditions.getTempurature());
    }

    private void setText(int id, String value) {
        TextView view = (TextView) findViewById(id);
        final Animation quickSpin = AnimationUtils.loadAnimation(this, R.anim.spin);
        view.setText(value);
        view.startAnimation(quickSpin);
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
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_pick_background:
                pickImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_BACKGROUND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_CITY:
                showCurrentWeather(data);
                break;
            case PICK_BACKGROUND:
                setBackground(data.getData());
                break;
        }
    }


    private void setBackground(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Drawable image = Drawable.createFromStream(inputStream, uri.getLastPathSegment());
            View weatherView = findViewById(R.id.content_weather);
            weatherView.setBackground(image);
            Toast.makeText(this, "You picked an image!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            showError(R.string.error_cant_show_image);
        }
    }

    private void showError(int message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
