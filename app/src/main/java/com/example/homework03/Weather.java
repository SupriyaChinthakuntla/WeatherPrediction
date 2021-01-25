package com.example.homework03;

import androidx.annotation.NonNull;

public class Weather {

    String city;
    String headline;
    String date;
    String temperature;
    String day_icon;
    String day_weather;
    String night_icon;
    String night_weather;
    String mobilelink;


    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", headline='" + headline + '\'' +
                ", date='" + date + '\'' +
                ", temperature='" + temperature + '\'' +
                ", day_icon='" + day_icon + '\'' +
                ", day_weather='" + day_weather + '\'' +
                ", night_icon='" + night_icon + '\'' +
                ", night_weather='" + night_weather + '\'' +
                ", mobilelink='" + mobilelink + '\'' +
                '}';
    }
}
