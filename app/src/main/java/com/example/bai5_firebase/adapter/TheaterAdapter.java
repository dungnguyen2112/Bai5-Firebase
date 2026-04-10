package com.example.bai5_firebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.R;
import com.example.bai5_firebase.model.Theater;

import java.util.ArrayList;
import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {
    public interface OnTheaterClickListener {
        void onTheaterClick(Theater theater);
    }

    private final List<Theater> theaters = new ArrayList<>();
    private final OnTheaterClickListener listener;

    public TheaterAdapter(OnTheaterClickListener listener) {
        this.listener = listener;
    }

    public void setTheaters(List<Theater> theaterList) {
        theaters.clear();
        theaters.addAll(theaterList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        Theater theater = theaters.get(position);
        holder.tvName.setText(theater.getName());
        holder.tvAddress.setText(theater.getAddress());
        holder.itemView.setOnClickListener(v -> listener.onTheaterClick(theater));
    }

    @Override
    public int getItemCount() {
        return theaters.size();
    }

    static class TheaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAddress;

        TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTheaterItemName);
            tvAddress = itemView.findViewById(R.id.tvTheaterItemAddress);
        }
    }
}
