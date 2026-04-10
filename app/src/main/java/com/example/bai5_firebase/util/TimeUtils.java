package com.example.bai5_firebase.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimeUtils {
    private static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";

    private TimeUtils() {
    }

    public static String formatDateTime(long timeMillis) {
        return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault())
                .format(new Date(timeMillis));
    }
}
