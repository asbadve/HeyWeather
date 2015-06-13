package com.badve.ajinkya.heyweather.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.badve.ajinkya.heyweather.MainActivity;
import com.badve.ajinkya.heyweather.Models.City;
import com.badve.ajinkya.heyweather.Models.Tempreture;
import com.badve.ajinkya.heyweather.Models.Weather;
import com.badve.ajinkya.heyweather.Models.WeatherDes;
import com.badve.ajinkya.heyweather.MyApplication;
import com.badve.ajinkya.heyweather.Network.VolleySingleton;
import com.badve.ajinkya.heyweather.NextWeather;
import com.badve.ajinkya.heyweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Ajinkya on 02-06-2015.
 */
public class RecyclerViewWeatherAdapter extends RecyclerView.Adapter<RecyclerViewWeatherAdapter.WeatherHolder> {
    private static final String TAG_JSON = "Json response";
    private static final String TAG_WEATHER_PARCEBLE = "weather";
    private static final String TAG_WEATHER_CITY = "city";
    Activity activity;
    Context context;
    ArrayList<City> mCityArrayList;
    private LayoutInflater mInflater;
    private ArrayList<Weather> weather;
    ImageLoader mImageLoader;

    public RecyclerViewWeatherAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mImageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    public void setCities(ArrayList<City> mCityArrayList){
        this.mCityArrayList = mCityArrayList;
        notifyDataSetChanged();

    }

