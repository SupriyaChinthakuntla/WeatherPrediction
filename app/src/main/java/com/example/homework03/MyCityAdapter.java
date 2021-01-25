package com.example.homework03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyCityAdapter extends RecyclerView.Adapter<MyCityAdapter.ViewHolder> {

    ArrayList<City> cities;
    Context ctx;
    public static InteractWithMainActivity interact;

    public MyCityAdapter(ArrayList<City> cities, Context ctx) {
        this.cities = cities;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout recyclerview_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyCityAdapter.ViewHolder viewHolder = new MyCityAdapter.ViewHolder(recyclerview_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        interact =(InteractWithMainActivity)ctx;

        holder.textview_savedcity.setText(cities.get(position).cityname+", "+cities.get(position).country);
        holder.textview_savedtemp.setText(cities.get(position).temperature);
        holder.textview_savedtime.setText(cities.get(position).date);
        if (cities.get(position).favorite==0)
        {
            holder.checkBox.setChecked(false);
        }
        else
        {
            holder.checkBox.setChecked(true);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cities.get(position).favorite==0)
                {
                    cities.get(position).favorite=1;
                }
                else if(cities.get(position).favorite==1)
                {
                    cities.get(position).favorite=0;
                }
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                interact.deleteItem(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textview_savedcity;
        TextView textview_savedtemp;
        TextView textview_savedtime;
        CheckBox checkBox;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_savedcity = itemView.findViewById(R.id.textview_savedcity);
            textview_savedtemp =itemView.findViewById(R.id.textview_savedtemp);
            textview_savedtime =itemView.findViewById(R.id.textview_savedtime);
            checkBox =itemView.findViewById(R.id.checkBox);
            linearLayout =itemView.findViewById(R.id.linearlayout_save);


        }
    }

    public interface InteractWithMainActivity
    {
        void deleteItem(int position);
    }
}
