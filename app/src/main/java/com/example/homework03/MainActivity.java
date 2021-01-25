package com.example.homework03;
/*  Assignment HomeWork03
   File Name : MainActivity.java
   Name: Supriya Chinthakuntla
   Name: Amulya Venkatapathi
   Group Name : 14
 */
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements MyCityAdapter.InteractWithMainActivity{

    TextView textview_text;
    TextView textview_city;
    TextView textview_weather;
    TextView textview_temperature;
    TextView textview_time;
    TextView textview_info;
    TextView textview_savedtitle;
    EditText edittext_cityname;
    EditText edittext_country;
    ImageView imageView;
    ImageView imageview_progress;
    Button button_current;
    Button button_searchcity;
    ProgressBar progressBar;
    RecyclerView recyclerview_savedcities;
    RecyclerView.Adapter recyclerview_adapter;
    LinearLayoutManager recyclerview_layoutManager;
    static String cityKey ="CITY";
    public static ArrayList<City> cityGlobal = new ArrayList<>();
    public static CurrentCity currentCityGlobal =new CurrentCity();
    private static final int REQ_CODE =0x001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview_text =findViewById(R.id.textview_text);
        textview_info=findViewById(R.id.tv_info);
        button_current=findViewById(R.id.button_current);

        textview_text.setVisibility(View.VISIBLE);
        textview_info.setVisibility(View.VISIBLE);
        button_current.setVisibility(View.VISIBLE);

        edittext_cityname=findViewById(R.id.edittext_cityname);
        edittext_country=findViewById(R.id.edittext_country);

        button_searchcity =findViewById(R.id.button_searchcity);

        button_searchcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag =0;

                String s1= edittext_country.getText().toString();
                String s2 = edittext_cityname.getText().toString();

                if(s1.equals(""))
                {
                    edittext_country.setError("Enter Vaild Text");
                    flag =1;
                }
                if(s2.equals(""))
                {
                    edittext_cityname.setError("Enter Vaild Text");
                    flag=1;
                }

                if(flag==0) {
                    new GetCityLocation(edittext_country.getText().toString()).execute(edittext_cityname.getText().toString());
                }
            }
        });


        button_current =findViewById(R.id.button_current);
        button_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);

                View Alert_CurrentCity = li.inflate(R.layout.current_city,null);

                final AlertDialog.Builder alertbuilder =new AlertDialog.Builder(MainActivity.this);

                alertbuilder.setView(Alert_CurrentCity);

                final EditText edittext_cityname= Alert_CurrentCity.findViewById(R.id.edittext_cityname);
                final EditText edittext_country =Alert_CurrentCity.findViewById(R.id.edittext_country);
                final Button button_Cancel =Alert_CurrentCity.findViewById(R.id.button_cancel);
                final Button button_Ok =Alert_CurrentCity.findViewById(R.id.button_ok);

                alertbuilder.setCancelable(false)
                        .setTitle("Enter city details");

                final AlertDialog alertDialog = alertbuilder.create();
                button_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                button_Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("demo","Value set is : "+edittext_cityname.getText()+" COuntry is :"+edittext_country.getText());
                        if(isConnected()) {
                            new GetLocation(edittext_country.getText().toString()).execute(edittext_cityname.getText().toString());
                        }
                        else
                        {

                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        textview_city =findViewById(R.id.textview_savedcity);
        final String s1= textview_city.getText().toString();

        textview_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    LayoutInflater li = LayoutInflater.from(MainActivity.this);

                    View Alert_CurrentCity = li.inflate(R.layout.current_city,null);

                    final AlertDialog.Builder alertbuilder =new AlertDialog.Builder(MainActivity.this);

                    alertbuilder.setView(Alert_CurrentCity);

                    final EditText et_cityname= Alert_CurrentCity.findViewById(R.id.edittext_cityname);
                    final EditText et_country =Alert_CurrentCity.findViewById(R.id.edittext_country);
                    final Button bt_Cancel =Alert_CurrentCity.findViewById(R.id.button_cancel);
                    final Button bt_Ok =Alert_CurrentCity.findViewById(R.id.button_ok);

                    alertbuilder.setCancelable(false)
                            .setTitle("Enter city details");

                    final AlertDialog alertDialog = alertbuilder.create();
                    bt_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    bt_Ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("demo","Value set is : "+et_cityname.getText()+" COuntry is :"+et_country.getText());
                            if(isConnected()) {
                                new GetLocation(et_country.getText().toString()).execute(et_cityname.getText().toString());
                            }
                            else
                            {

                            }

                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }


        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }




    class GetLocation extends AsyncTask<String,Void,CurrentCity>
    {

        String country;

        public GetLocation(String country) {
            this.country=country;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar=findViewById(R.id.progressBar);
            imageview_progress=findViewById(R.id.iv_progress);

            progressBar.setVisibility(View.VISIBLE);
            imageview_progress.setVisibility(View.VISIBLE);

        }


        @Override
        protected CurrentCity doInBackground(String... strings) {

            HttpURLConnection connection=null;
            CurrentCity currentCity =new CurrentCity();

            try {
                String uri="https://dataservice.accuweather.com/locations/v1/cities"+"/"+ URLEncoder.encode(country, "UTF-8")+"/"+"search?" +
                        "apikey="+getResources().getString(R.string.Key)+"&q="+URLEncoder.encode(strings[0],"UTF-8");

                URL url =new URL(uri);
                connection =(HttpURLConnection)url.openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String json = IOUtils.toString(connection.getInputStream(),"UTF-8");

                    Log.d("demo","VAlue got "+json);

                    JSONArray root =new JSONArray(json);




                        JSONObject locationJSON = root.getJSONObject(0);

                        currentCity.city_key=locationJSON.getString("Key");
                        currentCity.city_name =strings[0];
                        currentCity.country=country;

                }



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return currentCity;
        }



        @Override
        protected void onPostExecute(CurrentCity s) {
            super.onPostExecute(s);
            new GetCurrentCity().execute(s);

        }

    }


    class GetCurrentCity extends AsyncTask<CurrentCity,Void,CurrentCity>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected CurrentCity doInBackground(CurrentCity... arrayLists) {
            HttpURLConnection connection=null;
            String city_key=arrayLists[0].city_key;
            Log.d("demo",city_key);

            CurrentCity currentCity =new CurrentCity();

            try {
                String uri ="https://dataservice.accuweather.com/currentconditions/v1/"+URLEncoder.encode(city_key,"UTF-8")+"?"+"apikey="+getResources().getString(R.string.Key);


                URL url = new URL(uri);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    Log.d("demo", "VAlue got " + json);

                    JSONArray root = new JSONArray(json);

                    JSONObject cityJSON = root.getJSONObject(0);

                    currentCity.city_key=arrayLists[0].city_key;
                    currentCity.LocalObservationDateTime=cityJSON.getString("LocalObservationDateTime");
                    currentCity.WeatherText=cityJSON.getString("WeatherText");
                    currentCity.WeatherIcon=cityJSON.getString("WeatherIcon");
                    currentCity.city_name=arrayLists[0].city_name;
                    currentCity.country=arrayLists[0].country;

                    JSONObject temp = cityJSON.getJSONObject("Temperature");

                    JSONObject tempJSON =temp.getJSONObject("Imperial");

                    currentCity.Value=tempJSON.getString("Value");
                    currentCity.Unit=tempJSON.getString("Unit");


                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return currentCity;

        }


        @Override
        protected void onPostExecute(CurrentCity currentCities) {
            super.onPostExecute(currentCities);

            progressBar=findViewById(R.id.progressBar);
            imageview_progress=findViewById(R.id.iv_progress);

            progressBar.setVisibility(View.INVISIBLE);
            imageview_progress.setVisibility(View.INVISIBLE);


            Log.d("demo","values are : "+currentCities.toString());
            textview_text =findViewById(R.id.textview_text);
            button_current=findViewById(R.id.button_current);

            textview_text.setVisibility(View.INVISIBLE);
            button_current.setVisibility(View.INVISIBLE);

            textview_city =findViewById(R.id.textview_savedcity);
            textview_weather =findViewById(R.id.textview_weather);
            textview_temperature =findViewById(R.id.textview_temperature);
            textview_time =findViewById(R.id.textview_time);
            imageView=findViewById(R.id.imageView);

            textview_city.setVisibility(View.VISIBLE);
            textview_weather.setVisibility(View.VISIBLE);
            textview_temperature.setVisibility(View.VISIBLE);
            textview_time.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);


            textview_city.setText(currentCities.city_name+", "+currentCities.country);
            textview_weather.setText(currentCities.WeatherText);
            textview_temperature.setText("Temperature: "+currentCities.Value+" "+currentCities.Unit);


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = new Date();

            try {
                date = dateFormat.parse(currentCities.LocalObservationDateTime);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime p  = new PrettyTime();

            String date_time= p.format(date);

            textview_time.setText("Updated: "+date_time);

            int value = Integer.parseInt(currentCities.WeatherIcon);
            String v;
            if(value<10)
            {
                v= "0"+value;
            }
            else {
                v = String.valueOf(value);
            }
            String urlToImage="https://developer.accuweather.com/sites/default/files/"+v+"-s.png";

            Picasso.get().load(urlToImage).into(imageView);

            currentCityGlobal.city_key=currentCities.city_key;
            currentCityGlobal.city_name=currentCities.city_name;
            currentCityGlobal.country=currentCities.country;
            currentCityGlobal.WeatherText=currentCities.WeatherText;
            currentCityGlobal.Value=currentCities.Value;
            currentCityGlobal.Unit=currentCities.Unit;
            currentCityGlobal.LocalObservationDateTime=date_time;
            currentCityGlobal.WeatherIcon=currentCities.WeatherIcon;

            Toast.makeText(MainActivity.this,"Current city saved",Toast.LENGTH_SHORT).show();

        }

    }

    class GetCityLocation extends AsyncTask<String,Void,ArrayList<CurrentCity>>
    {

        String country;

        public GetCityLocation(String country) {
            this.country=country;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ArrayList<CurrentCity> doInBackground(String... strings) {

            HttpURLConnection connection=null;
            ArrayList<CurrentCity> currentCitiesList = new ArrayList<>();

            try {
                String uri="https://dataservice.accuweather.com/locations/v1/cities"+"/"+ URLEncoder.encode(country, "UTF-8")+"/"+"search?" +
                        "apikey="+getResources().getString(R.string.Key)+"&q="+URLEncoder.encode(strings[0],"UTF-8");

                URL url =new URL(uri);
                connection =(HttpURLConnection)url.openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String json = IOUtils.toString(connection.getInputStream(),"UTF-8");

                    Log.d("demo","VAlue got "+json);

                    JSONArray root =new JSONArray(json);



                    for(int i=0;i<root.length();i++)
                    {
                    JSONObject locationJSON = root.getJSONObject(i);
                    CurrentCity currentCity =new CurrentCity();
                    currentCity.city_key=locationJSON.getString("Key");

                    JSONObject stateJSON = locationJSON.getJSONObject("AdministrativeArea");

                    currentCity.city_name =strings[0]+", "+stateJSON.getString("ID");
                    currentCity.country=country;

                    currentCitiesList.add(currentCity);
                    }


                }



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return currentCitiesList;
        }



        @Override
        protected void onPostExecute(final ArrayList<CurrentCity> s) {
            super.onPostExecute(s);

            final ArrayList<String> cities = new ArrayList<>();

            textview_info = findViewById(R.id.tv_info);
            textview_info.setVisibility(View.INVISIBLE);

            for (int i = 0; i < s.size(); i++) {
                cities.add(s.get(i).city_name);
            }

            if (cities.size() > 0) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);

                alertBuilder.setTitle("Select City")
                        .setItems(cities.toArray(new String[cities.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("demo", "Clicked On: " + cities.get(which));

                                Intent intent = new Intent(MainActivity.this, CityWeather.class);

                                intent.putExtra(cityKey, s.get(which));
                                startActivityForResult(intent, REQ_CODE);
                            }
                        });

                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

            }
            else
            {
                Toast.makeText(MainActivity.this,"City not found",Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==REQ_CODE && resultCode == RESULT_OK && data !=null)
        {
            String []v;

            v=data.getExtras().getStringArray(CityWeather.KEY);

            Log.d("demo","Array value "+v[0]+" "+v[1]);

            if(v[1].equals("1")) {
                update();
            }
            if(v[0].equals("1")){
               updateGlobalList();
            }
        }


    }


    public void update()
    {
        Log.d("demo","Updated Current city list "+currentCityGlobal.toString());
        textview_text =findViewById(R.id.textview_text);
        button_current=findViewById(R.id.button_current);

        textview_text.setVisibility(View.INVISIBLE);
        button_current.setVisibility(View.INVISIBLE);

        textview_city =findViewById(R.id.textview_savedcity);
        textview_weather =findViewById(R.id.textview_weather);
        textview_temperature =findViewById(R.id.textview_temperature);
        textview_time =findViewById(R.id.textview_time);
        imageView=findViewById(R.id.imageView);

        textview_city.setVisibility(View.VISIBLE);
        textview_weather.setVisibility(View.VISIBLE);
        textview_temperature.setVisibility(View.VISIBLE);
        textview_time.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        textview_city.setText(currentCityGlobal.city_name+", "+currentCityGlobal.country);
        textview_weather.setText(currentCityGlobal.WeatherText);
        textview_temperature.setText("Temperature: "+currentCityGlobal.Value+" "+currentCityGlobal.Unit);
        textview_time.setText("Updated: "+currentCityGlobal.LocalObservationDateTime);

        int value = Integer.parseInt(currentCityGlobal.WeatherIcon);
        String v;
        if(value<10)
        {
            v= "0"+value;
        }
        else {
            v = String.valueOf(value);
        }
        String urlToImage="https://developer.accuweather.com/sites/default/files/"+v+"-s.png";

        Picasso.get().load(urlToImage).into(imageView);

    }

    public void updateGlobalList()
    {
        textview_savedtitle =findViewById(R.id.textview_savedtitle);
        textview_savedtitle.setVisibility(View.VISIBLE);

        recyclerview_savedcities =findViewById(R.id.recyclerview_savedcities);
        recyclerview_savedcities.setHasFixedSize(true);



        recyclerview_layoutManager = new LinearLayoutManager(this);
        recyclerview_savedcities.setLayoutManager(recyclerview_layoutManager);

        recyclerview_adapter = new MyCityAdapter(cityGlobal,this);
        recyclerview_savedcities.setAdapter(recyclerview_adapter);
    }

    public void deleteItem(int position) {
        cityGlobal.remove(position);
        recyclerview_adapter.notifyDataSetChanged();
    }




}
