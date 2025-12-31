package com.example.e_rental.admin;

public class bottom_nav {
    private int iconResId;
    private String title;

    // Constructor
    public bottom_nav(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    // Getter untuk iconResId
    public int getIconResId() {
        return iconResId;
    }

    // Setter untuk iconResId
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    // Getter untuk title
    public String getTitle() {
        return title;
    }

    // Setter untuk title
    public void setTitle(String title) {
        this.title = title;
    }
}
