package io.github.liziscoding.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherLayout = findViewById(R.id.weatherLayout);
        cityNameEditText = findViewById(R.id.cityNameEditText);
        cityNameWeatherView = findViewById(R.id.cityNameWeatherView);
        degreesTextView = findViewById(R.id.degrees);
        weatherDescriptionTextView = findViewById(R.id.weatherDescription);

    }

    public void getWeather(View view){
        enteredCity = cityNameEditText.getText().toString();
        if (enteredCity.length() > 0){
            cityNameWeatherView.setText(enteredCity.toUpperCase());
            String searchUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + enteredCity + "&APPID=" + apiKey;
            GetWeatherData getWeatherData = new GetWeatherData(this);
            getWeatherData.execute(searchUrl);
        }

        weatherLayout.setVisibility(View.VISIBLE);
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
        cityNameEditText.setText("");
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
}
