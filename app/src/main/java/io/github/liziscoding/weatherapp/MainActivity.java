package io.github.liziscoding.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
    }

    public void getWeather(View view){
        enteredCity = cityNameEditText.getText().toString();
        if (enteredCity.length() > 0){
            cityNameWeatherView.setText(enteredCity.toUpperCase());
            String searchUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + enteredCity + "&APPID=" + apiKey;
            GetWeatherData getWeatherData = new GetWeatherData(this);
            getWeatherData.execute(searchUrl);
        }

        forecastLayout.setVisibility(View.INVISIBLE);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    public void getForecast(View view){
        enteredCity = cityNameEditText.getText().toString();
        if (enteredCity.length() > 0){
            forecastCityTextView.setText(enteredCity.toUpperCase());
            String searchUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + enteredCity + "&APPID=" + apiKey;
            GetForecastData getForecastData = new GetForecastData(this);
            getForecastData.execute(searchUrl);
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
            }
            temperatureShown = false;
        }
        //cityNameEditText.setText("");
    }

    public void switchCelsiusFahrenheit(View view){
        boolean on = ((Switch) view).isChecked();

        if (on){
            tempInFahrenheit = true;
            if (temperatureShown){
                degreesTextView.setText(Integer.toString(currentTempInFahrenheit)+"°F");
            }
        } else {
            tempInFahrenheit = false;
            if (temperatureShown){
                degreesTextView.setText(Integer.toString(currentTempInCelsius)+"°C");
            }
        }
    }

    public void setPossibleErrors(int errorCode){
        possibleErrors = errorCode;
    }

    public void setCards(ArrayList<ArrayList<String>> cardDataAll){
        ArrayList<String> cardDate = cardDataAll.get(0);
        ArrayList<String> cardTime = cardDataAll.get(1);
        ArrayList<String> cardTemp = cardDataAll.get(2);
        ArrayList<String> cardDescription = cardDataAll.get(3);

        ForecastAdapter adapter = new ForecastAdapter(cardDate, cardTime, cardTemp, cardDescription);
        forecastCardsRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        layoutManager.setStackFromEnd(true);
        forecastCardsRecyclerView.setLayoutManager(layoutManager);

        weatherLayout.setVisibility(View.INVISIBLE);
        forecastLayout.setVisibility(View.VISIBLE);
    }
}
