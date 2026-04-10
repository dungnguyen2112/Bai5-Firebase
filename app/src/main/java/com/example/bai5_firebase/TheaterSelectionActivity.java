package com.example.bai5_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.adapter.TheaterAdapter;
import com.example.bai5_firebase.model.Theater;
import com.example.bai5_firebase.util.BottomNavHelper;
import com.example.bai5_firebase.util.FirebaseCollections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TheaterSelectionActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String movieId;
    private String movieTitle;
    private TheaterAdapter theaterAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_selection);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        if (auth.getCurrentUser() == null || movieId == null || movieTitle == null) {
            finish();
            return;
        }

        TextView tvTitle = findViewById(R.id.tvTheaterSelectionTitle);
        tvTitle.setText("Chọn rạp - " + movieTitle);

        RecyclerView recyclerTheaters = findViewById(R.id.recyclerTheaters);
        recyclerTheaters.setLayoutManager(new LinearLayoutManager(this));
        theaterAdapter = new TheaterAdapter(this::openShowtimes);
        recyclerTheaters.setAdapter(theaterAdapter);
        BottomNavHelper.setup(this, R.id.nav_home);

        loadTheatersByMovie();
    }

    private void loadTheatersByMovie() {
        firestore.collection(FirebaseCollections.SHOWTIMES)
                .whereEqualTo("movieId", movieId)
                .get()
                .addOnSuccessListener(showtimeDocs -> {
                    Set<String> theaterIds = new HashSet<>();
                    for (QueryDocumentSnapshot doc : showtimeDocs) {
                        String theaterId = doc.getString("theaterId");
                        if (theaterId != null) {
                            theaterIds.add(theaterId);
                        }
                    }
                    if (theaterIds.isEmpty()) {
                        theaterAdapter.setTheaters(new ArrayList<>());
                        Toast.makeText(this, "Phim này chưa có suất chiếu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loadTheaterDetails(theaterIds);
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void loadTheaterDetails(Set<String> theaterIds) {
        firestore.collection(FirebaseCollections.THEATERS)
                .get()
                .addOnSuccessListener(theaterDocs -> {
                    List<Theater> theaters = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : theaterDocs) {
                        if (!theaterIds.contains(doc.getId())) {
                            continue;
                        }
                        Theater theater = doc.toObject(Theater.class);
                        theater.setId(doc.getId());
                        theaters.add(theater);
                    }
                    theaterAdapter.setTheaters(theaters);
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void openShowtimes(Theater theater) {
        Intent intent = new Intent(this, ShowtimesActivity.class);
        intent.putExtra("movieId", movieId);
        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("theaterId", theater.getId());
        intent.putExtra("theaterName", theater.getName());
        startActivity(intent);
    }
}
