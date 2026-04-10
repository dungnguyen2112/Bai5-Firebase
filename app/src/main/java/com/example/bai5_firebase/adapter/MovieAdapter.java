package com.example.bai5_firebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.R;
import com.example.bai5_firebase.model.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private final List<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> movieList) {
        movies.clear();
        movies.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvGenre.setText("Thể loại: " + movie.getGenre());
        holder.tvDuration.setText("Thời lượng: " + movie.getDurationMin() + " phút");
        holder.tvDescription.setText(movie.getDescription());
        Glide.with(holder.itemView.getContext())
                .load(movie.getImageUrl())
                .placeholder(R.drawable.bg_card)
                .error(R.drawable.bg_card)
                .centerCrop()
                .into(holder.ivPoster);
        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivPoster;
        TextView tvGenre;
        TextView tvDuration;
        TextView tvDescription;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            ivPoster = itemView.findViewById(R.id.ivMoviePoster);
            tvGenre = itemView.findViewById(R.id.tvMovieGenre);
            tvDuration = itemView.findViewById(R.id.tvMovieDuration);
            tvDescription = itemView.findViewById(R.id.tvMovieDescription);
        }
    }
}
