package com.example.bai5_firebase;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai5_firebase.adapter.TicketAdapter;
import com.example.bai5_firebase.model.Ticket;
import com.example.bai5_firebase.util.BottomNavHelper;
import com.example.bai5_firebase.util.FirebaseCollections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyTicketsActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private TicketAdapter ticketAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        RecyclerView recyclerTickets = findViewById(R.id.recyclerTickets);
        recyclerTickets.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketAdapter();
        recyclerTickets.setAdapter(ticketAdapter);
        BottomNavHelper.setup(this, R.id.nav_tickets);

        loadTickets();
    }

    private void loadTickets() {
        if (auth.getCurrentUser() == null) {
            finish();
            return;
        }

        firestore.collection(FirebaseCollections.TICKETS)
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Ticket> tickets = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Ticket ticket = doc.toObject(Ticket.class);
                        ticket.setId(doc.getId());
                        tickets.add(ticket);
                    }
                    tickets.sort((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
                    ticketAdapter.setTickets(tickets);
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
