package com.example.homework03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.homework03.MainActivity.cityGlobal;
import static com.example.homework03.MainActivity.currentCityGlobal;

public class CityWeather extends AppCompatActivity implements MyAdapter.interactWithCityWeather {

    CurrentCity city;
    TextView textview_city;
    TextView textview_headline;
    TextView textview_date;
    TextView textview_temp;
    TextView textview_day;
    TextView textview_night;
    TextView textview_link;
    TextView textview_condition;
    TextView textview_daytext;
    TextView textview_nighttext;
    ImageView imageview_day;
    ImageView imageview_night;
    ImageView imageview_cityprogress;
    ProgressBar progressBarCity;
    Button button_save;
    Button button_current;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerview_adapter;
    LinearLayoutManager recyclerview_layoutManager;
    ArrayList<Weather> weatherArrayList;
    static String KEY="key";
    String[] key_pass={"0","0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        setTitle("City Weather");

        key_pass[0]="0";
        key_pass[1]="0";

        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            city = (CurrentCity) getIntent().getExtras().getSerializable(MainActivity.cityKey);
            new GetWeather().execute(city);
        }


        button_save =findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SaveCityWeather().execute(city);

            }
        });

         button_current =findViewById(R.id.button_current);
         button_current.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 new GetCurrentCityWeather().execute(city);
             }
         });


    }



    class GetWeather extends AsyncTask<CurrentCity,Void,ArrayList<Weather>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            imageview_cityprogress =findViewById(R.id.imageview_cityprogress);
            progressBarCity =findViewById(R.id.progressBarCity);

            imageview_cityprogress.setVisibility(View.VISIBLE);
            progressBarCity.setVisibility(View.VISIBLE);

            textview_city =findViewById(R.id.textview_savedcity);
            textview_headline =findViewById(R.id.textview_headline);
            textview_date =findViewById(R.id.textview_date);
            textview_temp =findViewById(R.id.textview_temp);
            textview_day =findViewById(R.id.textview_day);
            textview_night =findViewById(R.id.textview_night);
            textview_link =findViewById(R.id.textview_link);
            textview_condition =findViewById(R.id.textview_condition);
            textview_daytext =findViewById(R.id.textview_daytext);
            textview_nighttext =findViewById(R.id.textview_nighttext);
            imageview_day=findViewById(R.id.imageview_day);
            imageview_night=findViewById(R.id.imageview_night);
            button_save=findViewById(R.id.button_save);
            button_current=findViewById(R.id.button_current);
            recyclerView=findViewById(R.id.recyclerView);

            textview_city.setVisibility(View.INVISIBLE);
            textview_headline.setVisibility(View.INVISIBLE);
            textview_date.setVisibility(View.INVISIBLE);
            textview_temp.setVisibility(View.INVISIBLE);
            textview_day.setVisibility(View.INVISIBLE);
            textview_night.setVisibility(View.INVISIBLE);
            textview_link.setVisibility(View.INVISIBLE);
            textview_condition.setVisibility(View.INVISIBLE);
            textview_daytext.setVisibility(View.INVISIBLE);
            textview_nighttext.setVisibility(View.INVISIBLE);
            imageview_day.setVisibility(View.INVISIBLE);
            imageview_night.setVisibility(View.INVISIBLE);
            button_save.setVisibility(View.INVISIBLE);
            button_current.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);



        }

        @Override
        protected ArrayList<Weather> doInBackground(CurrentCity... arrayLists) {

            HttpURLConnection connection=null;
            String city_key=arrayLists[0].city_key;

            ArrayList<Weather> currentWeather =new ArrayList<>();

            try {
                String uri ="https://dataservice.accuweather.com/forecasts/v1/daily/5day/"+URLEncoder.encode(city_key,"UTF-8")+"?apikey="+getResources().getString(R.string.Key);

                URL url = new URL(uri);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    Log.d("demo", "VAlue got " + json);

                    JSONObject root = new JSONObject(json);

                    JSONObject headlineJSON = root.getJSONObject("Headline");


                    JSONArray dailyJSON = root.getJSONArray("DailyForecasts");

                    for(int i=0;i<dailyJSON.length();i++)
                    {
                        Weather weather =new Weather();
                        weather.city=arrayLists[0].city_name;
                        weather.headline= headlineJSON.getString("Text");

                        JSONObject dailyWeatherJSON = dailyJSON.getJSONObject(i);

                        weather.date=dailyWeatherJSON.getString("Date");

                        JSONObject tempJSON =dailyWeatherJSON.getJSONObject("Temperature");
                        JSONObject tempMinJSON =tempJSON.getJSONObject("Minimum");
                        JSONObject tempMaxJSON =tempJSON.getJSONObject("Maximum");

                        String temp=tempMaxJSON.getString("Value")+"/"+tempMinJSON.getString("Value")+" "+tempMaxJSON.getString("Unit");
                        weather.temperature=temp;

                        JSONObject dayJSON =dailyWeatherJSON.getJSONObject("Day");
                        weather.day_icon=dayJSON.getString("Icon");
                        weather.day_weather=dayJSON.getString("IconPhrase");

                        JSONObject nightJSON =dailyWeatherJSON.getJSONObject("Night");
                        weather.night_icon=nightJSON.getString("Icon");
                        weather.night_weather=nightJSON.getString("IconPhrase");

                        weather.mobilelink=dailyWeatherJSON.getString("MobileLink");


                        currentWeather.add(weather);
                    }






                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return currentWeather;
        }

        @Override
        protected void onPostExecute(final ArrayList<Weather> weathers) {
            super.onPostExecute(weathers);

            weatherArrayList = new ArrayList<>(weathers);

            imageview_cityprogress =findViewById(R.id.imageview_cityprogress);
            progressBarCity =findViewById(R.id.progressBarCity);

            imageview_cityprogress.setVisibility(View.INVISIBLE);
            progressBarCity.setVisibility(View.INVISIBLE);

            textview_city =findViewById(R.id.textview_savedcity);
            textview_headline =findViewById(R.id.textview_headline);
            textview_date =findViewById(R.id.textview_date);
            textview_temp =findViewById(R.id.textview_temp);
            textview_day =findViewById(R.id.textview_day);
            textview_night =findViewById(R.id.textview_night);
            textview_link =findViewById(R.id.textview_link);
            textview_condition =findViewById(R.id.textview_condition);
            textview_daytext =findViewById(R.id.textview_daytext);
            textview_nighttext =findViewById(R.id.textview_nighttext);
            imageview_day=findViewById(R.id.imageview_day);
            imageview_night=findViewById(R.id.imageview_night);
            button_save=findViewById(R.id.button_save);
            button_current=findViewById(R.id.button_current);
            recyclerView=findViewById(R.id.recyclerView);

            textview_city.setVisibility(View.VISIBLE);
            textview_headline.setVisibility(View.VISIBLE);
            textview_date.setVisibility(View.VISIBLE);
            textview_temp.setVisibility(View.VISIBLE);
            textview_day.setVisibility(View.VISIBLE);
            textview_night.setVisibility(View.VISIBLE);
            textview_link.setVisibility(View.VISIBLE);
            textview_condition.setVisibility(View.VISIBLE);
            textview_daytext.setVisibility(View.VISIBLE);
            textview_nighttext.setVisibility(View.VISIBLE);
            imageview_day.setVisibility(View.VISIBLE);
            imageview_night.setVisibility(View.VISIBLE);
            button_save.setVisibility(View.VISIBLE);
            button_current.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            textview_city.setText(weathers.get(0).city);
            textview_headline.setText(weathers.get(0).headline);


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = new Date();

            try {
                date = dateFormat.parse(weathers.get(0).date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMMM dd, yyyy");
            String s=dateFormat1.format(date.getTime());

            textview_date.setText("Forecast on "+s);
            textview_temp.setText("Temperature "+weathers.get(0).temperature);

            int value = Integer.parseInt(weathers.get(0).day_icon);
            String v;
            if(value<10)
            {
                v= "0"+value;
            }
            else {
                v = String.valueOf(value);
            }
            String urlToImage="https://developer.accuweather.com/sites/default/files/"+v+"-s.png";
            Picasso.get().load(urlToImage).into(imageview_day);

            int value1 = Integer.parseInt(weathers.get(0).night_icon);
            String v1;
            if(value1<10)
            {
                v1= "0"+value1;
            }
            else {
                v1 = String.valueOf(value1);
            }
            String urlToImage1="https://developer.accuweather.com/sites/default/files/"+v1+"-s.png";
            Picasso.get().load(urlToImage1).into(imageview_night);

            textview_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(weathers.get(0).mobilelink));
                    startActivity(intent);
                }
            });

            textview_day.setText(weathers.get(0).day_weather);
            textview_night.setText(weathers.get(0).night_weather);
            recyclerView.setHasFixedSize(true);

            recyclerview_layoutManager = new LinearLayoutManager(CityWeather.this,LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(recyclerview_layoutManager);


            recyclerview_adapter = new MyAdapter(weathers,CityWeather.this);
            recyclerView.setAdapter(recyclerview_adapter);


        }
    }

    @Override
    public void selectItem(final int position) {

        textview_city =findViewById(R.id.textview_savedcity);
        textview_headline =findViewById(R.id.textview_headline);
        textview_date =findViewById(R.id.textview_date);
        textview_temp =findViewById(R.id.textview_temp);
        textview_day =findViewById(R.id.textview_day);
        textview_night =findViewById(R.id.textview_night);
        textview_link =findViewById(R.id.textview_link);
        imageview_day=findViewById(R.id.imageview_day);
        imageview_night=findViewById(R.id.imageview_night);
        button_save=findViewById(R.id.button_save);
        button_current=findViewById(R.id.button_current);
        recyclerView=findViewById(R.id.recyclerView);

        textview_city.setText(weatherArrayList.get(position).city);
        textview_headline.setText(weatherArrayList.get(position).headline);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = new Date();

        try {
            date = dateFormat.parse(weatherArrayList.get(position).date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMMM dd, yyyy");
        String s=dateFormat1.format(date.getTime());

        textview_date.setText("Forecast on "+s);
        textview_temp.setText("Temperature "+weatherArrayList.get(position).temperature);

        int value = Integer.parseInt(weatherArrayList.get(position).day_icon);
        String v;
        if(value<10)
        {
            v= "0"+value;
        }
        else {
            v = String.valueOf(value);
        }
        String urlToImage="https://developer.accuweather.com/sites/default/files/"+v+"-s.png";
        Picasso.get().load(urlToImage).into(imageview_day);

        int value1 = Integer.parseInt(weatherArrayList.get(position).night_icon);
        String v1;
        if(value1<10)
        {
            v1= "0"+value1;
        }
        else {
            v1 = String.valueOf(value1);
        }
        String urlToImage1="https://developer.accuweather.com/sites/default/files/"+v1+"-s.png";
        Picasso.get().load(urlToImage1).into(imageview_night);

        textview_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(weatherArrayList.get(position).mobilelink));
                startActivity(intent);
            }
        });

        textview_day.setText(weatherArrayList.get(position).day_weather);
        textview_night.setText(weatherArrayList.get(position).night_weather);
        recyclerView.setHasFixedSize(true);

        recyclerview_layoutManager = new LinearLayoutManager(CityWeather.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(recyclerview_layoutManager);

        recyclerview_adapter = new MyAdapter(weatherArrayList,CityWeather.this);
        recyclerView.setAdapter(recyclerview_adapter);

    }

    class SaveCityWeather extends AsyncTask<CurrentCity,Void, CurrentCity> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected CurrentCity doInBackground(CurrentCity... arrayLists) {
            HttpURLConnection connection = null;
            String city_key = arrayLists[0].city_key;
            Log.d("demo", city_key);

            CurrentCity currentCity = new CurrentCity();

            try {
                String uri = "https://dataservice.accuweather.com/currentconditions/v1/" + URLEncoder.encode(city_key, "UTF-8") + "?" + "apikey=" + getResources().getString(R.string.Key);


                URL url = new URL(uri);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    Log.d("demo", "VAlue got " + json);

                    JSONArray root = new JSONArray(json);

                    JSONObject cityJSON = root.getJSONObject(0);

                    currentCity.city_key=arrayLists[0].city_key;
                    currentCity.LocalObservationDateTime = cityJSON.getString("LocalObservationDateTime");
                    currentCity.WeatherText = cityJSON.getString("WeatherText");
                    currentCity.WeatherIcon = cityJSON.getString("WeatherIcon");
                    currentCity.city_name = arrayLists[0].city_name;
                    currentCity.country = arrayLists[0].country;

                    JSONObject temp = cityJSON.getJSONObject("Temperature");

                    JSONObject tempJSON = temp.getJSONObject("Imperial");

                    currentCity.Value = tempJSON.getString("Value");
                    currentCity.Unit = tempJSON.getString("Unit");


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

            Log.d("demo", "values are : " + currentCities.toString());

            String key = currentCities.city_key;
            int flag = 1;
            for (int i = 0; i < cityGlobal.size(); i++) {
                City c = cityGlobal.get(i);
                if (c.citykey.equals(key)) {
                    String temp = "Temperature: " + currentCities.Value + " " + currentCities.Unit;
                    cityGlobal.get(i).temperature = temp;


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    Date date = new Date();

                    try {
                        date = dateFormat.parse(currentCities.LocalObservationDateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    PrettyTime p = new PrettyTime();

                    String date_time = "Updated: " + p.format(date);
                    cityGlobal.get(i).date = date_time;

                    flag = 0;

                    Toast.makeText(CityWeather.this,"City Updated",Toast.LENGTH_SHORT).show();
                    break;
                }
            }


            if (flag == 1) {

                    City cityitem = new City();

                    cityitem.citykey = currentCities.city_key;

                    String[] strings = currentCities.city_name.split(",");

                    cityitem.cityname = strings[0];
                    cityitem.country = currentCities.country;

                    String temp = "Temperature: " + currentCities.Value + " " + currentCities.Unit;
                    cityitem.temperature = temp;


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    Date date = new Date();

                    try {
                        date = dateFormat.parse(currentCities.LocalObservationDateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    PrettyTime p = new PrettyTime();

                    String date_time = "Updated: " + p.format(date);
                    cityitem.date = date_time;

                    cityitem.favorite = 0;

                    cityGlobal.add(cityitem);

                Toast.makeText(CityWeather.this,"City Saved",Toast.LENGTH_SHORT).show();

                }
            Intent i= new Intent();
            key_pass[0]="1";
            Log.d("demo","Array value in cityweather "+key_pass[0]+" "+key_pass[1]);
            i.putExtra(KEY,key_pass);
            setResult(RESULT_OK,i);
            finish();

            }

        }

    class GetCurrentCityWeather extends AsyncTask<CurrentCity,Void,CurrentCity>
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

            Log.d("demo","values are : "+currentCities.toString());



            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = new Date();

            try {
                date = dateFormat.parse(currentCities.LocalObservationDateTime);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime p  = new PrettyTime();

            String date_time= p.format(date);

            Log.d("demo","Global city values "+ currentCityGlobal.city_key+" "+currentCities.city_key);
            if(currentCityGlobal.city_key != null) {
                if (currentCityGlobal.city_key.equals(currentCities.city_key)) {
                    Log.d("demo", "INSIDE CURRENT CITY UPDATE ");
                    currentCityGlobal.WeatherText = currentCities.WeatherText;
                    currentCityGlobal.Value = currentCities.Value;
                    currentCityGlobal.Unit = currentCities.Unit;
                    currentCityGlobal.LocalObservationDateTime = date_time;
                    currentCityGlobal.WeatherIcon = currentCities.WeatherIcon;
                    Toast.makeText(CityWeather.this, "Current City Updated", Toast.LENGTH_SHORT).show();
                }
                else {

                    currentCityGlobal.city_key = currentCities.city_key;
                    String[] strings = currentCities.city_name.split(",");

                    currentCityGlobal.city_name = strings[0];
                    currentCityGlobal.country = currentCities.country;
                    currentCityGlobal.WeatherText = currentCities.WeatherText;
                    currentCityGlobal.Value = currentCities.Value;
                    currentCityGlobal.Unit = currentCities.Unit;
                    currentCityGlobal.LocalObservationDateTime = date_time;
                    currentCityGlobal.WeatherIcon = currentCities.WeatherIcon;
                    Toast.makeText(CityWeather.this,"Current City Saved",Toast.LENGTH_SHORT).show();

                }
            }
            else {

                currentCityGlobal.city_key = currentCities.city_key;
                String[] strings = currentCities.city_name.split(",");

                currentCityGlobal.city_name = strings[0];
                currentCityGlobal.country = currentCities.country;
                currentCityGlobal.WeatherText = currentCities.WeatherText;
                currentCityGlobal.Value = currentCities.Value;
                currentCityGlobal.Unit = currentCities.Unit;
                currentCityGlobal.LocalObservationDateTime = date_time;
                currentCityGlobal.WeatherIcon = currentCities.WeatherIcon;
                Toast.makeText(CityWeather.this,"Current City Saved",Toast.LENGTH_SHORT).show();

            }

            Log.d("demo","Current city "+currentCityGlobal.WeatherIcon);

            key_pass[1]="1";

        }

    }



}
