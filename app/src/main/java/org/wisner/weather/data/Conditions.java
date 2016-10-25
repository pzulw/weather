package org.wisner.weather.data;

public class Conditions {
    private String summary;
    private String city;
    private String tempurature;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
