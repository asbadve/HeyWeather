package com.badve.ajinkya.heyweather;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.badve.ajinkya.heyweather.Adapters.RecyclerViewWeatherAdapter;
import com.badve.ajinkya.heyweather.Models.Weather;
import com.badve.ajinkya.heyweather.view.RecycleEmptyErrorView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String WEATHER_PARCEBLE_TAG = "weather";
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
        initRecyclerView(savedInstanceState);






    }



    private void initRecyclerView(Bundle savedInstanceState) {
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
