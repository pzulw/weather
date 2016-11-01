package org.wisner.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class PickCityActivity extends Activity {

    public static final String EXTRA_CITY = "org.wisner.weather.CITY";
    public static final String EXTRA_STATE = "org.wisner.weather.STATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_city_layout);

        Button button = (Button)findViewById(R.id.pick_city_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    private void returnResult() {
        EditText city = (EditText)findViewById(R.id.city);
        EditText state = (EditText)findViewById(R.id.state);
        String cityName = city.getText().toString();
        String stateCode = state.getText().toString().toUpperCase();

        Intent results = new Intent();
        results.putExtra(EXTRA_CITY, cityName);
        results.putExtra(EXTRA_STATE, stateCode);

        setResult(RESULT_OK, results);
        finish();
    }
}
