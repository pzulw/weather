package org.wisner.weather.network;

import android.os.AsyncTask;

import org.wisner.weather.data.Conditions;
import org.wisner.weather.parse.ConditionsParser;
import org.wisner.weather.parse.ParseException;

import java.io.InputStream;

/**
 */
public class ConditionsDownloader {

    private static final String CONDITIONS_ENDPOINT = "conditions/q/%s/%s.json";
    public static final Conditions FAILED = null;

    final private String city;
    final private String stateCode;

    public ConditionsDownloader(String city, String stateCode) {
        this.city = city;
        this.stateCode = stateCode;
    }

    public interface DownloadReceiver {
        void onReceived(Conditions condition);
    }

    public void requestDownload(final DownloadReceiver downloadReceiver) {
        new AsyncTask<Void, Void, Conditions>() {
            @Override
            protected Conditions doInBackground(Void... params) {
                try {
                    return download();
                } catch (DownloadException | ParseException e) {
                    return FAILED;
                }
            }

            @Override
            protected void onPostExecute(Conditions conditions) {
                // runs on UI thread
                super.onPostExecute(conditions);
                downloadReceiver.onReceived(conditions);
            }
        }.execute();
    }

    public Conditions download() throws DownloadException, ParseException {
        WeatherDownloader weatherDownloader = new WeatherDownloader();

        String conditionsEndpoint = String.format(CONDITIONS_ENDPOINT, stateCode, city);
        InputStream jsonStream = weatherDownloader.download(conditionsEndpoint);
        ConditionsParser parser = new ConditionsParser();
        return parser.parse(jsonStream);
    }

}
