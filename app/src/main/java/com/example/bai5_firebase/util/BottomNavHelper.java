package com.example.bai5_firebase.util;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bai5_firebase.MainActivity;
import com.example.bai5_firebase.MyTicketsActivity;
import com.example.bai5_firebase.ProfileActivity;
import com.example.bai5_firebase.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class BottomNavHelper {
    private BottomNavHelper() {
    }

    public static void setup(AppCompatActivity activity, int selectedItemId) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNavMain);
        if (bottomNav == null) {
            return;
        }
        bottomNav.setSelectedItemId(selectedItemId);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == selectedItemId) {
                return true;
            }
            Intent intent;
            if (itemId == R.id.nav_home) {
                intent = new Intent(activity, MainActivity.class);
            } else if (itemId == R.id.nav_tickets) {
                intent = new Intent(activity, MyTicketsActivity.class);
            } else if (itemId == R.id.nav_account) {
                intent = new Intent(activity, ProfileActivity.class);
            } else {
                return false;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            return true;
        });
    }
}
