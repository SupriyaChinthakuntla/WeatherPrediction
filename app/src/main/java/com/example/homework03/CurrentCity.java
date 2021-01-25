package com.example.homework03;

import java.io.Serializable;

public class CurrentCity implements Serializable {

    String city_name;
    String country;
    String city_key;
    String LocalObservationDateTime;
    String WeatherText;
    String WeatherIcon;
    String Value;
    String Unit;


    @Override
    public String toString() {
        return "CurrentCity{" +
                "city_name='" + city_name + '\'' +
                ", country='" + country + '\'' +
                ", city_key='" + city_key + '\'' +
                ", LocalObservationDateTime='" + LocalObservationDateTime + '\'' +
                ", WeatherText='" + WeatherText + '\'' +
                ", WeatherIcon='" + WeatherIcon + '\'' +
                ", Value='" + Value + '\'' +
                ", Unit='" + Unit + '\'' +
                '}';
    }
}
