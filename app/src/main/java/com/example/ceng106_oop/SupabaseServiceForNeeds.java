package com.example.ceng106_oop;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// Bu Interface, Retrofit kütüphanesi üzerinden Supabase REST API’ye ihtiyaç verilerini POST etmek için kullanılır.
public interface SupabaseServiceForNeeds {

    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("needs")
    //addNeed(@Body Needs need) metodu ile Needs nesnesi JSON formatında API'ye gönderilir.
    Call<Void> addNeed(@Body Needs need);
}
