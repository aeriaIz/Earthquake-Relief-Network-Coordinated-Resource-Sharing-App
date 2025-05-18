package com.example.ceng106_oop;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// biktim
public class ProfileActivity extends AppCompatActivity {
    private TextView textViewName,textViewSurname,textViewBirthDay,textViewBirthMonth,textViewBirthYear,textViewGender;
    private Button updateInfoButton;
    private final int UPDATE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        NavigationBar.setupNavigationBar(this, R.id.nav_profile);
        // TextView'leri bağla
        textViewName = findViewById(R.id.textViewName);
        textViewSurname = findViewById(R.id.textViewSurname);
        textViewBirthDay = findViewById(R.id.textViewBirthDay);
        textViewBirthMonth = findViewById(R.id.textViewBirthMonth);
        textViewBirthYear = findViewById(R.id.textViewBirthYear);
        textViewGender = findViewById(R.id.textViewGender);

        // Güncelleme butonunu bağla
        updateInfoButton = findViewById(R.id.buttonUpdateInfo);

        //displayUserInfoFromIntent(getIntent());

        String userId = getIntent().getStringExtra("id");
        if (userId != null) {
            fetchUserFromSupabase(userId);
        } else {
            Toast.makeText(this, "Kullanıcı ID alınamadı!", Toast.LENGTH_SHORT).show();
        }



        updateInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateInfoActivity.class);
            intent.putExtra("id", userId); // UUID olan kullanıcı id'si
            // Mevcut verileri intent ile geç
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("name", textViewName.getText().toString());
            intent.putExtra("surname", textViewSurname.getText().toString());
            intent.putExtra("birthday", Integer.parseInt(textViewBirthDay.getText().toString()));
            intent.putExtra("birthmonth", Integer.parseInt(textViewBirthMonth.getText().toString()));
            intent.putExtra("birthyear", Integer.parseInt(textViewBirthYear.getText().toString()));
            intent.putExtra("gender", textViewGender.getText().toString());

            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });
    }
    private void displayUserInfoFromIntent(Intent intent) {
        textViewName.setText(intent.getStringExtra("name"));
        textViewSurname.setText(intent.getStringExtra("surname"));
        textViewBirthDay.setText(String.valueOf(intent.getIntExtra("birthday", 0)));
        textViewBirthMonth.setText(String.valueOf(intent.getIntExtra("birthmonth", 0)));
        textViewBirthYear.setText(String.valueOf(intent.getIntExtra("birthyear", 0)));
        textViewGender.setText(intent.getStringExtra("gender"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Güncellenen verileri al ve göster
            displayUserInfoFromIntent(data);
        }
    }
    private void fetchUserFromSupabase(String userId) {
        SupabaseService service = ApiClient.getClient().create(SupabaseService.class);
        Call<List<UserModel>> call = service.getUserById("eq." + userId);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    UserModel user = response.body().get(0);

                    textViewName.setText(user.getName());
                    textViewSurname.setText(user.getSurname());
                    textViewBirthDay.setText(String.valueOf(user.getBirthday()));
                    textViewBirthMonth.setText(String.valueOf(user.getBirthmonth()));
                    textViewBirthYear.setText(String.valueOf(user.getBirthyear()));
                    textViewGender.setText(user.getGender());
                } else {
                    Toast.makeText(ProfileActivity.this, "Kullanıcı bilgileri alınamadı!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Sunucu hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //
}

