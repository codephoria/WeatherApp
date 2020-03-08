package io.github.liziscoding.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetWeatherData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetWeatherData";
    private final MainActivity mCallback;

    public GetWeatherData(MainActivity callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch (FileNotFoundException e){
            Log.e(TAG, "================Something wrong in GetWeatherData================");
            e.printStackTrace();
            mCallback.setPossibleErrors(1);
        } catch (Exception e){
            Log.e(TAG, "================Something wrong in GetWeatherData================");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ParseWeatherData parseWeatherData = new ParseWeatherData(mCallback);
        parseWeatherData.execute(s);
    }
}
