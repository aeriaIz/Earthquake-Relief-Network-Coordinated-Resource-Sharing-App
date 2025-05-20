package com.example.ceng106_oop;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NeedsFormActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCategory, autoItem;
    private EditText editProvince, editDistrict, editNeighborhood, editStreet, editBuilding;
    private Button btnSave;
    private final Map<String, List<String>> itemMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_needs_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Talep Formu");

        NavigationBar.setupNavigationBar(this, R.id.nav_new_request);

        autoCategory = findViewById(R.id.autoCategory);
        autoItem = findViewById(R.id.autoItem);
        editProvince = findViewById(R.id.editProvince);
        editDistrict = findViewById(R.id.editDistrict);
        editNeighborhood = findViewById(R.id.editNeighborhood);
        editStreet = findViewById(R.id.editStreet);
        editBuilding = findViewById(R.id.editBuilding);
        btnSave = findViewById(R.id.btnSave);

        setupDropdowns();

        btnSave.setOnClickListener(v -> {
            Log.d("DEBUG", "==> KAYDET BUTONUNA BASILDI");
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            Log.d("DEBUG", "user_id gönderiliyor: " + userId);

            btnSave.setEnabled(false);
            btnSave.setText("Gönderiliyor...");
            String category = autoCategory.getText().toString().trim();
            String item = autoItem.getText().toString().trim();
            String province = editProvince.getText().toString().trim();
            String district = editDistrict.getText().toString().trim();
            String neighborhood = editNeighborhood.getText().toString().trim();
            String street = editStreet.getText().toString().trim();
            String building = editBuilding.getText().toString().trim();

            if (category.isEmpty() || item.isEmpty()) {
                Toast.makeText(this, "Lütfen kategori ve öğe seçiniz.", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
                btnSave.setText("Kaydet");
                return;
            }

            boolean hasError = false;

            if (province.isEmpty()) {
                editProvince.setError("İl boş bırakılamaz");
                hasError = true;
            }
            if (district.isEmpty()) {
                editDistrict.setError("İlçe boş bırakılamaz");
                hasError = true;
            }
            if (neighborhood.isEmpty()) {
                editNeighborhood.setError("Mahalle boş bırakılamaz");
                hasError = true;
            }
            if (street.isEmpty()) {
                editStreet.setError("Sokak boş bırakılamaz");
                hasError = true;
            }
            if (building.isEmpty()) {
                editBuilding.setError("Bina/No boş bırakılamaz");
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
                btnSave.setText("Kaydet");
                return;
            }




            if (userId == null) {
                Toast.makeText(this, "Kullanıcı oturumu bulunamadı!", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
                btnSave.setText("Kaydet");
                return;
            }

            // Veriyi Supabase'e gönder
            Needs newNeed = new Needs();
            newNeed.setUser_id(userId);
            newNeed.setCategory(category);
            newNeed.setItem(item);
            newNeed.setProvince(province);
            newNeed.setDistrict(district);
            newNeed.setNeighborhood(neighborhood);
            newNeed.setStreet(street);
            newNeed.setBuilding_info(building);
            newNeed.setStatus("beklemede");

            // Retrofit ile Supabase API’ye bağlantı hazırlanır
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://dcolkghkjgbriquchvza.supabase.co/rest/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // API servisi oluşturulur
            SupabaseServiceForNeeds service = retrofit.create(SupabaseServiceForNeeds.class);

            // API'ye POST isteği gönderilir
            service.addNeed(newNeed).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("Kaydet");
                    if (response.isSuccessful()) {
                        Toast.makeText(NeedsFormActivity.this, "Başarıyla gönderildi!", Toast.LENGTH_SHORT).show();
                        // Eğer istek başarılıysa form temizlenir
                        autoCategory.setText("");
                        autoItem.setText("");
                        editProvince.setText("");
                        editDistrict.setText("");
                        editNeighborhood.setText("");
                        editStreet.setText("");
                        editBuilding.setText("");
                        // Kategori değiştiğinde item listesi de temizlenir
                        autoItem.setAdapter(null);
                    } else {
                        Toast.makeText(NeedsFormActivity.this, "Sunucu hatası: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("Kaydet");
                    Toast.makeText(NeedsFormActivity.this, "Bağlantı hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupDropdowns() {
        // Kategorilere karşılık gelen ürün listeleri eşleştirilir
        itemMap.put("Gıda", Arrays.asList("Su", "Konserve", "Kuru Gıda", "Bebek Maması"));
        itemMap.put("Kıyafet", Arrays.asList("Mont", "Ayakkabı", "Bebek Kıyafeti"));
        itemMap.put("Hijyen", Arrays.asList("Hijyen Kiti", "Sabun", "Islak Mendil"));

        // Kategori adapter’i hazırlanır ve dropdown’a bağlanır
        List<String> categoryList = new ArrayList<>(itemMap.keySet());

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categoryList);
        autoCategory.setAdapter(categoryAdapter);

        // Kategori seçildiğinde ilgili ürün listesi dropdown’a yüklenir
        autoCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = parent.getItemAtPosition(position).toString();
            List<String> items = itemMap.get(selectedCategory);

            ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, items);
            autoItem.setAdapter(itemAdapter);
            autoItem.setText(""); // Önceki seçim temizlenir
        });
    }
}
