package com.example.demo.util;

import android.icu.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,new  DateDeserializer() ).create();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")

            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    private RetrofitManager(){}
    public static Retrofit getRetrofit() {
        return retrofit;
    }
    private static class DateDeserializer implements JsonDeserializer<Date> {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json != null) {
                final String jsonString = json.getAsString();
                try {
                    return (Date) dateFormat.parse(jsonString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final long jsonLong= json.getAsLong();
                try {
                    return new Date(jsonLong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
