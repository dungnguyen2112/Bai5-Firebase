package com.example.bai5_firebase;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.adapter.MovieAdapter;
import com.example.bai5_firebase.model.Movie;
import com.example.bai5_firebase.receiver.ReminderReceiver;
import com.example.bai5_firebase.util.BottomNavHelper;
import com.example.bai5_firebase.util.FirebaseCollections;
import com.example.bai5_firebase.util.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private MovieAdapter movieAdapter;
    private final Map<String, String> defaultImageByMovieId = new HashMap<>();
    private final List<Movie> allMovies = new ArrayList<>();
    private final List<String> categoryOptions = new ArrayList<>();
    private Spinner spCategory;
    private EditText etFilterKeyword;
    private String selectedCategory = "Tất cả";

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setupNotification();
        setupDefaultMovieImages();
        setupRecycler();
        setupFilterControls();
        setupHiddenTestNotification();
        BottomNavHelper.setup(this, R.id.nav_home);
        seedSampleDataIfNeeded();
        loadMovies();
        subscribeTopic();
    }

    private void setupRecycler() {
        RecyclerView recyclerMovies = findViewById(R.id.recyclerMovies);
        recyclerMovies.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movieId", movie.getId());
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieDuration", movie.getDurationMin());
            intent.putExtra("movieDescription", movie.getDescription());
            intent.putExtra("movieImageUrl", movie.getImageUrl());
            startActivity(intent);
        });
        recyclerMovies.setAdapter(movieAdapter);
    }

    private void setupFilterControls() {
        spCategory = findViewById(R.id.spCategory);
        etFilterKeyword = findViewById(R.id.etFilterKeyword);
        findViewById(R.id.btnApplyFilter).setOnClickListener(v -> {
            Object selected = spCategory.getSelectedItem();
            selectedCategory = selected == null ? "Tất cả" : selected.toString();
            applyMovieFilter();
        });
    }

    private void setupHiddenTestNotification() {
        TextView tvMainTitle = findViewById(R.id.tvMainTitle);

        tvMainTitle.setOnLongClickListener(v -> {
            long triggerAtMillis = System.currentTimeMillis() + 5000;
            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("movieTitle", "Test notification");
            intent.putExtra("theaterName", "Movie Ticket App");
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
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    }
                } catch (SecurityException ex) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    }
                }
                Toast.makeText(this, "Da bat test notification (5 giay)", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void seedSampleDataIfNeeded() {
        Movie m1 = new Movie("m1", "Avengers: Endgame", "Action", 181,
                "Biệt đội siêu anh hùng chiến đấu trận cuối.",
                defaultImageByMovieId.get("m1"));
        Movie m2 = new Movie("m2", "Doraemon Movie", "Animation", 115,
                "Chuyến phiêu lưu mới cùng nhóm bạn Nobita.",
                defaultImageByMovieId.get("m2"));
        Movie m3 = new Movie("m3", "Interstellar", "Sci-Fi", 169,
                "Hành trình không gian tìm sự sống mới cho nhân loại.",
                defaultImageByMovieId.get("m3"));
        Movie m4 = new Movie("m4", "The Batman", "Action/Crime", 176,
                "Batman truy tìm tên sát nhân hàng loạt ở Gotham.",
                defaultImageByMovieId.get("m4"));
        Movie m5 = new Movie("m5", "Your Name", "Romance/Animation", 106,
                "Câu chuyện hoán đổi thân xác đầy cảm xúc.",
                defaultImageByMovieId.get("m5"));
        Movie m6 = new Movie("m6", "Fast X", "Action", 141,
                "Dom cùng đồng đội tiếp tục cuộc đua sinh tử.",
                defaultImageByMovieId.get("m6"));

        firestore.collection(FirebaseCollections.MOVIES).document("m1").set(m1);
        firestore.collection(FirebaseCollections.MOVIES).document("m2").set(m2);
        firestore.collection(FirebaseCollections.MOVIES).document("m3").set(m3);
        firestore.collection(FirebaseCollections.MOVIES).document("m4").set(m4);
        firestore.collection(FirebaseCollections.MOVIES).document("m5").set(m5);
        firestore.collection(FirebaseCollections.MOVIES).document("m6").set(m6);

        Map<String, Object> t1 = new HashMap<>();
        t1.put("name", "CGV Vincom");
        t1.put("address", "Quận 1, TP.HCM");
        Map<String, Object> t2 = new HashMap<>();
        t2.put("name", "Lotte Cinema");
        t2.put("address", "Quận 7, TP.HCM");
        Map<String, Object> t3 = new HashMap<>();
        t3.put("name", "Galaxy Nguyễn Du");
        t3.put("address", "Quận 1, TP.HCM");
        Map<String, Object> t4 = new HashMap<>();
        t4.put("name", "BHD Bitexco");
        t4.put("address", "Quận 1, TP.HCM");
        firestore.collection(FirebaseCollections.THEATERS).document("t1").set(t1);
        firestore.collection(FirebaseCollections.THEATERS).document("t2").set(t2);
        firestore.collection(FirebaseCollections.THEATERS).document("t3").set(t3);
        firestore.collection(FirebaseCollections.THEATERS).document("t4").set(t4);

        seedShowtimesWholeDay();
    }

    private void seedShowtimesWholeDay() {
        String[] movieIds = {"m1", "m2", "m3", "m4", "m5", "m6"};
        String[] theaterIds = {"t1", "t2", "t3", "t4"};
        double[] prices = {90000, 100000, 75000, 110000, 85000, 98000};

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int showtimeIndex = 1;

        // Trai deu lich chieu trong 2 ngay, khung gio 08:00 -> 23:00
        for (int dayOffset = 0; dayOffset <= 1; dayOffset++) {
            for (int hour = 8; hour <= 23; hour++) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.DATE, dayOffset);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);
                long startTimeMillis = calendar.getTimeInMillis();
                if (startTimeMillis <= System.currentTimeMillis()) {
                    continue;
                }

                String movieId = movieIds[(showtimeIndex - 1) % movieIds.length];
                String theaterId = theaterIds[(showtimeIndex - 1) % theaterIds.length];
                double price = prices[(showtimeIndex - 1) % prices.length];
                String showtimeId = "s" + showtimeIndex;

                createShowtime(showtimeId, movieId, theaterId, startTimeMillis, price);
                showtimeIndex++;
            }
        }
    }

    private void createShowtime(String id, String movieId, String theaterId, long startTimeMillis, double price) {
        Map<String, Object> data = new HashMap<>();
        data.put("movieId", movieId);
        data.put("theaterId", theaterId);
        data.put("startTimeMillis", startTimeMillis);
        data.put("price", price);
        firestore.collection(FirebaseCollections.SHOWTIMES).document(id).set(data);
    }

    private void loadMovies() {
        firestore.collection(FirebaseCollections.MOVIES)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null || queryDocumentSnapshots == null) {
                        if (e != null) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                    List<Movie> movies = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = doc.toObject(Movie.class);
                        movie.setId(doc.getId());
                        backfillMovieImageIfMissing(doc.getId(), movie.getImageUrl());
                        movies.add(movie);
                    }
                    allMovies.clear();
                    allMovies.addAll(movies);
                    refreshCategoryOptions();
                    applyMovieFilter();
                });
    }

    private void refreshCategoryOptions() {
        Set<String> categories = new LinkedHashSet<>();
        categories.add("Tất cả");
        for (Movie movie : allMovies) {
            if (movie.getGenre() != null && !movie.getGenre().trim().isEmpty()) {
                categories.add(movie.getGenre().trim());
            }
        }
        categoryOptions.clear();
        categoryOptions.addAll(categories);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
        int index = categoryOptions.indexOf(selectedCategory);
        spCategory.setSelection(index >= 0 ? index : 0);
    }

    private void applyMovieFilter() {
        String keyword = etFilterKeyword.getText().toString().trim().toLowerCase(Locale.getDefault());
        List<Movie> filtered = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean matchCategory = "Tất cả".equals(selectedCategory) || selectedCategory.equalsIgnoreCase(movie.getGenre());
            String title = movie.getTitle() == null ? "" : movie.getTitle().toLowerCase(Locale.getDefault());
            boolean matchKeyword = keyword.isEmpty() || title.contains(keyword);

            if (matchCategory && matchKeyword) {
                filtered.add(movie);
            }
        }
        movieAdapter.setMovies(filtered);
    }

    private void setupDefaultMovieImages() {
        defaultImageByMovieId.put("m1", "https://picsum.photos/seed/avengers/800/450");
        defaultImageByMovieId.put("m2", "https://picsum.photos/seed/doraemon/800/450");
        defaultImageByMovieId.put("m3", "https://picsum.photos/seed/interstellar/800/450");
        defaultImageByMovieId.put("m4", "https://picsum.photos/seed/batman/800/450");
        defaultImageByMovieId.put("m5", "https://picsum.photos/seed/yourname/800/450");
        defaultImageByMovieId.put("m6", "https://picsum.photos/seed/fastx/800/450");
    }

    private void backfillMovieImageIfMissing(String movieId, String imageUrl) {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            return;
        }
        String fallback = defaultImageByMovieId.get(movieId);
        if (fallback == null) {
            return;
        }
        firestore.collection(FirebaseCollections.MOVIES)
                .document(movieId)
                .update("imageUrl", fallback);
    }

    private void setupNotification() {
        NotificationUtils.createChannel(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("showtime_reminder");
    }
}