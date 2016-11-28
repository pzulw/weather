package org.wisner.weather;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wisner.weather.data.Conditions;
import org.wisner.weather.network.ConditionsDownloader;
import org.wisner.weather.network.DownloadException;
import org.wisner.weather.parse.ParseException;

/**
 * A service to download the latest weather conditions in the background.
 */
public class WeatherDownloadService extends IntentService {

    private final static String TAG = WeatherDownloadService.class.getSimpleName();

    public static final String ORG_WISNER_WEATHER = "org.wisner.weather.";
    public final static String ACTION_UPDATE_WEATHER = ORG_WISNER_WEATHER + "action.UPDATE_WEATHER";
    public static final String EXTRA_CITY = ORG_WISNER_WEATHER + "extra.CITY";
    public static final String EXTRA_STATE_CODE = ORG_WISNER_WEATHER + "extra.STATE_CODE";
    public static final String EXTRA_TEMPURATURE = ORG_WISNER_WEATHER + "extra.TEMPURATURE";
    public static final String EXTRA_SUMMARY = ORG_WISNER_WEATHER + "extra.SUMARY";
    public static final String EXTRA_TIMESTAMP = ORG_WISNER_WEATHER + "extra.TIMESTAMP";

    public static final String BROADCAST_WEATHER_CONDITIONS = ORG_WISNER_WEATHER + "WEATHER_CONDITIONS";
    public static final String BROADCAST_WEATHER_FAILED = ORG_WISNER_WEATHER + "WEATHER_FAILED";

    public WeatherDownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (ACTION_UPDATE_WEATHER.equals(action)) {
            String stateCode = intent.getStringExtra(EXTRA_STATE_CODE);
            String city = intent.getStringExtra(EXTRA_CITY);
            updateWeather(city, stateCode);
        }
    }

    private void updateWeather(String city, String stateCode) {
        try {
            ConditionsDownloader downloader = new ConditionsDownloader(city, stateCode);
            Conditions conditions = downloader.download();
            broadcastResult(conditions);
        } catch (DownloadException | ParseException e) {
            broadcastError(e);
        }
    }

    private void broadcastResult(Conditions conditions) {
        Intent intent = new Intent(BROADCAST_WEATHER_CONDITIONS);
        intent.putExtra(EXTRA_CITY, conditions.getCity());
        intent.putExtra(EXTRA_SUMMARY, conditions.getSummary());
        intent.putExtra(EXTRA_TEMPURATURE, conditions.getTempurature());
        intent.putExtra(EXTRA_TIMESTAMP, System.currentTimeMillis());
        sendBroadcast(intent);
    }

    private void broadcastError(Exception e) {
        Intent intent = new Intent(BROADCAST_WEATHER_FAILED);
        sendBroadcast(intent);
    }


}
