package com.example.bai5_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.adapter.ShowtimeAdapter;
import com.example.bai5_firebase.model.Showtime;
import com.example.bai5_firebase.util.BottomNavHelper;
import com.example.bai5_firebase.util.FirebaseCollections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShowtimesActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String movieId;
    private String movieTitle;
    private String theaterId;
    private String theaterName;
    private ShowtimeAdapter showtimeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtimes);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        theaterId = getIntent().getStringExtra("theaterId");
        theaterName = getIntent().getStringExtra("theaterName");

        if (movieId == null || movieTitle == null || theaterId == null || theaterName == null
                || auth.getCurrentUser() == null) {
            finish();
            return;
        }

        TextView tvMovieTitle = findViewById(R.id.tvShowtimeMovieTitle);
        tvMovieTitle.setText("Giờ chiếu - " + movieTitle + " (" + theaterName + ")");

        RecyclerView recyclerShowtimes = findViewById(R.id.recyclerShowtimes);
        recyclerShowtimes.setLayoutManager(new LinearLayoutManager(this));
        showtimeAdapter = new ShowtimeAdapter(java.util.Collections.singletonMap(theaterId, theaterName), this::openSeatSelection);
        recyclerShowtimes.setAdapter(showtimeAdapter);
        BottomNavHelper.setup(this, R.id.nav_home);

        loadShowtimes();
    }

    private void loadShowtimes() {
        firestore.collection(FirebaseCollections.SHOWTIMES)
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("theaterId", theaterId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Showtime> showtimes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Showtime showtime = doc.toObject(Showtime.class);
                        showtime.setId(doc.getId());
                        showtimes.add(showtime);
                    }
                    showtimes.sort(Comparator.comparingLong(Showtime::getStartTimeMillis));
                    showtimeAdapter.setShowtimes(showtimes);
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void openSeatSelection(Showtime showtime) {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra("showtimeId", showtime.getId());
        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("theaterName", theaterName);
        intent.putExtra("showtimeMillis", showtime.getStartTimeMillis());
        intent.putExtra("price", showtime.getPrice());
        startActivity(intent);
    }
}
