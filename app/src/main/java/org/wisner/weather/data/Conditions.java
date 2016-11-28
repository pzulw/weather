package org.wisner.weather.data;

import android.content.Intent;

import org.wisner.weather.WeatherDownloadService;

public class Conditions {
    private String summary;
    private String city;
    private String tempurature;
    private long timestamp;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTempurature() {
        return tempurature;
    }

    public void setTempurature(String tempurature) {
        this.tempurature = tempurature;
    }

    public static Conditions fromIntent(Intent intent) {
        Conditions conditions = new Conditions();
        conditions.setCity(intent.getStringExtra(WeatherDownloadService.EXTRA_CITY));
        conditions.setSummary(intent.getStringExtra(WeatherDownloadService.EXTRA_SUMMARY));
        conditions.setTempurature(intent.getStringExtra(WeatherDownloadService.EXTRA_TEMPURATURE));
        return conditions;
    }
}
