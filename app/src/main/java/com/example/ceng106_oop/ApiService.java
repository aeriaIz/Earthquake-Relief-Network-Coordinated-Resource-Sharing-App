package com.example.ceng106_oop;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

//import spes.myapplication.model.modelIhtiyacItem;
//import spes.myapplication.model.ToplanmaAlani;

public interface ApiService {

    // ihtiyac_listesi tablosundaki tüm verileri al
    @GET("needs")
    Call<List<modelIhtiyacItem>> getNeedsList();

    // toplanma_alanlari tablosundaki tüm verileri al

    @GET("ihtiyac_listesi")
    Call<List<modelIhtiyacItem>> getIhtiyacListesiByKategori();

    @PATCH("needs")
    Call<Void> updateStatus(@Query("id") String id, @Body Map<String, String> statusMap);


}