package com.example.bai5_firebase.util;

import android.text.TextUtils;
import android.util.Patterns;

public final class ValidationUtils {
    private ValidationUtils() {
    }

    public static String validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "Email không được để trống";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không đúng định dạng";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Mật khẩu không được để trống";
        }
        if (password.length() < 6) {
            return "Mật khẩu phải từ 6 ký tự";
        }
        return null;
    }

    public static String validateName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "Tên không được để trống";
        }
        if (name.trim().length() < 2) {
            return "Tên phải từ 2 ký tự";
        }
        return null;
    }

    public static String validateQuantity(String quantityText) {
        if (TextUtils.isEmpty(quantityText)) {
            return "Số lượng vé không được để trống";
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                return "Số lượng vé phải lớn hơn 0";
            }
            if (quantity > 10) {
                return "Mỗi lần đặt tối đa 10 vé";
            }
        } catch (NumberFormatException ex) {
            return "Số lượng vé phải là số";
        }
        return null;
    }
}
