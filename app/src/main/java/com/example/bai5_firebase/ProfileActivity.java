package com.example.bai5_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bai5_firebase.util.BottomNavHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView tvProfileEmail = findViewById(R.id.tvProfileEmail);
        TextView tvProfileName = findViewById(R.id.tvProfileName);
        Button btnLogoutProfile = findViewById(R.id.btnLogoutProfile);

        tvProfileEmail.setText(user.getEmail() == null ? "Chưa cập nhật email" : user.getEmail());
        tvProfileName.setText(user.getDisplayName() == null ? "Movie User" : user.getDisplayName());

        btnLogoutProfile.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        BottomNavHelper.setup(this, R.id.nav_account);
    }
}
