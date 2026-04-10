package com.example.bai5_firebase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bai5_firebase.receiver.ReminderReceiver;
import com.example.bai5_firebase.util.BottomNavHelper;
import com.example.bai5_firebase.util.FirebaseCollections;
import com.example.bai5_firebase.util.NotificationUtils;
import com.example.bai5_firebase.util.TimeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private String showtimeId;
    private String movieTitle;
    private String theaterName;
    private long showtimeMillis;
    private double price;

    private final Set<String> bookedSeats = new HashSet<>();
    private final Set<String> selectedSeats = new HashSet<>();
    private GridLayout gridSeats;
    private TextView tvSeatInfo;
    private Button btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        showtimeId = getIntent().getStringExtra("showtimeId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        theaterName = getIntent().getStringExtra("theaterName");
        showtimeMillis = getIntent().getLongExtra("showtimeMillis", 0L);
        price = getIntent().getDoubleExtra("price", 0.0);

        if (auth.getCurrentUser() == null || showtimeId == null || movieTitle == null || theaterName == null) {
            finish();
            return;
        }

        TextView tvTitle = findViewById(R.id.tvSeatTitle);
        TextView tvInfo = findViewById(R.id.tvSeatShowtimeInfo);
        tvSeatInfo = findViewById(R.id.tvSeatSelectedInfo);
        gridSeats = findViewById(R.id.gridSeats);
        btnConfirm = findViewById(R.id.btnConfirmSeatBooking);

        tvTitle.setText(movieTitle + " - " + theaterName);
        tvInfo.setText("Giờ chiếu: " + TimeUtils.formatDateTime(showtimeMillis) + " | Chọn ghế bạn muốn");

        btnConfirm.setOnClickListener(v -> validateAndBook());
        BottomNavHelper.setup(this, R.id.nav_home);

        loadBookedSeats();
    }

    private void loadBookedSeats() {
        firestore.collection(FirebaseCollections.TICKETS)
                .whereEqualTo("showtimeId", showtimeId)
                .whereEqualTo("status", "booked")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookedSeats.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        List<String> seats = (List<String>) doc.get("selectedSeats");
                        if (seats != null) {
                            bookedSeats.addAll(seats);
                        }
                    }
                    drawSeatMap();
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void drawSeatMap() {
        gridSeats.removeAllViews();
        gridSeats.setColumnCount(8);

        for (char row = 'A'; row <= 'E'; row++) {
            for (int col = 1; col <= 8; col++) {
                String seatCode = row + String.valueOf(col);
                Button seatButton = new Button(this);
                seatButton.setText(seatCode);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(6, 6, 6, 6);
                seatButton.setLayoutParams(params);

                if (bookedSeats.contains(seatCode)) {
                    seatButton.setEnabled(false);
                    seatButton.setBackgroundColor(0xFFE0E0E0);
                } else {
                    seatButton.setBackgroundColor(0xFFBBDEFB);
                    seatButton.setOnClickListener(v -> toggleSeat(seatCode, seatButton));
                }

                gridSeats.addView(seatButton);
            }
        }

        updateSelectedInfo();
    }

    private void toggleSeat(String seatCode, Button seatButton) {
        if (selectedSeats.contains(seatCode)) {
            selectedSeats.remove(seatCode);
            seatButton.setBackgroundColor(0xFFBBDEFB);
        } else {
            if (selectedSeats.size() >= 10) {
                Toast.makeText(this, "Mỗi lần đặt tối đa 10 ghế", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedSeats.add(seatCode);
            seatButton.setBackgroundColor(0xFF80CBC4);
        }
        updateSelectedInfo();
    }

    private void updateSelectedInfo() {
        int count = selectedSeats.size();
        double totalPrice = count * price;
        tvSeatInfo.setText("Đã chọn: " + count + " ghế");
        btnConfirm.setText("Đặt vé - " + ((long) totalPrice) + " VNĐ");
    }

    private void validateAndBook() {
        int count = selectedSeats.size();
        if (count <= 0) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 ghế", Toast.LENGTH_SHORT).show();
            return;
        }
        if (count > 10) {
            Toast.makeText(this, "Mỗi lần đặt tối đa 10 ghế", Toast.LENGTH_SHORT).show();
            return;
        }
        saveTicket();
    }

    private void saveTicket() {
        int quantity = selectedSeats.size();
        double totalPrice = price * quantity;
        List<String> seats = new ArrayList<>(selectedSeats);
        Collections.sort(seats);

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("userId", auth.getCurrentUser().getUid());
        ticket.put("showtimeId", showtimeId);
        ticket.put("movieTitle", movieTitle);
        ticket.put("theaterName", theaterName);
        ticket.put("showtimeMillis", showtimeMillis);
        ticket.put("quantity", quantity);
        ticket.put("selectedSeats", seats);
        ticket.put("totalPrice", totalPrice);
        ticket.put("status", "booked");
        ticket.put("createdAt", System.currentTimeMillis());

        firestore.collection(FirebaseCollections.TICKETS)
                .add(ticket)
                .addOnSuccessListener(documentReference -> {
                    saveReminderRequest();
                    scheduleLocalReminder();
                    NotificationUtils.createChannel(this);
                    NotificationUtils.showNotification(
                            this,
                            (int) (System.currentTimeMillis() % Integer.MAX_VALUE),
                            "Đặt vé thành công",
                            "Bạn đã đặt " + quantity + " vé cho " + movieTitle + " tại " + theaterName
                    );
                    Toast.makeText(this, "Dat ve thanh cong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MyTicketsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void saveReminderRequest() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Map<String, Object> reminder = new HashMap<>();
            reminder.put("userId", auth.getCurrentUser().getUid());
            reminder.put("movieTitle", movieTitle);
            reminder.put("theaterName", theaterName);
            reminder.put("showtimeMillis", showtimeMillis);
            reminder.put("fcmToken", token);
            reminder.put("topic", "showtime_reminder");
            reminder.put("createdAt", System.currentTimeMillis());
            firestore.collection(FirebaseCollections.REMINDERS).add(reminder);
        });
    }

    private void scheduleLocalReminder() {
        long reminderTime = showtimeMillis - 30 * 60 * 1000L;
        if (reminderTime <= System.currentTimeMillis()) {
            return;
        }

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("theaterName", theaterName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) (System.currentTimeMillis() % Integer.MAX_VALUE),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                } else {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                }
            } catch (SecurityException ex) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                }
            }
        }
    }
}
