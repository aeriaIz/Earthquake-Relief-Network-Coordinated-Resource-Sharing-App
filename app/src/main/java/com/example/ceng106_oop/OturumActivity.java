package com.example.ceng106_oop;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import spes.example.oop_proje.ProfileActivity;
//import spes.example.oop_proje.SecondActivity;

public class OturumActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private SupabaseService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oturum);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Başlangıçta +90 sabit olarak yerleştirilir
        editTextUsername.setText("+90 ");
        editTextUsername.setSelection(editTextUsername.getText().length());

        editTextUsername.addTextChangedListener(new TextWatcher() {
            boolean isFormatting;
            int prevLength;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;

                String raw = s.toString();

                // Eğer kullanıcı +90 silmeye çalıştıysa geri ekle
                if (!raw.startsWith("+90")) {
                    raw = "+90";
                }

                // Rakamları al (ilk 3 karakter olan +90 dışındakileri filtrele)
                String digits = raw.replaceAll("[^\\d]", "");
                if (digits.startsWith("90")) {
                    digits = digits.substring(2); // +90'ı çıkar, sadece numara kalsın
                }

                // Format: 555 555 55 55
                StringBuilder formatted = new StringBuilder("+90 ");
                for (int i = 0; i < digits.length() && i < 10; i++) {
                    formatted.append(digits.charAt(i));
                    if (i == 2 || i == 5 || i == 7) {
                        formatted.append(" ");
                    }
                }

                // Yeni formatı ayarla
                editTextUsername.setText(formatted.toString());
                editTextUsername.setSelection(formatted.length());

                isFormatting = false;
            }
        });

        service = ApiClient.getClient().create(SupabaseService.class);

        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(OturumActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(view -> loginUser());
    }
    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }
        // Supabase'de kullanıcıyı sorgulamak için filtreli GET isteği
        service.getUsersByUsername("eq." + username, "*").enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    UserModel user = response.body().get(0);
                    if (user.getPassword().equals(password)) {
                        Toast.makeText(OturumActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();

                        // 🔽 BURASI: user_id'yi SharedPreferences'e kaydet
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("user_id", user.getId()); // UUID burada geliyor
                        editor.apply();

                        // Kullanıcının kayıt formunu daha önce doldurup doldurmadığını kontrol et
                        if (user.getName() != null && !user.getName().isEmpty()) {
                            // Kayıt formu zaten doldurulmuş → ProfileActivity'e yönlendir
                            Intent intent = new Intent(OturumActivity.this, ProfileActivity.class);
                            intent.putExtra("id", user.getId());
                            startActivity(intent);
                            finish();

                        } else {
                            // Kayıt formu henüz doldurulmamış → SecondActivity'e yönlendir
                            Intent intent = new Intent(OturumActivity.this, SecondActivity.class);
                            intent.putExtra("id", user.getId());
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(OturumActivity.this, "Şifre yanlış!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OturumActivity.this, "Kullanıcı bulunamadı! Hata kodu: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(OturumActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
      //
    }
}
