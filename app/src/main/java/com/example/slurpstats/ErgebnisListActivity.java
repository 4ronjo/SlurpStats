package com.example.slurpstats;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.slurpstats.ResultDataSource;
import com.example.slurpstats.Result;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ErgebnisListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ListView listViewErgebnisse;
    private TextView textViewAnzahlErgebnisse;

    private ResultDataSource resultDataSource;
    private List<Result> ergebnisListe;
    private ArrayAdapter<String> adapter;
    private List<String> ergebnisTitelListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ergebnis);

        // Toolbar einrichten
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gespeicherte Ergebnisse");

        // Navigation Drawer einrichten
        drawerLayout = findViewById(R.id.drawer_layout_ergebnis_liste);
        navigationView = findViewById(R.id.navigation_view_ergebnis_liste);
        navigationView.setNavigationItemSelectedListener(this);

        // Hamburger-Menü Icon hinzufügen
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new androidx.appcompat.app.ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Views initialisieren
        listViewErgebnisse = findViewById(R.id.list_view_ergebnisse);
        textViewAnzahlErgebnisse = findViewById(R.id.text_view_anzahl_ergebnisse);

        // Datenquelle öffnen
        resultDataSource = new ResultDataSource(this);
        resultDataSource.open();

        // Ergebnisse laden
        ergebnisseLaden();

        // Klick-Events für ListView-Elemente
        listViewErgebnisse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                long ergebnisId = ergebnisListe.get(position).getId();
                Intent intent = new Intent(ErgebnisListActivity.this, ErgebnisActivity.class);
                intent.putExtra("ergebnis_id", ergebnisId);
                startActivity(intent);
            }
        });

        // Langklick zum Löschen eines Ergebnisses
        listViewErgebnisse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                loeschBestaetigungsDialogAnzeigen(position);
                return true;
            }
        });
    }

    private void ergebnisseLaden() {
        ergebnisListe = resultDataSource.getAllResults();
        ergebnisTitelListe = new ArrayList<>();
        for (Result ergebnis : ergebnisListe) {
            ergebnisTitelListe.add(ergebnis.getTitle() + " - " + ergebnis.getDate());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ergebnisTitelListe);
        listViewErgebnisse.setAdapter(adapter);

        int anzahlErgebnisse = ergebnisListe.size();
        if (anzahlErgebnisse == 0) {
            textViewAnzahlErgebnisse.setText("Keine Ergebnisse gespeichert.");
        } else {
            textViewAnzahlErgebnisse.setText("Anzahl gespeicherter Ergebnisse: " + anzahlErgebnisse);
        }
    }

    private void loeschBestaetigungsDialogAnzeigen(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Möchten Sie dieses Ergebnis löschen?")
                .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ergebnisLoeschen(position);
                    }
                })
                .setNegativeButton("Abbrechen", null);
        builder.create().show();
    }

    private void ergebnisLoeschen(int position) {
        Result ergebnis = ergebnisListe.get(position);
        resultDataSource.deleteResult(ergebnis.getId());
        ergebnisseLaden();
        Toast.makeText(this, "Ergebnis gelöscht.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultDataSource.close();
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
            // Aktuelle Activity
        } else if (id == R.id.nav_impressum) {
            Intent intent = new Intent(this, ImpressumActivity.class);
            startActivity(intent);
        }

        return true;
    }
}