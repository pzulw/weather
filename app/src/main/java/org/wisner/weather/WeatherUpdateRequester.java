package org.wisner.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class WeatherUpdateRequester {
    private final Context context;
    private AlarmManager alarmManager;
    private static final int ALARM_REQUEST_CODE = 19; // arbitrary value
    private PendingIntent pendingIntent;

    public WeatherUpdateRequester(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @NonNull
    public void requestUpdateWeatherIntent(String city, String stateCode) {
        Intent intent = getWeatherUpdateIntent(city, stateCode);
        context.startService(intent);
    }

    @NonNull
    private Intent getWeatherUpdateIntent(String city, String stateCode) {
        Intent intent = new Intent(context, WeatherDownloadService.class);
        intent.setAction(WeatherDownloadService.ACTION_UPDATE_WEATHER);
        intent.putExtra(WeatherDownloadService.EXTRA_CITY, city);
        intent.putExtra(WeatherDownloadService.EXTRA_STATE_CODE, stateCode);
        return intent;
    }

    public void scheduleRepeatingUpdate(String city, String stateCode, long interval) {

        cancelRepeating();

        Intent intent = getWeatherUpdateIntent(city, stateCode);
        pendingIntent = PendingIntent.getService(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, 0, interval, pendingIntent);
    }

    public void cancelRepeating() {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent = null;
        }
    }
}
