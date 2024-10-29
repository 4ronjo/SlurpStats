package com.example.slurpstats;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class ErgebnisListActivity extends BaseActivity {

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

        setupNavigationDrawer();

        getSupportActionBar().setTitle("Gespeicherte Ergebnisse");

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
}