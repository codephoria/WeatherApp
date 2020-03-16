package io.github.liziscoding.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ConstraintLayout weatherLayout;
    EditText cityNameEditText;
    TextView cityNameWeatherView;
    TextView degreesTextView;
    TextView weatherDescriptionTextView;
    private static final String apiKey = new GetKey().getApiKey();
    String enteredCity = "";
    boolean tempInFahrenheit = false;
    int currentTempInCelsius = 0;
    int currentTempInFahrenheit = 0;
    boolean temperatureShown = false;
    int possibleErrors = 0; // 1 = filenotfound
    LinearLayout forecastLayout;
    TextView forecastCityTextView;
    RecyclerView forecastCardsRecyclerView;
    ArrayList<String> cardTempCelsius;
    ArrayList<String> cardTempFahrenheit;
    boolean modeIsWeatherForecast = false;
    ConstraintLayout infoLayout;
    TextView infoText;


    ArrayList<String> cardDate;
    ArrayList<String> cardTime;
    ArrayList<String> cardTempKelvin;
    ArrayList<String> cardDescription;
    ArrayList<String> cardTemp;

    InputMethodManager mgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.birds);
        weatherLayout = findViewById(R.id.weatherLayout);
        cityNameEditText = findViewById(R.id.cityNameEditText);
        cityNameWeatherView = findViewById(R.id.cityNameWeatherView);
        degreesTextView = findViewById(R.id.degrees);
        weatherDescriptionTextView = findViewById(R.id.weatherDescription);
        forecastLayout = findViewById(R.id.forecastLayout);
        forecastCityTextView = findViewById(R.id.forecastCityTextView);
        forecastCardsRecyclerView = findViewById(R.id.forecastCardView);
        infoLayout = findViewById(R.id.infoLayout);
        infoText = findViewById(R.id.infoText);
    }

    public void getWeather(View view){
        modeIsWeatherForecast = false;
        try {
            enteredCity = cityNameEditText.getText().toString();
            if (enteredCity.length() > 0){
                cityNameWeatherView.setText(enteredCity.toUpperCase());
                String encodedCityName = URLEncoder.encode(enteredCity, "UTF-8");
                String searchUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&APPID=" + apiKey;

                mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(cityNameEditText.getWindowToken(), 0);

                GetWeatherData getWeatherData = new GetWeatherData(this);
                getWeatherData.execute(searchUrl);
            }

            forecastLayout.setVisibility(View.INVISIBLE);
            weatherLayout.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.INVISIBLE);
        } catch (Exception e){
            e.printStackTrace();
            infoText.setText("There was a problem. Please try again.");
            forecastLayout.setVisibility(View.INVISIBLE);
            weatherLayout.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
        }

    }

    public void getForecast(View view){
        infoText.setText("Loading...");
        forecastLayout.setVisibility(View.INVISIBLE);
        weatherLayout.setVisibility(View.INVISIBLE);
        infoLayout.setVisibility(View.VISIBLE);
        modeIsWeatherForecast = true;
        enteredCity = cityNameEditText.getText().toString();
        try {
            if (enteredCity.length() > 0){
                forecastCityTextView.setText(enteredCity.toUpperCase());
                String encodedCityName = URLEncoder.encode(enteredCity, "UTF-8");
                String searchUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + encodedCityName + "&APPID=" + apiKey;
                mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(cityNameEditText.getWindowToken(), 0);
                GetForecastData getForecastData = new GetForecastData(this);
                getForecastData.execute(searchUrl);
            }
        } catch (Exception e){
            e.printStackTrace();
            infoText.setText("There was a problem. Please try again.");
            forecastLayout.setVisibility(View.INVISIBLE);
            weatherLayout.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
        }

    }

    public void setWeather(ArrayList<String> parsedWeatherData){

        if (parsedWeatherData != null){
            String parsedTemperatureString = parsedWeatherData.get(0);
            String parsedDescription = parsedWeatherData.get(1);
            currentTempInFahrenheit = (int) (Double.parseDouble(parsedTemperatureString) * 9/5 - 459.67);
            currentTempInCelsius =  (int) (Double.parseDouble(parsedTemperatureString) - 273.15);
            if (tempInFahrenheit){
                degreesTextView.setText(Integer.toString(currentTempInFahrenheit)+"°F");
            } else {
                degreesTextView.setText(Integer.toString(currentTempInCelsius)+"°C");
            }
            weatherDescriptionTextView.setText(parsedDescription);
            temperatureShown = true;
        } else {
            degreesTextView.setText("");
            weatherDescriptionTextView.setText("There was a problem. Try again.");
            if (possibleErrors == 1){
                weatherDescriptionTextView.setText("There was a problem. Try again.\nIt seems we couldn't find your city.");
                possibleErrors = 0;
            }
            temperatureShown = false;
        }
        //cityNameEditText.setText("");
    }

    public void switchCelsiusFahrenheit(View view){
        boolean on = ((Switch) view).isChecked();

        if (on){
            tempInFahrenheit = true;
            if (modeIsWeatherForecast == false){
                if (temperatureShown){
                    degreesTextView.setText(Integer.toString(currentTempInFahrenheit)+"°F");
                }
            } else {
                switchCardView();
            }

        } else {
            tempInFahrenheit = false;
            if (modeIsWeatherForecast == false){
                if (temperatureShown){
                    degreesTextView.setText(Integer.toString(currentTempInCelsius)+"°C");
                }
            } else {
                switchCardView();
            }
        }
    }

    public void setPossibleErrors(int errorCode){
        possibleErrors = errorCode;
    }

    public void setCards(ArrayList<ArrayList<String>> cardDataAll){

        if (cardDataAll != null) {
            cardDate = cardDataAll.get(0);
            cardTime = cardDataAll.get(1);
            cardTempKelvin = cardDataAll.get(2);
            cardDescription = cardDataAll.get(3);

            cardTempCelsius = new ArrayList<>();
            cardTempFahrenheit = new ArrayList<>();
            for (String degrees : cardTempKelvin) {
                int celsius = (int) (Double.parseDouble(degrees) - 273.15);
                cardTempCelsius.add(Integer.toString(celsius));
            }
            for (String degrees : cardTempKelvin) {
                int fahrenheit = (int) (Double.parseDouble(degrees) * 9/5 - 459.67);
                cardTempFahrenheit.add(Integer.toString(fahrenheit));
            }

            if (tempInFahrenheit){
                cardTemp = cardTempFahrenheit;
            } else {
                cardTemp = cardTempCelsius;
            }
            ForecastAdapter adapter = new ForecastAdapter(cardDate, cardTime, cardTemp, cardDescription);
            forecastCardsRecyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            //layoutManager.setStackFromEnd(true);
            forecastCardsRecyclerView.setLayoutManager(layoutManager);

            weatherLayout.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.INVISIBLE);
            forecastLayout.setVisibility(View.VISIBLE);

        } else {
            infoText.setText("There was a problem. Please try again.");
            if (possibleErrors == 1){
                infoText.setText("There was a problem. Please try again.\nIt seems we couldn't find your city.");
                possibleErrors = 0;
            }
            forecastLayout.setVisibility(View.INVISIBLE);
            weatherLayout.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            cardDate = new ArrayList<>();
            cardTime = new ArrayList<>();
            cardTemp = new ArrayList<>();
            cardDescription = new ArrayList<>();
            cardDate.add("There was a problem. Please try again. ");
            cardTime.add("It seems we couldn't find your city.");
            cardTemp.add("Uh-oh!");
            cardDescription.add("");
        }


        }

        public void switchCardView(){

        if (!cardTemp.get(0).equalsIgnoreCase("Uh-oh!")){
            if (tempInFahrenheit){
                cardTemp = cardTempFahrenheit;
            } else {
                cardTemp = cardTempCelsius;
            }
        }

            ForecastAdapter adapter = new ForecastAdapter(cardDate, cardTime, cardTemp, cardDescription);
            forecastCardsRecyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            //layoutManager.setStackFromEnd(true);
            forecastCardsRecyclerView.setLayoutManager(layoutManager);

        }
}
