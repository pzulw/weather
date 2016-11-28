package org.wisner.weather;

import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wisner.weather.data.Conditions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class WeatherActivityTest {

    public static final String CITY = "Mos Islay";
    public static final String TEMPURATURE = "200C";
    public static final String SUMMARY = "Sunny";
    private WeatherActivity activity;

    @Rule
    public ActivityTestRule<WeatherActivity> activityTestRule
            = new ActivityTestRule<WeatherActivity>(WeatherActivity.class);


    @Before
    public void setup() {
        activity = activityTestRule.getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void testActivityStartup() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testSetWeather() {
        setExampleWeather();

        checkExampleWeather();
    }

    @Test
    public void testRotate() {
        setExampleWeather();

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        checkExampleWeather();
    }

    private void checkExampleWeather() {
        onView(withId(R.id.city)).check(matches(withText(CITY)));
        onView(withId(R.id.temperature)).check(matches(withText(TEMPURATURE)));
        onView(withId(R.id.summary)).check(matches(withText(SUMMARY)));
    }

    private void setExampleWeather() {
        Conditions conditions = new Conditions();
        conditions.setCity(CITY);
        conditions.setTempurature(TEMPURATURE);
        conditions.setSummary(SUMMARY);

        setWeather(conditions);
    }

    private void setWeather(final Conditions conditions) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setWeatherInUI(conditions);
            }
        });
    }
}
