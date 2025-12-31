package com.example.e_rental.adapter;

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

    private List<bottom_nav> navItems;  // ← Perbaikan: gunakan bottom_nav bukan BottomNavAdapter
    private OnItemClickListener listener;

    // Interface untuk handle click event
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor
    public BottomNavAdapter(List<bottom_nav> navItems, OnItemClickListener listener) {  // ← Perbaikan
        this.navItems = navItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_bottom_nav.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bottom_nav, parent, false);
        return new NavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
        // Ambil data dari list berdasarkan posisi
        bottom_nav item = navItems.get(position);  // ← Perbaikan: deklarasi variabel dengan benar

        // Set icon ke ImageView
        holder.ivNavIcon.setImageResource(item.getIconResId());

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return jumlah item dalam list
        return navItems.size();
    }

    // ViewHolder class untuk menyimpan reference view
    static class NavViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNavIcon;

        public NavViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inisialisasi ImageView dari layout
            ivNavIcon = itemView.findViewById(R.id.ivNavIcon);
        }
    }
}