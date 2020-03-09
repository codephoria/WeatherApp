package io.github.liziscoding.weatherapp;

import android.os.AsyncTask;

import java.util.ArrayList;

public class ParseForecastData  extends AsyncTask<String, Void, ArrayList<ArrayList<String>>>{
    private static final String TAG = "ParseForecastData";
    private final MainActivity mCallback;

        public ParseForecastData(MainActivity callback) {
            mCallback = callback;
        }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... jsonString) {

            ArrayList<String> testdate = new ArrayList<>();
            testdate.add("JAN 1");
            testdate.add("JAN 2");
        ArrayList<String> testtime = new ArrayList<>();
        testtime.add("8pm");
        testtime.add("10pm");
            ArrayList<String> testtemp = new ArrayList<>();
            testtemp.add("4");
            testtemp.add("80");
            ArrayList<String> testdescr = new ArrayList<>();
            testdescr.add("Rain");
            testdescr.add("Sunny");


            ArrayList<ArrayList<String>> result = new ArrayList<>();
            result.add(testdate);
            result.add(testtime);
            result.add(testtemp);
            result.add(testdescr);

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> strings) {
        super.onPostExecute(strings);
        mCallback.setCards(strings);
    }
}
