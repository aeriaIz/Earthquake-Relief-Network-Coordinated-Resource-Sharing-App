package com.example.ceng106_oop;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SupabaseService {

    @Headers({
            "Content-Type: application/json",
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Prefer: return=minimal"
    })
    @POST("users")
    Call<Void> registerUser(@Body UserModel user);

    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg"
    })
    @GET("users")
    Call<List<UserModel>> getUsersByUsername(@Query("username") String username, @Query("select") String select);


    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Content-Type: application/json"
    })
    @PATCH("users")
    Call<Void> updateUserById(@Query("id") String id, @Body UserModel user);
    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg"
    })
    @GET("users")
    Call<List<UserModel>> getUserById(@Query("id") String id);
    //

}

