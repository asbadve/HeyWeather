package com.badve.ajinkya.heyweather.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.badve.ajinkya.heyweather.Models.Weather;
import com.badve.ajinkya.heyweather.Network.VolleySingleton;
import com.badve.ajinkya.heyweather.NextWeather;
import com.badve.ajinkya.heyweather.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ajinkya on 07-06-2015.
 */
public class RecyclerViewWeatherNextAdapter extends RecyclerView.Adapter<RecyclerViewWeatherNextAdapter.NextHolder> {

    private final Context context;
    private final Activity activity;
    private final ImageLoader mImageLoader;
    private ArrayList<Weather> weather;
    private LayoutInflater mInflater;

    public RecyclerViewWeatherNextAdapter(Activity activity, Context context, ArrayList<Weather> weather) {
        this.activity = activity;
        this.context = context;
        this.weather = weather;
        mInflater = LayoutInflater.from(context);
        mImageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public NextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = mInflater.inflate(R.layout.next_weather_row_layout, parent, false);
        NextHolder viewHolder = new NextHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(NextHolder holder, int position) {

        final Weather weather = getWeather(position);

        holder.mainTemp.setText(""+getInKelvin(weather.getmArrayListTemp().get(0).getDayTmpreture())+""+ (char) 0x00B0);
        Date date = weather.getDate();
        holder.curretnDate.setText(" "+ android.text.format.DateFormat.format("dd", date)+" "+android.text.format.DateFormat.format("EEEE", date));
        holder.weatherIcon.setImageUrl(getWetherUrl(weather.getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);
        holder.windSpeed.setText("Wind speed "+weather.getWindSpeed()+" m/s");
        holder.windDescp.setText(weather.getmArrayListWeatherInfo().get(0).getDesciption().toString());

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return weather.size();
    }



    public Weather getWeather(int position) {
        return weather.get(position);
    }

    private String getWetherUrl(String iconName){
        return "http://openweathermap.org/img/w/"+iconName+".png";
    }

    private String getInKelvin(Double minDailyTmpreture) {
        double cel = minDailyTmpreture - 273.15;
        return ""+(int)cel;
    }

    static class NextHolder extends RecyclerView.ViewHolder
    {

        TextView mainTemp,curretnDate,windSpeed,windDescp;
        NetworkImageView weatherIcon;
        CardView cardView;

        public NextHolder(View itemView) {
            super(itemView);
            mainTemp = (TextView) itemView.findViewById(R.id.mainTemp);
            weatherIcon = (NetworkImageView) itemView.findViewById(R.id.weatherIcon);
            curretnDate= (TextView) itemView.findViewById(R.id.currentDate);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            windSpeed = (TextView)itemView.findViewById(R.id.windSpeed);
            windDescp = (TextView)itemView.findViewById(R.id.windDescMain);
        }
    }
}
