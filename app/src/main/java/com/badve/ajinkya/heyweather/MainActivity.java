package com.badve.ajinkya.heyweather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.badve.ajinkya.heyweather.Adapters.RecyclerViewCityAdapter;
import com.badve.ajinkya.heyweather.Adapters.RecyclerViewWeatherAdapter;
import com.badve.ajinkya.heyweather.Models.City;
import com.badve.ajinkya.heyweather.Models.Tempreture;
import com.badve.ajinkya.heyweather.Models.Weather;
import com.badve.ajinkya.heyweather.Models.WeatherDes;
import com.badve.ajinkya.heyweather.Network.VolleySingleton;
import com.badve.ajinkya.heyweather.view.RecycleEmptyErrorView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecycleEmptyErrorView mRecyclerView;
    Database mDatabase;
    private RecyclerViewWeatherAdapter mRecyclerViewWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerView();






    }






    private void initRecyclerView() {
        mRecyclerView = (RecycleEmptyErrorView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = new Database(getApplicationContext());
        mRecyclerViewWeatherAdapter = new RecyclerViewWeatherAdapter(MainActivity.this,getApplicationContext());
        mRecyclerView.setAdapter(mRecyclerViewWeatherAdapter);
        mRecyclerViewWeatherAdapter.setCities(mDatabase.readCity());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
