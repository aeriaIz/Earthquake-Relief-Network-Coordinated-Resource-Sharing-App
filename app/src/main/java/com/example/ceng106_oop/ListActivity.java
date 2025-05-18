package com.example.ceng106_oop;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private EditText aramaEditText;
    private Button araButton;
    private Button yardimGonderButton;
    private String seciliIhtiyac = null;

    private List<String> veriAdiListesi = new ArrayList<>();
    private List<modelIhtiyacItem> ihtiyaclar = new ArrayList<>();
    modelIhtiyacItem selectedItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        NavigationBar.setupNavigationBar(this, R.id.nav_sender);



        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, veriAdiListesi);
        listView.setAdapter(adapter);

        yardimGonderButton = findViewById(R.id.yardimGonderButton);
        yardimGonderButton.setEnabled(false);
        yardimGonderButton.setOnClickListener(v -> {

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userId = prefs.getString("user_id",null);

            if (selectedItem == null) {
                Toast.makeText(getApplicationContext(), "Lütfen önce bir ihtiyaç seçin.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (seciliIhtiyac != null) {
                Toast.makeText(ListActivity.this, "Yardım siz tarafından gönderiliyor", Toast.LENGTH_SHORT).show();
            }
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Map<String, String> body = new HashMap<>();
            body.put("status", "yolda");
            body.put("sender_id", userId);

            apiService.updateStatus("eq." + selectedItem.id, body).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ListActivity.this, "Yardımınız yola çıktı!", Toast.LENGTH_SHORT).show();
                        Log.d("API", "Status başarıyla güncellendi.");
                        verileriGetir();
                    } else {
                        Log.e("API", "Başarısız yanıt: " + response.code());
                        Toast.makeText(ListActivity.this, "Durum güncellenemedi. Tekrar deneyin.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API", "Hata oluştu: " + t.getMessage());
                    Toast.makeText(ListActivity.this, "Sunucu hatası oluştu.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            seciliIhtiyac = veriAdiListesi.get(position);
            selectedItem = ihtiyaclar.get(position);
            yardimGonderButton.setEnabled(true);
        });

        aramaEditText = findViewById(R.id.aramaEditText);
        araButton = findViewById(R.id.araButton);
        araButton.setEnabled(false); // Başta pasif

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getNeedsList().enqueue(new Callback<List<modelIhtiyacItem>>() {
            @Override
            public void onResponse(Call<List<modelIhtiyacItem>> call, Response<List<modelIhtiyacItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ihtiyaclar = response.body();
                    araButton.setEnabled(true);

                    for (modelIhtiyacItem item : ihtiyaclar) {
                        if (item.item != null) {
                            if(Objects.equals(item.status, "beklemede")){
                                String adres = item.neighborhood + " mahallesi, " +
                                        item.street + " caddesi, No:" +
                                        item.building_info + ", " +
                                        item.province + "/" + item.district;

                                veriAdiListesi.add(item.item + " - " + adres);
                            }

                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<modelIhtiyacItem>> call, Throwable t) {
                Log.e("API Error", "Needs listesi alınamadı: " + t.getMessage());
            }
        });

        // Arama işlemi
        araButton.setOnClickListener(v -> {
            String aramaTerimi = aramaEditText.getText().toString().trim().toLowerCase();
            veriAdiListesi.clear();

            for (modelIhtiyacItem item : ihtiyaclar) {
                if (item.item != null &&
                        item.item.toLowerCase().contains(aramaTerimi) &&
                        "beklemede".equals(item.status)) {

                    String adres = item.neighborhood + " mahallesi, " +
                            item.street + " caddesi, No:" +
                            item.building_info + ", " +
                            item.province + "/" + item.district;

                    veriAdiListesi.add(item.item + " - " + adres);
                }
            }

            adapter.notifyDataSetChanged();
        });
        verileriGetir();
    }
    private void verileriGetir() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getNeedsList().enqueue(new Callback<List<modelIhtiyacItem>>() {
            @Override
            public void onResponse(Call<List<modelIhtiyacItem>> call, Response<List<modelIhtiyacItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ihtiyaclar = response.body();
                    veriAdiListesi.clear(); // öncekileri sil

                    for (modelIhtiyacItem item : ihtiyaclar) {
                        if (item.item != null && "beklemede".equals(item.status)) {
                            String adres = item.neighborhood + " mahallesi, " +
                                    item.street + " caddesi, No:" +
                                    item.building_info + ", " +
                                    item.province + "/" + item.district;

                            veriAdiListesi.add(item.item + " - " + adres);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    yardimGonderButton.setEnabled(false); // eski seçim temizlenir
                }
            }

            @Override
            public void onFailure(Call<List<modelIhtiyacItem>> call, Throwable t) {
                Log.e("API Error", "Liste alınamadı: " + t.getMessage());
            }
        });
    }

}