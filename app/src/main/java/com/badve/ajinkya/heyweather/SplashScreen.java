package com.badve.ajinkya.heyweather;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.badve.ajinkya.heyweather.Adapters.RecyclerViewCityAdapter;
import com.badve.ajinkya.heyweather.Interfaces.SoftKeyboardStateHelper;
import com.badve.ajinkya.heyweather.Models.City;
import com.badve.ajinkya.heyweather.Util.PlaceAPI;
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
import java.util.ArrayList;
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

    private PlacesAutoCompleteAdapter mAdapter;
    RelativeLayout autocompleteLayout;


    PlacesTask placesTask;
    ParserTask parserTask;
    private static final String API_KEY = "AIzaSyAkpLDW9FyaYi5qKyTfl1x1115BoMZGm1I";
    private boolean isKeyBoardOpen=false;
    Database mDatabase;


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

                        setErrorMsg("enter valid ciy",autoCompleteTextView);
                    }



                }else {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }


                //Toast.makeText(getApplicationContext(), "city Name"+ city,Toast.LENGTH_SHORT).show();
            }
        });
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Entered text", s.toString());
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = new Database(getApplicationContext());
        recyclerViewCityAdapter = new RecyclerViewCityAdapter(getApplicationContext(),SplashScreen.this);
        mRecyclerView.setAdapter(recyclerViewCityAdapter);
        recyclerViewCityAdapter.setCities( mDatabase.readCity());
    }

    public static void setErrorMsg(String msg,CustomAutoCompleteTextView viewId)
    {
        //Osama ibrahim 10/5/2013
        int ecolor = Color.WHITE; // whatever color you want
        String estring = msg;
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
        viewId.setError(ssbuilder);

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
                Log.d("Background Task", e.toString());
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

    public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

        ArrayList<String> resultList;

        Context mContext;
        int mResource;


        PlaceAPI mPlaceAPI = new PlaceAPI();

        public PlacesAutoCompleteAdapter(Context context, int resource) {
            super(context, resource);

            mContext = context;
            mResource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            //if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (position != (resultList.size() - 1))
                view = inflater.inflate(R.layout.autocomplete_list_item, null);
            else
                view = inflater.inflate(R.layout.autocomplete_google_logo, null);
            //}
            //else {
            //    view = convertView;
            //}

            if (position != (resultList.size() - 1)) {
                TextView autocompleteTextView = (TextView) view.findViewById(R.id.autocompleteText);
                autocompleteTextView.setText(resultList.get(position));
            }
            else {
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                // not sure what to do <img class="emoji" draggable="false" alt="😀" src="http://s.w.org/images/core/emoji/72x72/1f600.png">
            }

            return view;
        }

        @Override
        public int getCount() {
            // Last item will be the footer
            return resultList.size();
        }

        @Override
        public String getItem(int position) {
            return resultList.get(position);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = mPlaceAPI.autocomplete(constraint.toString());

                        // Footer
                        resultList.add("footer");

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter;
        }


    }



}
