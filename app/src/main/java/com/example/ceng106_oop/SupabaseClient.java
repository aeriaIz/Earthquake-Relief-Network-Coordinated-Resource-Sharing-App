package com.example.ceng106_oop;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class SupabaseClient {

    private static final String BASE_URL = "https://dcolkghkjgbriquchvza.supabase.co/rest/v1/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg";  // <- Buraya kendi API key’ini koy
    private static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg"; // <- Eğer farklıysa, buraya Bearer token

    private static Retrofit retrofit= null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("apikey", API_KEY)
                                .addHeader("Authorization", "Bearer " + API_KEY)
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Prefer", "return=representation")
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
