// kullanici kayit ekrani
package com.example.ceng106_oop;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.oturum_sayfasi.ApiClient;
//import com.example.oturum_sayfasi.R;
//import com.example.oturum_sayfasi.SupabaseService;
//import com.example.oturum_sayfasi.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {

    private EditText editTextName, editTextSurname, editTextDay, editTextMonth, editTextYear;
    private Spinner genderSpinner;
    private String userId; // UUID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextDay = findViewById(R.id.editTextDay);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextYear = findViewById(R.id.editTextYear);
        genderSpinner = findViewById(R.id.spinnerGender);

        userId = getIntent().getStringExtra("id");

        //KVKK metnini göster
        TextView textViewShowKvkk = findViewById(R.id.textViewShowKvkk);

        textViewShowKvkk.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
            builder.setTitle("KVKK Metni");

            builder.setMessage("Bu uygulama kapsamında kişisel verileriniz, yalnızca afet durumlarında yardım amacıyla kullanılmak üzere işlenecektir. Kullanım koşullarını ve veri işleme politikalarını onayladığınızda bilgileriniz sistemimize kaydedilecektir.");

            builder.setPositiveButton("Kapat", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // Spinner verilerini yükle
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Kadın","Erkek", "Diğer"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        Button submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(v -> {
            saveUserToSupabase();
        });
    }
    private void saveUserToSupabase() {
        Log.d("SecondActivity", "saveUserToSupabase başladı");


        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        // Text alanları boş mu?
        if (name.isEmpty()) {
            editTextName.setError("İsim boş bırakılamaz");
            return;
        }
        if (surname.isEmpty()) {
            editTextSurname.setError("Soyisim boş bırakılamaz");
            return;
        }

        int day, month, year;
        try {
            day = Integer.parseInt(editTextDay.getText().toString().trim());
            month = Integer.parseInt(editTextMonth.getText().toString().trim());
            year = Integer.parseInt(editTextYear.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Doğum tarihi boş bırakılamaz!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gün, ay ve yıl değerleri geçerli mi?
        if (day < 1 || day > 31) {
            editTextDay.setError("Gün 1 ile 31 arasında olmalıdır");
            return;
        }
        if (month < 1 || month > 12) {
            editTextMonth.setError("Ay 1 ile 12 arasında olmalıdır");
            return;
        }
        if (year < 1900 || year > 2025) {
            editTextYear.setError("Yıl 1900 ile 2025 arasında olmalıdır");
            return;
        }
        CheckBox checkKvkk = findViewById(R.id.checkKvkk);
        if (!checkKvkk.isChecked()) {
            Toast.makeText(this, "KVKK onayını kabul etmeniz gerekmektedir!", Toast.LENGTH_SHORT).show();
            Log.e("SecondActivity", "KVKK işareti eksik");
            return;
        }

        Log.d("SecondActivity", "Veriler kontrol edildi, JSON hazırlanıyor");


        UserModel updatedUser = new UserModel();
        updatedUser.setName(name);
        updatedUser.setSurname(surname);
        updatedUser.setGender(gender);
        updatedUser.setBirthday(day);
        updatedUser.setBirthmonth(month);
        updatedUser.setBirthyear(year);

        SupabaseService service = ApiClient.getClient().create(SupabaseService.class);
        Call<Void> call = service.updateUserById("eq." + userId, updatedUser);


        Log.d("SecondActivity", "Retrofit çağrısı gönderiliyor");

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("SecondActivity", "Sunucu yanıtı: " + response);

                if (response.isSuccessful()) {
                    // Kayıt veya güncelleme işlemi başarılıysa:
                    SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id", userId);
                    editor.apply();

                    Toast.makeText(SecondActivity.this, "Bilgiler başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
                    // Profil ekranına geçiş yapılabilir
                    Intent intent = new Intent(SecondActivity.this, ProfileActivity.class);
                    intent.putExtra("id", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("SecondActivity", "Güncelleme başarısız. Kod: " + response.code());
                    Toast.makeText(SecondActivity.this, "Güncelleme başarısız! Kod: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SecondActivity", "Retrofit HATA: " + t.getMessage());
                Toast.makeText(SecondActivity.this, "Sunucu hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

