package com.example.ceng106_oop;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.oturum_sayfasi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UpdateInfoActivity extends AppCompatActivity {
    private EditText editTextName, editTextSurname, editTextDay, editTextMonth, editTextYear;
    private Spinner genderSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextDay = findViewById(R.id.editTextDay);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextYear = findViewById(R.id.editTextYear);
        genderSpinner = findViewById(R.id.spinnerGender);
        saveButton = findViewById(R.id.buttonSave);

        //Spinner verilerini yükle
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Erkek", "Kadın", "Diğer"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // JSON'dan gelen verileri al
        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        int day = getIntent().getIntExtra("birthday", 0);
        int month = getIntent().getIntExtra("birthmonth", 0);
        int year = getIntent().getIntExtra("birthyear", 0);
        String gender = getIntent().getStringExtra("gender");

        // Verileri editText'lere yerleştir
        editTextName.setText(name);
        editTextSurname.setText(surname);
        editTextDay.setText(String.valueOf(day));
        editTextMonth.setText(String.valueOf(month));
        editTextYear.setText(String.valueOf(year));
        genderSpinner.setSelection(getGenderPosition(gender));

// Kaydet butonuna tıklama işlemi
        saveButton.setOnClickListener(v -> {
            // EditText'lerden verileri al
            String updatedName = editTextName.getText().toString().trim();
            String updatedSurname = editTextSurname.getText().toString().trim();
            String dayStr = editTextDay.getText().toString().trim();
            String monthStr = editTextMonth.getText().toString().trim();
            String yearStr = editTextYear.getText().toString().trim();

            // Alanlar boş mu kontrol et
            if (updatedName.isEmpty() || updatedSurname.isEmpty() ||
                    dayStr.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty()) {
                Toast.makeText(UpdateInfoActivity.this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Doğum tarihi girişlerinin sayısal ve geçerli olduğundan emin ol
            int updatedDay, updatedMonth, updatedYear;
            try {
                updatedDay = Integer.parseInt(dayStr);
                updatedMonth = Integer.parseInt(monthStr);
                updatedYear = Integer.parseInt(yearStr);

                if (updatedDay < 1 || updatedDay > 31 || updatedMonth < 1 || updatedMonth > 12 || updatedYear < 1900) {
                    Toast.makeText(UpdateInfoActivity.this, "Geçerli bir doğum tarihi giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(UpdateInfoActivity.this, "Doğum tarihi sadece sayı olmalıdır.", Toast.LENGTH_SHORT).show();
                return;
            }

            String updatedGender = genderSpinner.getSelectedItem().toString();
            String username = getIntent().getStringExtra("username");

            Log.d("UpdateInfoActivity", "Updated Data: " +
                    "Username:" + username +
                    "  Name: " + updatedName +
                    ", Surname: " + updatedSurname +
                    ", Day: " + updatedDay +
                    ", Month: " + updatedMonth +
                    ", Year: " + updatedYear +
                    ", Gender: " + updatedGender);

            updateUserInfoOnSupabase(username, updatedName, updatedSurname, updatedGender, updatedDay, updatedMonth, updatedYear);
        });


    }

    private int getGenderPosition(String gender) {
        if (gender.equals("Kadın")) {
            return 1;
        } else if (gender.equals("Diğer")) {
            return 2;
        }
        return 0; // Erkek
    }

    private void updateUserInfoOnSupabase(String id, String name, String surname, String gender, int day, int month, int year) {
        id = getIntent().getStringExtra("id"); // UUID olarak gelen id alınmalı
        String url = "https://dcolkghkjgbriquchvza.supabase.co/rest/v1/users?id=eq." + id;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("surname", surname);
            jsonBody.put("gender", gender);
            jsonBody.put("birthday", day);
            jsonBody.put("birthmonth", month);
            jsonBody.put("birthyear", year);
        } catch (JSONException e) {
            Log.e("UpdateInfoActivity", "JSON Exception: " + e.getMessage());
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.PATCH, url,
                response -> {
                    Log.d("UpdateInfoActivity", "Güncelleme başarılı!");
                    Toast.makeText(this, "Güncelleme başarılı!", Toast.LENGTH_SHORT).show();

                    // Activity'ye güncel verileri geri gönder
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("surname", surname);
                    resultIntent.putExtra("gender", gender);
                    resultIntent.putExtra("birthday", day);
                    resultIntent.putExtra("birthmonth", month);
                    resultIntent.putExtra("birthyear", year);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                },
                error -> {
                    Log.e("Volley Error", "Error Message: " + error.getMessage());
                    if (error.networkResponse != null) {
                        Log.e("Volley Error", "Error Response: " + new String(error.networkResponse.data));
                    }
                    Toast.makeText(this, "Güncelleme Hatası: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg"); // güvenlik için bir ENV dosyasında tutmanı öneririm
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRjb2xrZ2hramdicmlxdWNodnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4MDgyNjgsImV4cCI6MjA2MjM4NDI2OH0.H1w4ZUG24RATNi3_At778X2c01R-twxpvKz8mBHLrYg");
                headers.put("Content-Type", "application/json");
                headers.put("Prefer", "return=representation");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}