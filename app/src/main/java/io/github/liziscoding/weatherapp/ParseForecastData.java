package io.github.liziscoding.weatherapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseForecastData  extends AsyncTask<String, Void, ArrayList<ArrayList<String>>>{
    private static final String TAG = "ParseForecastData";
    private final MainActivity mCallback;

        public ParseForecastData(MainActivity callback) {
            mCallback = callback;
        }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... jsonString) {
        ArrayList<String> temperatures = new ArrayList<>();
        ArrayList<String> weatherDescriptions = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> parsedTime = new ArrayList<>();
        ArrayList<ArrayList<String>> parsedResult = new ArrayList<ArrayList<String>>();


        try {
            JSONArray rawWeatherDataArray = new JSONObject(jsonString[0]).getJSONArray("list");
            for (int i = 0; i < rawWeatherDataArray.length(); i++) {
                JSONObject oneForecastItem = rawWeatherDataArray.getJSONObject(i);
                String temperature = oneForecastItem.getJSONObject("main").getString("temp");
                temperatures.add(temperature);
                String weatherDescription = oneForecastItem.getJSONArray("weather").getJSONObject(0).getString("main");
                weatherDescriptions.add(weatherDescription);
                String dateTime = oneForecastItem.getString("dt_txt");
                String[] splitDateTime = dateTime.split(" ");  // ["2020-03-10","21:00:00"]
                String[] separatedDate = splitDateTime[0].split("-"); // ["2020","03","10"]
                String month = separatedDate[1];
                switch (month){
                    case "01":
                        month = "JAN";
                        break;
                    case "02":
                        month = "FEB";
                        break;
                    case "03":
                        month = "MAR";
                        break;
                    case "04":
                        month = "APR";
                        break;
                    case "05":
                        month = "MAY";
                        break;
                    case "06":
                        month = "JUN";
                        break;
                    case "07":
                        month = "JUL";
                        break;
                    case "08":
                        month = "AUG";
                        break;
                    case "09":
                        month = "SEP";
                        break;
                    case "10":
                        month = "OCT";
                        break;
                    case "11":
                        month = "NOV";
                        break;
                    case "12":
                        month = "DEC";
                        break;
                }

                String day = separatedDate[2];

                if (Character.toString(day.charAt(0)).equalsIgnoreCase("0")){
                    day = day.substring(1);
                }
                date.add(month + " " + day);


                String[] splitTimes = splitDateTime[1].split(":"); // ["21","00","00"]
                String ampm = "AM";
                String newTime = "";
                int timeAsNumber = Integer.parseInt(splitTimes[0]);
                if (timeAsNumber == 0){
                    newTime = "12";
                    ampm = "AM";
                } else if (timeAsNumber < 12){
                    newTime = String.valueOf(timeAsNumber);
                    ampm = "AM";
                } else {
                    if (timeAsNumber != 12){
                        newTime = String.valueOf(timeAsNumber-12);

                    } else {
                        newTime = String.valueOf(timeAsNumber);
                    }
                    ampm = "PM";

                }
                parsedTime.add(newTime + ampm);
            }

            parsedResult.add(date);
            parsedResult.add(parsedTime);
            parsedResult.add(temperatures);
            parsedResult.add(weatherDescriptions);
            return parsedResult;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> strings) {
        super.onPostExecute(strings);
        mCallback.setCards(strings);
    }
}
