package com.example.ceng106_oop;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationBar {

    public static void setupNavigationBar(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        if (bottomNavigationView == null) {
            Log.e("NavigationBar", "bottom_navigation bulunamadı!");
            return;
        }

        // 🔁 Aktif ikonu elle ayarla
        bottomNavigationView.setSelectedItemId(selectedItemId);

        // ➕ Intent ile gelen kullanıcı id'yi al
        Intent currentIntent = activity.getIntent();
        String userId = currentIntent.getStringExtra("id");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_sender && !(activity instanceof ListActivity)) {
                intent = new Intent(activity, ListActivity.class);
            } else if (id == R.id.nav_new_request && !(activity instanceof NeedsFormActivity)) {
                intent = new Intent(activity, NeedsFormActivity.class);
            } else if (id == R.id.nav_profile && !(activity instanceof ProfileActivity)) {
                intent = new Intent(activity, ProfileActivity.class);
            } else if (id == R.id.nav_my_requests && !(activity instanceof TakipSayfasiActivity)) {
                intent = new Intent(activity, TakipSayfasiActivity.class);
            }

            if (intent != null) {
                if (userId != null) {
                    intent.putExtra("id", userId); // id kaybolmasın
                }
                activity.startActivity(intent);
                activity.finish();
                return true;
            }
            return false;
        });
    }
}