package io.github.liziscoding.weatherapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private ArrayList<String> date;
    private ArrayList<String> time;
    private ArrayList<String> temperature;
    private ArrayList<String> description;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public ForecastAdapter(ArrayList<String> date, ArrayList<String> time, ArrayList<String> temperature, ArrayList<String> description) {
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.description = description;
    }

    @Override
    public int getItemCount() {
        return temperature.size();
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_entry, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.mCardView;
        TextView dateTextView = (TextView) cardView.findViewById(R.id.date);
        dateTextView.setText(date.get(position));
        TextView timeTextView = (TextView) cardView.findViewById(R.id.time);
        timeTextView.setText(time.get(position));
        TextView forecastDegrees = (TextView) cardView.findViewById(R.id.forecastDegrees);
        forecastDegrees.setText(temperature.get(position)+"°");
        TextView forecastText = (TextView) cardView.findViewById(R.id.forecastText);
        forecastText.setText(description.get(position));
    }
}
