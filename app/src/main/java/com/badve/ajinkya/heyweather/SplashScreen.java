package com.badve.ajinkya.heyweather;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.badve.ajinkya.heyweather.Adapters.RecyclerViewCityAdapter;
import com.badve.ajinkya.heyweather.Interfaces.SoftKeyboardStateHelper;
import com.badve.ajinkya.heyweather.Models.City;
import com.badve.ajinkya.heyweather.Util.PlaceJSONParser;
import com.badve.ajinkya.heyweather.view.CustomAutoCompleteTextView;
import com.badve.ajinkya.heyweather.view.RecycleEmptyErrorView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/***
 * @author Ajinkya
 * //TODO fetch the city text file parse it and store it into the local database
 */
public class SplashScreen extends AppCompatActivity {
    FloatingActionButton fab;
    RecycleEmptyErrorView mRecyclerView;
    RecyclerViewCityAdapter recyclerViewCityAdapter;
    CustomAutoCompleteTextView autoCompleteTextView;

    private static String TAG = SplashScreen.class.getSimpleName();

    RelativeLayout autocompleteLayout;
    TextInputLayout textInputLayoutForCity;


    PlacesTask placesTask;
    ParserTask parserTask;
    private static final String API_KEY = "AIzaSyAkpLDW9FyaYi5qKyTfl1x1115BoMZGm1I";
    private boolean isKeyBoardOpen=false;
    Database mDatabase;
    private LinearLayoutManager mLayoutManager;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mDatabase = new Database(getApplicationContext());

       /* if(!mDatabase.isCityPresent()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }*/


        initRecyclerView();
        autocompleteLayout = (RelativeLayout)findViewById(R.id.autocompleteLayout);
        fab = (FloatingActionButton) findViewById(R.id.imageView);
        textInputLayoutForCity = (TextInputLayout) findViewById(R.id.editText1);
        autoCompleteTextView = (CustomAutoCompleteTextView) findViewById(R.id.editText);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isKeyBoardOpen){
                    if (autoCompleteTextView.getEditableText().length()!=0){

                        City city = new City();
                        city.setName(autoCompleteTextView.getText().toString());

                        mDatabase.insertCity(city);
                        recyclerViewCityAdapter.addCity(city);
                        autoCompleteTextView.setText("");

                    }else {

                        setErrorMsg("enter valid ciy",textInputLayoutForCity);
                    }



                }else {


                    if(mDatabase.isCityPresent())
                    {
                        Toast.makeText(getApplicationContext(),"Please add atlest one ciy",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                }


                //Toast.makeText(getApplicationContext(), "city Name"+ city,Toast.LENGTH_SHORT).show();
            }
        });
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Entered text", s.toString());
                //TODO check internet related validation

                if (isOnline()) {
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                }else{
                    Toast.makeText(getApplicationContext(),"No internet connection please try again later",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });





        final SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.rootLayout));
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {

                fab.setImageResource(R.drawable.ic_action_add);
                isKeyBoardOpen=true;
                autoCompleteTextView.setEnabled(true);
            }

            @Override
            public void onSoftKeyboardClosed() {

                fab.setImageResource(R.drawable.ic_action_next);
                isKeyBoardOpen=false;

            }
        });






    }

    private void initRecyclerView() {
        mRecyclerView = (RecycleEmptyErrorView)findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = new Database(getApplicationContext());
        recyclerViewCityAdapter = new RecyclerViewCityAdapter(getApplicationContext(),SplashScreen.this);
        mRecyclerView.setAdapter(recyclerViewCityAdapter);
        recyclerViewCityAdapter.setCities( mDatabase.readCity());
    }

    public static void setErrorMsg(String msg,TextInputLayout viewId)
    {

        viewId.setErrorEnabled(true);
        viewId.setError(msg);

    }


    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key="+API_KEY;

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=(cities)";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
                Log.d("Url",url);
                Log.d("returning data",data.toString());
            }catch(Exception e){

                Log.d("Background Task error", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            autoCompleteTextView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
