package com.example.bai5_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bai5_firebase.util.FirebaseCollections;
import com.example.bai5_firebase.util.ValidationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> register());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        String nameError = ValidationUtils.validateName(name);
        if (nameError != null) {
            etName.setError(nameError);
            return;
        }
        String emailError = ValidationUtils.validateEmail(email);
        if (emailError != null) {
            etEmail.setError(emailError);
            return;
        }
        String passwordError = ValidationUtils.validatePassword(password);
        if (passwordError != null) {
            etPassword.setError(passwordError);
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    saveUserProfile(name, email);
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void saveUserProfile(String name, String email) {
        if (auth.getCurrentUser() == null) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("displayName", name);
        data.put("updatedAt", System.currentTimeMillis());

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            data.put("fcmToken", token);
            firestore.collection(FirebaseCollections.USERS)
                    .document(auth.getCurrentUser().getUid())
                    .set(data);
        });
    }
}
