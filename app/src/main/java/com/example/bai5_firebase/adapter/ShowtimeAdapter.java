package com.example.bai5_firebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.R;
import com.example.bai5_firebase.model.Showtime;
import com.example.bai5_firebase.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {
    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    private final List<Showtime> showtimes = new ArrayList<>();
    private final Map<String, String> theaterNameById;
    private final OnShowtimeClickListener listener;

    public ShowtimeAdapter(Map<String, String> theaterNameById, OnShowtimeClickListener listener) {
        this.theaterNameById = theaterNameById;
        this.listener = listener;
    }

    public void setShowtimes(List<Showtime> showtimeList) {
        showtimes.clear();
        showtimes.addAll(showtimeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimes.get(position);
        holder.tvTheater.setText(theaterNameById.getOrDefault(showtime.getTheaterId(), "Rạp chưa cập nhật"));
        holder.tvStartTime.setText("Giờ chiếu: " + TimeUtils.formatDateTime(showtime.getStartTimeMillis()));
        holder.tvPrice.setText("Giá: " + showtime.getPrice() + " VNĐ");
        holder.itemView.setOnClickListener(v -> listener.onShowtimeClick(showtime));
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTheater;
        TextView tvStartTime;
        TextView tvPrice;

        ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTheater = itemView.findViewById(R.id.tvTheaterName);
            tvStartTime = itemView.findViewById(R.id.tvShowtimeStart);
            tvPrice = itemView.findViewById(R.id.tvShowtimePrice);
        }
    }
}
