package com.badve.ajinkya.heyweather;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.badve.ajinkya.heyweather.Adapters.RecyclerViewWeatherAdapter;
import com.badve.ajinkya.heyweather.Adapters.RecyclerViewWeatherNextAdapter;
import com.badve.ajinkya.heyweather.Models.Tempreture;
import com.badve.ajinkya.heyweather.Models.Weather;
import com.badve.ajinkya.heyweather.Models.WeatherDes;
import com.badve.ajinkya.heyweather.Network.VolleySingleton;
import com.badve.ajinkya.heyweather.view.RecycleEmptyErrorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NextWeather extends AppCompatActivity {
    private static final String TAG_WEATHER_PARCEBLE = "weather";
    private ArrayList<Weather> weather;
    private RecycleEmptyErrorView mRecyclerView;
    private RecyclerViewWeatherNextAdapter mRecyclerViewWeatherNextAdapter;
    private static final String TAG_WEATHER_CITY = "city";
    private String cityName;
    Toolbar toolbar;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_weather);
        toolbar = (Toolbar)findViewById(R.id.app_bar);

        initRecyclerView();
        if (getIntent().hasExtra(TAG_WEATHER_CITY))
        {
            cityName = getIntent().getStringExtra(TAG_WEATHER_CITY);
            toolbar.setTitle(cityName);

        }else {
            finish();//TODO with some toast message
        }

        String URL = getURL(cityName);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pDialog.setMessage("Loading ...");
        showDialog();
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        //TODO remove after the desiging
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideDialog();
                //Log.d("Responce", "" + response);

                weather = parseJsonResponse(response);
                Log.d("Responce", "" + weather);


                mRecyclerViewWeatherNextAdapter = new RecyclerViewWeatherNextAdapter(NextWeather.this,getApplicationContext(),weather);
                mRecyclerView.setAdapter(mRecyclerViewWeatherNextAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        });
        requestQueue.add(request);

        //mRecyclerViewWeatherNextAdapter.setWeathers(weather);







    }

    private void initRecyclerView() {
        mRecyclerView = (RecycleEmptyErrorView)findViewById(R.id.recyclerViewNext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*mRecyclerViewWeatherNextAdapter = new RecyclerViewWeatherNextAdapter(NextWeather.this,getApplicationContext());
        mRecyclerView.setAdapter(mRecyclerViewWeatherNextAdapter);*/
        //mRecyclerView.setAdapter(mRecyclerViewWeatherNextAdapter);
        //mRecyclerViewWeatherNextAdapter.setWeathers(weather);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_next_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Weather> parseJsonResponse(JSONObject response) {

        ArrayList<Weather> mWeather = new ArrayList<Weather>();
        Weather currentWeather = null;
        if(response != null && response.length()>0) {
            int listNumber = 0;

            try {

                if (response.has("cnt") && !response.isNull("cnt")) {
                    listNumber = response.getInt("cnt");
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

    private String getInKelvin(Double minDailyTmpreture) {
        double cel = minDailyTmpreture - 273.15;

        return ""+(int)cel;
    }

}
