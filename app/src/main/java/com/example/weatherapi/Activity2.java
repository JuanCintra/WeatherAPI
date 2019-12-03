package com.example.weatherapi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Activity2 extends AppCompatActivity {
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab,fab2;

        fab = findViewById(R.id.fab_delete);
        fab2 = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_left, R.anim.slide_oright);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, Activity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_oleft);
            }
        });

    }

    class Weather extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... address) {

            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }



    public void search(final View view){
        TextView textView;
        TextView temp;
        TextView desc;
        TextView city;
        ImageView icon;
        Button searchbtn;
        textView = findViewById(R.id.isrCidade);
        temp = findViewById(R.id.temp);
        searchbtn = findViewById(R.id.button);
        city = findViewById(R.id.city);
        desc = findViewById(R.id.desc);
        icon = findViewById(R.id.icon);

        //test
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search(view);

                overridePendingTransition(R.anim.slide_right, R.anim.slide_oleft);
            }
        });

        String cidade = textView.getText().toString();

        String content;
        Activity2.Weather weather = new Activity2.Weather();
        try {
            content = weather.execute("https://api.openweathermap.org/data/2.5/weather?q=" + cidade +"&APPID=4831ce3a3046ce07f33ef558ece68952&units=metric").get();
            Log.i("content",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            Log.i("weatherData",weatherData);

            JSONArray array = new JSONArray(weatherData);
            String description = "";
            String tempS = "";
            String main = "";

            for(int i=0; i<array.length(); i++){
                JSONObject weatherPart = array.getJSONObject(i);
                JSONObject main_main = jsonObject.getJSONObject("main");
                main =  weatherPart.getString("main");
                tempS = String.valueOf(main_main.getDouble("temp"));

                description = weatherPart.getString("description");
            }

            //Log.i("main", main);
            Log.i("description", description);
            temp.setText(tempS +"º");
            city.setText(cidade.toUpperCase());
            if(main.equalsIgnoreCase("clouds")){
                icon.setImageResource(R.drawable.cloudy);
                desc.setText("Pouco nublado");
            }
            if(main.equalsIgnoreCase("clear")){
                icon.setImageResource(R.drawable.sun);
                desc.setText("Céu limpo");
            }
            if(main.equalsIgnoreCase("Thunderstorm")){
                icon.setImageResource(R.drawable.storm);
                desc.setText("Tempestade");
            }
            if(main.equalsIgnoreCase("Rain")){
                icon.setImageResource(R.drawable.rain);
                desc.setText("Chuva");
            }
            if(main.equalsIgnoreCase("Mist")){
                icon.setImageResource(R.drawable.cloudy);
                desc.setText("Névoa");
            }
            if(main.equalsIgnoreCase("Haze")){
                icon.setImageResource(R.drawable.cloudy);
                desc.setText("Névoa");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
