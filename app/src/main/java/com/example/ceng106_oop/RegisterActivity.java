package com.example.ceng106_oop;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextUsernameRegister, editTextPasswordRegister;
    private Button buttonRegisterSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        editTextUsernameRegister = findViewById(R.id.editTextUsernameRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        buttonRegisterSubmit = findViewById(R.id.buttonRegisterSubmit);

        // Başlangıçta +90 sabit olarak yerleştirilir
        editTextUsernameRegister.setText("+90 ");
        editTextUsernameRegister.setSelection(editTextUsernameRegister.getText().length());

        editTextUsernameRegister.addTextChangedListener(new TextWatcher() {
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
                editTextUsernameRegister.setText(formatted.toString());
                editTextUsernameRegister.setSelection(formatted.length());

                isFormatting = false;
            }
        });

        buttonRegisterSubmit.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String username = editTextUsernameRegister.getText().toString().trim();
        String password = editTextPasswordRegister.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        //
        if (!username.startsWith("+90") || username.length() != 17) {
            Toast.makeText(this, "Telefon numarası +90 ile başlamalı ve 10 haneli olmalıdır", Toast.LENGTH_SHORT).show();
            return;
        }

        UserModel user = new UserModel(username, password);

        SupabaseService service = ApiClient.getClient().create(SupabaseService.class);
        Call<Void> call = service.registerUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                    finish(); // Ana ekrana döner
                } else {
                    Toast.makeText(RegisterActivity.this, "Kayıt başarısız. Tekrar deneyin.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //
}

