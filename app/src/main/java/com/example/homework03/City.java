package com.example.homework03;

public class City {
    String citykey;
    String cityname;
    String country;
    String temperature;
    int favorite;
    String date;

    @Override
    public String toString() {
        return "City{" +
                "citykey='" + citykey + '\'' +
                ", cityname='" + cityname + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", favorite=" + favorite +
                ", date='" + date + '\'' +
                '}';
    }
}
