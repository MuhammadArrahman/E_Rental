package com.example.e_rental.adapter;

import android.graphics.Color; // Import baru untuk warna
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_rental.R;
import com.example.e_rental.admin.bottom_nav;
import java.util.List;

public class BottomNavAdapter extends RecyclerView.Adapter<BottomNavAdapter.NavViewHolder> {

    private List<bottom_nav> navItems;
    private OnItemClickListener listener;

    // Interface untuk handle click event
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor
    public BottomNavAdapter(List<bottom_nav> navItems, OnItemClickListener listener) {
        this.navItems = navItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_bottom_nav.xml yang baru saja kamu buat
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bottom_nav, parent, false);
        return new NavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
        // Ambil data dari list berdasarkan posisi
        bottom_nav item = navItems.get(position);

        // Set icon ke ImageView
        holder.ivNavIcon.setImageResource(item.getIconResId());

        // --- TAMBAHAN BARU: Mengubah warna icon menjadi putih secara programmatik ---
        holder.ivNavIcon.setColorFilter(Color.WHITE);
        // -------------------------------------------------------------------------

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return navItems.size();
    }

    static class NavViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNavIcon;

        public NavViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNavIcon = itemView.findViewById(R.id.ivNavIcon);
        }
    }
}