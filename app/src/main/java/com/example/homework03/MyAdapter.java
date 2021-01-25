package com.example.homework03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context ctx;
    public static interactWithCityWeather interact;
    ArrayList<Weather> weather;

    public MyAdapter(ArrayList<Weather> weather,Context ctx) {
        this.weather = weather;
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout recyclerview_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.column_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(recyclerview_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        interact =(interactWithCityWeather) ctx;

        int value = Integer.parseInt(weather.get(position).day_icon);
        String v;
        if(value<10)
        {
            v= "0"+value;
        }
        else {
            v = String.valueOf(value);
        }
        String urlToImage="https://developer.accuweather.com/sites/default/files/"+v+"-s.png";
        Picasso.get().load(urlToImage).into(holder.imageview_weather);
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date= new Date();
        try {
            date=dateFormat.parse(weather.get(position).date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 =new SimpleDateFormat("dd MMM''yy");
        String s=dateFormat1.format(date.getTime());
        holder.textview_date.setText(s);

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.selectItem(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return weather.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview_weather;
        TextView textview_date;
        LinearLayout linearlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview_weather = itemView.findViewById(R.id.imageview_weather);
            textview_date = itemView.findViewById(R.id.textview_date);
            linearlayout =itemView.findViewById(R.id.linearlayout_save);


        }

    }

    public interface interactWithCityWeather
    {
        void selectItem(final int position);
    }
}
