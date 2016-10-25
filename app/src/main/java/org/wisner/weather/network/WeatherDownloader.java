package org.wisner.weather.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Generic class for various Weather Underground endpoints.
 */
public class WeatherDownloader {
    public static final String WUAPI = "http://api.wunderground.com/api/cc9bc4a2b821de8c/";

    public InputStream download(String endpoint) throws DownloadException {
        try {
            URL url = new URL(WUAPI + endpoint);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            return connection.getInputStream();

        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
