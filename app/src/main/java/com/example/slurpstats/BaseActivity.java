package com.example.slurpstats;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupNavigationDrawer() {
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Navigation Listener setzen
        navigationView.setNavigationItemSelectedListener(this);

        // Hamburger-Menü Icon hinzufügen
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Navigation Drawer Menüauswahl behandeln
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Beenden Sie die aktuelle Activity
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(this, HilfeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_results) {
            Intent intent = new Intent(this, ErgebnisListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_impressum) {
            Intent intent = new Intent(this, ImpressumActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
