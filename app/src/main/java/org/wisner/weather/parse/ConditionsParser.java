package org.wisner.weather.parse;

import android.support.annotation.VisibleForTesting;

import org.json.JSONException;
import org.json.JSONObject;
import org.wisner.weather.data.Conditions;

import java.io.InputStream;
import java.util.Scanner;

public class ConditionsParser {

    public static final String CURRENT_OBSERVATION = "current_observation";
    public static final String WEATHER = "weather";
    public static final String TEMPERATURE_STRING = "temperature_string";
    public static final String DISPLAY_LOCATION = "display_location";
    public static final String FULL = "full";

    public Conditions parse(InputStream jsonStream) throws ParseException {
        try {
            Conditions conditions = new Conditions();

            String jsonString = streamToString(jsonStream);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject currentObservations = jsonObject.getJSONObject(CURRENT_OBSERVATION);
            String summary = currentObservations.getString(WEATHER);
            conditions.setSummary(summary);

            String temp = currentObservations.getString(TEMPERATURE_STRING);
            conditions.setTempurature(temp);

            String city = currentObservations.getJSONObject(DISPLAY_LOCATION).getString(FULL);
            conditions.setCity(city);

            return conditions;
        } catch (JSONException e) {
            throw new ParseException(e);
        }
    }

    @VisibleForTesting
    String streamToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }
}
