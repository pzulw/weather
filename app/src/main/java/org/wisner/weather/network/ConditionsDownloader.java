package org.wisner.weather.network;

import android.os.AsyncTask;

import org.wisner.weather.data.Conditions;
import org.wisner.weather.parse.ConditionsParser;
import org.wisner.weather.parse.ParseException;

import java.io.InputStream;

/**
 */
public class ConditionsDownloader {

    private static final String CONDITIONS_ENDPOINT = "conditions/q/MA/Cambridge.json";
    public static final Conditions FAILED = null;

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

    private Conditions download() throws DownloadException, ParseException {
        WeatherDownloader weatherDownloader = new WeatherDownloader();
        InputStream jsonStream = weatherDownloader.download(CONDITIONS_ENDPOINT);
        ConditionsParser parser = new ConditionsParser();
        return parser.parse(jsonStream);
    }

}
