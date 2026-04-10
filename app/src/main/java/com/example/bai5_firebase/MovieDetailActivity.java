package com.example.bai5_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bai5_firebase.util.BottomNavHelper;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        String movieId = getIntent().getStringExtra("movieId");
        String movieTitle = getIntent().getStringExtra("movieTitle");
        String movieGenre = getIntent().getStringExtra("movieGenre");
        long movieDuration = getIntent().getLongExtra("movieDuration", 0L);
        String movieDescription = getIntent().getStringExtra("movieDescription");
        String movieImageUrl = getIntent().getStringExtra("movieImageUrl");

        if (movieId == null || movieTitle == null) {
            finish();
            return;
        }

        ImageView ivPoster = findViewById(R.id.ivDetailPoster);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvGenre = findViewById(R.id.tvDetailGenre);
        TextView tvDuration = findViewById(R.id.tvDetailDuration);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        Button btnBookTicket = findViewById(R.id.btnGoBookTicket);

        tvTitle.setText(movieTitle);
        tvGenre.setText("Thể loại: " + (movieGenre == null ? "Đang cập nhật" : movieGenre));
        tvDuration.setText("Thời lượng: " + movieDuration + " phút");
        tvDescription.setText(movieDescription == null ? "Đang cập nhật nội dung phim." : movieDescription);

        Glide.with(this)
                .load(movieImageUrl)
                .placeholder(R.drawable.bg_card)
                .error(R.drawable.bg_card)
                .centerCrop()
                .into(ivPoster);

        btnBookTicket.setOnClickListener(v -> {
            Intent intent = new Intent(this, TheaterSelectionActivity.class);
            intent.putExtra("movieId", movieId);
            intent.putExtra("movieTitle", movieTitle);
            startActivity(intent);
        });

        BottomNavHelper.setup(this, R.id.nav_home);
    }
}
