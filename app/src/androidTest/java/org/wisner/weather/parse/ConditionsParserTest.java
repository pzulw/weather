package org.wisner.weather.parse;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.wisner.weather.data.Conditions;

import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ConditionsParserTest {

    @Test
    public void testParserReturnsConditions() throws Exception {
        ConditionsParser conditionsParser = new ConditionsParser();
        InputStream jsonData = getConditions();
        Conditions conditions = conditionsParser.parse(jsonData);
        assertNotNull(conditions);
    }

    @Test
    public void testParserFindsWeatherData() throws Exception {
        ConditionsParser conditionsParser = new ConditionsParser();
        InputStream jsonData = getConditions();
        Conditions conditions = conditionsParser.parse(jsonData);
        assertNotNull(conditions);

        assertEquals("Cambridge, MA", conditions.getCity());
        assertEquals("46.8 F (8.2 C)", conditions.getTempurature());

    }

    private InputStream getConditions() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        InputStream inputStream = context.getAssets().open("sample_conditions.json");
        return inputStream;
    }
}
