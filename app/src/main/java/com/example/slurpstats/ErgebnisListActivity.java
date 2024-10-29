package com.example.slurpstats;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErgebnisListActivity extends BaseActivity {

    private ListView listViewErgebnisse;
    private TextView textViewAnzahlErgebnisse;

    private ResultDataSource resultDataSource;
    private List<Result> ergebnisListe;
    private ResultListAdapter adapter;

    private boolean isSelectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ergebnis);

        setupNavigationDrawer();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gespeicherte Ergebnisse");
        }

        listViewErgebnisse = findViewById(R.id.list_view_ergebnisse);
        textViewAnzahlErgebnisse = findViewById(R.id.text_view_anzahl_ergebnisse);

        resultDataSource = new ResultDataSource(this);
        resultDataSource.open();

        ergebnisseLaden();

        listViewErgebnisse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                if (isSelectionMode) {
                    // Checkbox-Zustand umschalten
                    CheckBox checkBox = view.findViewById(R.id.result_checkbox);
                    boolean newState = !checkBox.isChecked();
                    checkBox.setChecked(newState);
                    adapter.checkedStates.set(position, newState);
                } else {
                    long ergebnisId = ergebnisListe.get(position).getId();
                    Intent intent = new Intent(ErgebnisListActivity.this, ErgebnisActivity.class);
                    intent.putExtra("ergebnis_id", ergebnisId);
                    startActivity(intent);
                }
            }
        });

        listViewErgebnisse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!isSelectionMode) {
                    enterSelectionMode();
                    CheckBox checkBox = view.findViewById(R.id.result_checkbox);
                    checkBox.setChecked(true);
                    adapter.checkedStates.set(position, true);
                }
                return true;
            }
        });
    }

    private void ergebnisseLaden() {
        ergebnisListe = resultDataSource.getAllResults();

        adapter = new ResultListAdapter(this, ergebnisListe);
        listViewErgebnisse.setAdapter(adapter);

        int anzahlErgebnisse = ergebnisListe.size();
        if (anzahlErgebnisse == 0) {
            textViewAnzahlErgebnisse.setText("Keine Ergebnisse gespeichert.");
        } else {
            textViewAnzahlErgebnisse.setText("Anzahl gespeicherter Ergebnisse: " + anzahlErgebnisse);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ergebnis_list_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        MenuItem selectAllItem = menu.findItem(R.id.action_select_all);

        if (isSelectionMode) {
            editItem.setVisible(false);
            deleteItem.setVisible(true);
            selectAllItem.setVisible(true);
        } else {
            editItem.setVisible(true);
            deleteItem.setVisible(false);
            selectAllItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void enterSelectionMode() {
        isSelectionMode = true;
        adapter.setSelectionMode(true);
        invalidateOptionsMenu(); // Menü aktualisieren
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Einträge auswählen");
        }
    }

    private void exitSelectionMode() {
        isSelectionMode = false;
        adapter.setSelectionMode(false);
        invalidateOptionsMenu();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gespeicherte Ergebnisse");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            // Auswahlmodus aktivieren
            enterSelectionMode();
            return true;
        } else if (id == R.id.action_delete) {
            // Ausgewählte Einträge löschen
            List<Result> selectedResults = adapter.getSelectedResults();
            if (selectedResults.isEmpty()) {
                Toast.makeText(this, "Keine Einträge ausgewählt.", Toast.LENGTH_SHORT).show();
            } else {
                loeschBestaetigungsDialogAnzeigen(selectedResults);
            }
            return true;
        } else if (id == R.id.action_select_all) {
            // Alle Einträge auswählen
            adapter.checkedStates = new ArrayList<>(Collections.nCopies(ergebnisListe.size(), true));
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loeschBestaetigungsDialogAnzeigen(List<Result> zuLoeschendeErgebnisse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Möchten Sie die ausgewählten Ergebnisse löschen?")
                .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ergebnisseLoeschen(zuLoeschendeErgebnisse);
                    }
                })
                .setNegativeButton("Abbrechen", null);
        builder.create().show();
    }

    private void ergebnisseLoeschen(List<Result> zuLoeschendeErgebnisse) {
        for (Result ergebnis : zuLoeschendeErgebnisse) {
            resultDataSource.deleteResult(ergebnis.getId());
        }
        ergebnisseLaden();
        exitSelectionMode();
        Toast.makeText(this, "Ausgewählte Ergebnisse gelöscht.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (isSelectionMode) {
            exitSelectionMode();
        } else {
            super.onBackPressed();
        }
    }
}