    public City getCity(int position){
        return mCityArrayList.get(position);
    }


    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = mInflater.inflate(R.layout.weather_row_layout, parent, false);
        WeatherHolder viewHolder = new WeatherHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(final WeatherHolder holder, int position) {

        final City city = getCity(position);
        holder.cityNameTextView.setText(city.getName());

        String URL = getURL(city.getName());

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        //TODO remove after the desiging
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Responce",""+response);



                weather = parseJsonResponse(response);
                Log.d("14 object of weather",""+weather.toString());
                holder.mainTemp.setText(""+getInKelvin(weather.get(0).getmArrayListTemp().get(0).getDayTmpreture())+""+ (char) 0x00B0);
                Date date = weather.get(0).getDate();
                holder.curretnDate.setText(" "+ android.text.format.DateFormat.format("dd", date)+" "+android.text.format.DateFormat.format("EEEE", date));
                holder.weatherIcon.setImageUrl(getWetherUrl(weather.get(0).getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);
                holder.windSpeed.setText("Wind speed "+weather.get(0).getWindSpeed()+" m/s");
                holder.windDescp.setText(weather.get(0).getmArrayListWeatherInfo().get(0).getDesciption().toString());

                holder.textViewForDay1.setText(android.text.format.DateFormat.format("EE", weather.get(1).getDate()));
                holder.textViewForDay2.setText(android.text.format.DateFormat.format("EE", weather.get(2).getDate()));
                holder.textViewForDay3.setText(android.text.format.DateFormat.format("EE", weather.get(3).getDate()));
                holder.textViewForDay4.setText(android.text.format.DateFormat.format("EE", weather.get(4).getDate()));
                holder.textViewForDay5.setText(android.text.format.DateFormat.format("EE", weather.get(5).getDate()));





                holder.minTemp1.setText(getInKelvin(weather.get(1).getmArrayListTemp().get(0).getMinDailyTmpreture()) +""+ (char) 0x00B0);
                holder.minTemp2.setText(getInKelvin(weather.get(2).getmArrayListTemp().get(0).getMinDailyTmpreture()) +""+ (char) 0x00B0);
                holder.minTemp3.setText(getInKelvin(weather.get(3).getmArrayListTemp().get(0).getMinDailyTmpreture()) +""+ (char) 0x00B0);
                holder.minTemp4.setText(getInKelvin(weather.get(4).getmArrayListTemp().get(0).getMinDailyTmpreture()) +""+ (char) 0x00B0);
                holder.minTemp5.setText(getInKelvin(weather.get(5).getmArrayListTemp().get(0).getMinDailyTmpreture()) +""+ (char) 0x00B0);

                holder.maxTemp1.setText(getInKelvin(weather.get(1).getmArrayListTemp().get(0).getMaxDailyTmpreture()) +""+ (char) 0x00B0);
                holder.maxTemp2.setText(getInKelvin(weather.get(2).getmArrayListTemp().get(0).getMaxDailyTmpreture()) +""+ (char) 0x00B0);
                holder.maxTemp3.setText(getInKelvin(weather.get(3).getmArrayListTemp().get(0).getMaxDailyTmpreture()) +""+ (char) 0x00B0);
                holder.maxTemp4.setText(getInKelvin(weather.get(4).getmArrayListTemp().get(0).getMaxDailyTmpreture()) +""+ (char) 0x00B0);
                holder.maxTemp5.setText(getInKelvin(weather.get(5).getmArrayListTemp().get(0).getMaxDailyTmpreture()) +""+ (char) 0x00B0);

                holder.weatherIcon1.setImageUrl(getWetherUrl(weather.get(1).getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);
                holder.weatherIcon2.setImageUrl(getWetherUrl(weather.get(2).getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);
                holder.weatherIcon3.setImageUrl(getWetherUrl(weather.get(3).getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);
                holder.weatherIcon4.setImageUrl(getWetherUrl(weather.get(4).getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);
                holder.weatherIcon5.setImageUrl(getWetherUrl(weather.get(5).getmArrayListWeatherInfo().get(0).getIcon()),mImageLoader);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",""+error);
                Toast.makeText(context,
                        error.getMessage() +"please retry", Toast.LENGTH_LONG).show();
                activity.finish();

            }
        });
        requestQueue.add(request);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, NextWeather.class);
                intent.putExtra(TAG_WEATHER_CITY, city);

                Log.d("City Name",city.toString());
                //intent.putParcelableArrayListExtra(TAG_WEATHER_PARCEBLE,weather);
                activity.startActivity(intent);

            }
        });



        //TODO call server to get the 14 days weather information




    }

    private String getInKelvin(Double minDailyTmpreture) {
        double cel = minDailyTmpreture - 273.15;

        return ""+(int)cel;
    }

    private ArrayList<Weather> parseJsonResponse(JSONObject response) {

        ArrayList<Weather> mWeather = new ArrayList<Weather>();
        Weather currentWeather = null;
        if(response != null && response.length()>0) {
            int listNumber = 0;

            try {

                if (response.has("cnt") && !response.isNull("cnt")) {
                    listNumber = response.getInt("cnt");
                    Log.d(TAG_JSON, "listNumber " + listNumber);
                    Log.d(TAG_JSON, "city name " + response.getJSONObject("city").getString("name"));
                }



                JSONArray lists = response.getJSONArray("list");
                for (int i = 0; i < listNumber; i++) {


                    currentWeather = new Weather();
                    JSONObject listObject = lists.getJSONObject(i);

                    if(listObject.has("dt") && !listObject.isNull("dt")){
                        long dateinMilisecond = listObject.getLong("dt");
                        currentWeather.setDate(getDateFormatByString(dateinMilisecond));
                    }

                    JSONObject temObject = listObject.getJSONObject("temp");


                    //for (int j = 0; j < temObject.length(); j++) {
                    ArrayList<Tempreture> mTempretureArrayList= new ArrayList<Tempreture>();

                    Tempreture tempreture = new Tempreture();

                    ;
                    if(temObject.has("day") && !temObject.isNull("day")){
                        tempreture.setDayTmpreture(temObject.getDouble("day"));
                    }

                    if(temObject.has("min") && !temObject.isNull("min")){
                        tempreture.setMinDailyTmpreture(temObject.getDouble("min"));
                    }
                    if(temObject.has("min") && !temObject.isNull("min")){
                        tempreture.setMaxDailyTmpreture(temObject.getDouble("max"));
                    }

                    if(temObject.has("night") && !temObject.isNull("night")){
                        tempreture.setNightTmpreture(temObject.getDouble("night"));
                    }

                    if(temObject.has("eve") && !temObject.isNull("eve")){
                        tempreture.setEveTmpreture(temObject.getDouble("eve"));
                    }

                    if(temObject.has("morn") && !temObject.isNull("morn")){
                        tempreture.setMorningTmpreture(temObject.getDouble("morn"));
                    }

                    mTempretureArrayList.add(tempreture);
                    currentWeather.setmArrayListTemp(mTempretureArrayList);

                    if(listObject.has("pressure") && !listObject.isNull("pressure")){
                        currentWeather.setPressure(listObject.getDouble("pressure"));
                    }

                    if(listObject.has("humidity") && !listObject.isNull("humidity")){
                        Log.d("Humidity ",""+listObject.getInt("humidity"));
                        currentWeather.setHumidity(listObject.getInt("humidity"));
                    }






                    //WeatherDes
                    ArrayList<WeatherDes> mWeatherDesArrayList = new ArrayList<WeatherDes>();

                    WeatherDes weatherDes = new WeatherDes();
                    ////////////////////
                    JSONArray weatherArray = listObject.getJSONArray("weather");

                    if (weatherArray.getJSONObject(0).has("id") && !weatherArray.getJSONObject(0).isNull("id")) {

                        weatherDes.setId(weatherArray.getJSONObject(0).getInt("id"));
                    }


                    if (weatherArray.getJSONObject(0).has("main") && !weatherArray.getJSONObject(0).isNull("main")) {

                        weatherDes.setMain(weatherArray.getJSONObject(0).getString("main"));
                    }

                    if (weatherArray.getJSONObject(0).has("description") && !weatherArray.getJSONObject(0).isNull("description")) {

                        weatherDes.setDesciption(weatherArray.getJSONObject(0).getString("description"));
                    }

                    if (weatherArray.getJSONObject(0).has("icon") && !weatherArray.getJSONObject(0).isNull("icon")) {

                        weatherDes.setIcon(weatherArray.getJSONObject(0).getString("icon"));
                    }

                    mWeatherDesArrayList.add(weatherDes);

                    currentWeather.setmArrayListWeatherInfo(mWeatherDesArrayList);

                    /////////////////


                    if(listObject.has("speed") && ! listObject.isNull("speed"))
                    {
                        currentWeather.setWindSpeed(listObject.getDouble("speed"));
                    }

                    if(listObject.has("deg") && ! listObject.isNull("deg"))
                    {
                        currentWeather.setWindDirection(listObject.getInt("deg"));//wind direction
                    }

                    if(listObject.has("clouds") && ! listObject.isNull("clouds"))
                    {
                        currentWeather.setCloud(listObject.getInt("clouds"));
                    }

                  /*  if(listObject.has("rain") && ! listObject.isNull("rain"))
                    {
                        currentWeather.setRain(listObject.getDouble("rain"));
                    }*///TODO null some time implment for null object

                   // Log.d("Weather object",currentWeather.toString());
                    mWeather.add(currentWeather);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mWeather;
    }

    private Date getDateFormatByString(long dateinMilisecond) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

/* debug: is it local time? */
        Log.d("Time zone: ", tz.getDisplayName());

/* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(tz);

/* print your timestamp and double check it's the date you expect */
        long timestamp = dateinMilisecond;
        String localTime = sdf.format(new Date(timestamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        Log.d("Time: ", localTime);
        Date date = null;
        try {
            date = sdf.parse(localTime);
            Log.d("Date format ", localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*Date date = null;

        String dateFormat = "dd/MM/yyyy";
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateinMilisecond);



        String StringDate = formatter.format(calendar.getTime());
        try {
            date = formatter.parse(StringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("Date ",""+date);
        */
        return date;

    }

    private String getURL(String name) {
        //String CityName="pune";

        String URL="http://api.openweathermap.org/data/2.5/forecast/daily?q="+name+"&cnt=14&APPID="+
        MyApplication.API_KEY_OPEN_WEATHER_MAP;
        return URL;
    }

    private String getWetherUrl(String iconName){

        //Log.d("Icon Image",""+iconName);

        return "http://openweathermap.org/img/w/"+iconName+".png";
    }


    @Override
    public int getItemCount() {
        return mCityArrayList.size();
    }



    static class WeatherHolder extends RecyclerView.ViewHolder{
        TextView cityNameTextView,mainTemp,curretnDate,windSpeed,windDescp;
        NetworkImageView weatherIcon;
        CardView cardView;
        TextView maxTemp1,maxTemp2,maxTemp3,maxTemp4,maxTemp5,minTemp1,minTemp2,minTemp3,minTemp4,minTemp5;
        TextView textViewForDay1,textViewForDay2,textViewForDay3,textViewForDay4,textViewForDay5;
        NetworkImageView weatherIcon1,weatherIcon2,weatherIcon3,weatherIcon4,weatherIcon5;




        public WeatherHolder(View itemView) {
            super(itemView);
            cityNameTextView = (TextView) itemView.findViewById(R.id.CityName);
            mainTemp = (TextView) itemView.findViewById(R.id.mainTemp);
            weatherIcon = (NetworkImageView) itemView.findViewById(R.id.weatherIcon);
            curretnDate= (TextView) itemView.findViewById(R.id.currentDate);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            windSpeed = (TextView)itemView.findViewById(R.id.windSpeed);
            windDescp = (TextView)itemView.findViewById(R.id.windDescMain);

            minTemp1 = (TextView)itemView.findViewById(R.id.minTemp1);
            minTemp2 = (TextView)itemView.findViewById(R.id.minTemp2);
            minTemp3 = (TextView)itemView.findViewById(R.id.minTemp3);
            minTemp4 = (TextView)itemView.findViewById(R.id.minTemp4);
            minTemp5 = (TextView)itemView.findViewById(R.id.minTemp5);

            maxTemp1 = (TextView)itemView.findViewById(R.id.maxTemp1);
            maxTemp2 = (TextView)itemView.findViewById(R.id.maxTemp2);
            maxTemp3 = (TextView)itemView.findViewById(R.id.maxTemp3);
            maxTemp4 = (TextView)itemView.findViewById(R.id.maxTemp4);
            maxTemp5 = (TextView)itemView.findViewById(R.id.maxTemp5);

            weatherIcon1 = (NetworkImageView)itemView.findViewById(R.id.weatherIcon1);
            weatherIcon2 = (NetworkImageView)itemView.findViewById(R.id.weatherIcon2);
            weatherIcon3 = (NetworkImageView)itemView.findViewById(R.id.weatherIcon3);
            weatherIcon4 = (NetworkImageView)itemView.findViewById(R.id.weatherIcon4);
            weatherIcon5 = (NetworkImageView)itemView.findViewById(R.id.weatherIcon5);

            textViewForDay1 = (TextView)itemView.findViewById(R.id.textViewForDay1);
            textViewForDay2 = (TextView)itemView.findViewById(R.id.textViewForDay2);
            textViewForDay3 = (TextView)itemView.findViewById(R.id.textViewForDay3);
            textViewForDay4 = (TextView)itemView.findViewById(R.id.textViewForDay4);
            textViewForDay5 = (TextView)itemView.findViewById(R.id.textViewForDay5);
        }
    }
}
