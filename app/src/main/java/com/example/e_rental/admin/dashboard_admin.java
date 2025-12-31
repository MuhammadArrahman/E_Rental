package com.example.e_rental.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rental.R;
import com.example.e_rental.adapter.BottomNavAdapter;

import java.util.ArrayList;
import java.util.List;

public class dashboard_admin extends AppCompatActivity {

    private RecyclerView bottomNavigationRecycler;
    private BottomNavAdapter bottomNavAdapter;
    private List<bottom_nav> navItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottomNavigationRecycler), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi RecyclerView
        bottomNavigationRecycler = findViewById(R.id.bottomNavigationRecycler);

        // Setup RecyclerView untuk Bottom Navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Buat data untuk bottom navigation
        navItems = new ArrayList<>();
        navItems.add(new bottom_nav(R.drawable.ic_home, "Home"));
        navItems.add(new bottom_nav(R.drawable.ic_add, "Add"));
        navItems.add(new bottom_nav(R.drawable.ic_history, "History"));
        navItems.add(new bottom_nav(R.drawable.ic_profile, "Profile"));

        // Setup LayoutManager dengan orientasi Horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        bottomNavigationRecycler.setLayoutManager(layoutManager);

        // Setup Adapter dengan click listener
        bottomNavAdapter = new BottomNavAdapter(navItems, new BottomNavAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                handleNavClick(position);
            }
        });

        // Set adapter ke RecyclerView
        bottomNavigationRecycler.setAdapter(bottomNavAdapter);
    }

    private void handleNavClick(int position) {
        switch (position) {
            case 0: // Home
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();
                // Sudah di halaman home, bisa refresh data atau tidak melakukan apa-apa
                break;

            case 1: // Add
                Toast.makeText(this, "Add clicked", Toast.LENGTH_SHORT).show();
                // TODO: Intent ke halaman Add Armada
                // Intent intent = new Intent(this, AddArmadaActivity.class);
                // startActivity(intent);
                break;

            case 2: // History
                Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show();
                // TODO: Intent ke halaman History
                // Intent intent = new Intent(this, HistoryActivity.class);
                // startActivity(intent);
                break;

            case 3: // Profile
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
                // TODO: Intent ke halaman Profile
                // Intent intent = new Intent(this, ProfileActivity.class);
                // startActivity(intent);
                break;
        }
    }
}