package io.github.liziscoding.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

public class ParseWeatherData extends AsyncTask<String, Void, ArrayList<String>> {
    private static final String TAG = "ParseWeatherData";
    private final MainActivity mCallback;
    private ArrayList<String> tempAndDescription = null;

    public ParseWeatherData(MainActivity callback) {
        mCallback = callback;
    }

    @Override
    protected ArrayList<String> doInBackground(String... jsonString) {

        try {
            JSONObject rawWeatherData = new JSONObject(jsonString[0]);
            String temperature = rawWeatherData.getJSONObject("main").getString("temp");
            String description = rawWeatherData.getJSONArray("weather").getJSONObject(0).getString("description");
            tempAndDescription = new ArrayList<>();
            tempAndDescription.add(temperature);
            tempAndDescription.add(description);
        } catch (Exception e) {
            Log.e(TAG, "================= Something went wrong while parsing JSON===================");
            e.printStackTrace();
            return null;
        }
        return tempAndDescription;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        mCallback.setWeather(strings);
    }
}
