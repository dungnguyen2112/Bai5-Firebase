package com.example.bai5_firebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.R;
import com.example.bai5_firebase.model.Ticket;
import com.example.bai5_firebase.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private final List<Ticket> tickets = new ArrayList<>();

    public void setTickets(List<Ticket> ticketList) {
        tickets.clear();
        tickets.addAll(ticketList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.tvMovie.setText(ticket.getMovieTitle());
        holder.tvTheater.setText("Rạp: " + ticket.getTheaterName());
        holder.tvShowtime.setText("Giờ chiếu: " + TimeUtils.formatDateTime(ticket.getShowtimeMillis()));
        holder.tvQuantity.setText("Số vé: " + ticket.getQuantity());
        holder.tvSeats.setText("Ghế: " + formatSeats(ticket.getSelectedSeats()));
        holder.tvTotal.setText("Tổng tiền: " + ticket.getTotalPrice() + " VNĐ");
        holder.tvStatus.setText("Trạng thái: " + ticket.getStatus());
    }

    private String formatSeats(List<String> seats) {
        if (seats == null || seats.isEmpty()) {
            return "Chưa có";
        }
        return String.join(", ", seats);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovie;
        TextView tvTheater;
        TextView tvShowtime;
        TextView tvQuantity;
        TextView tvSeats;
        TextView tvTotal;
        TextView tvStatus;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovie = itemView.findViewById(R.id.tvTicketMovie);
            tvTheater = itemView.findViewById(R.id.tvTicketTheater);
            tvShowtime = itemView.findViewById(R.id.tvTicketShowtime);
            tvQuantity = itemView.findViewById(R.id.tvTicketQuantity);
            tvSeats = itemView.findViewById(R.id.tvTicketSeats);
            tvTotal = itemView.findViewById(R.id.tvTicketTotal);
            tvStatus = itemView.findViewById(R.id.tvTicketStatus);
        }
    }
}
