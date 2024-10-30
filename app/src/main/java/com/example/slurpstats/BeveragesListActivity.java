package com.example.slurpstats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeveragesListActivity extends BaseActivity {

    private ListView listViewGetraenke;
    private TextView textViewAnzahlGetraenke;

    private DrinkDataSource drinkDataSource;
    private List<Drink> getraenkeListe;
    private DrinkListAdapter adapter;

    private boolean isSelectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_beverages_list);

        setupNavigationDrawer();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gespeicherte Getränke");
        }

        listViewGetraenke = findViewById(R.id.list_view_beverages);
        textViewAnzahlGetraenke = findViewById(R.id.text_view_anzahl_beverages);

        drinkDataSource = new DrinkDataSource(this);
        drinkDataSource.open();

        getraenkeLaden();

        listViewGetraenke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                if (isSelectionMode) {
                    CheckBox checkBox = view.findViewById(R.id.drink_checkbox);
                    boolean newState = !checkBox.isChecked();
                    checkBox.setChecked(newState);
                    adapter.checkedStates.set(position, newState);
                } else {
                    Toast.makeText(BeveragesListActivity.this,
                            "Auswahl: " + getraenkeListe.get(position).getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewGetraenke.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!isSelectionMode) {
                    enterSelectionMode();
                    CheckBox checkBox = view.findViewById(R.id.drink_checkbox);
                    checkBox.setChecked(true);
                    adapter.checkedStates.set(position, true);
                }
                return true;
            }
        });
    }

    private void getraenkeLaden() {
        getraenkeListe = drinkDataSource.getAllDrinks();

        adapter = new DrinkListAdapter(this, getraenkeListe);
        listViewGetraenke.setAdapter(adapter);

        int anzahlGetraenke = getraenkeListe.size();
        if (anzahlGetraenke == 0) {
            textViewAnzahlGetraenke.setText("Keine Getränke gespeichert.");
        } else {
            textViewAnzahlGetraenke.setText("Anzahl gespeicherter Getränke: " + anzahlGetraenke);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drinkDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beverages_list_menu, menu);
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
        invalidateOptionsMenu();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Getränke auswählen");
        }
    }

    private void exitSelectionMode() {
        isSelectionMode = false;
        adapter.setSelectionMode(false);
        invalidateOptionsMenu();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gespeicherte Getränke");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            enterSelectionMode();
            return true;
        } else if (id == R.id.action_delete) {
            List<Drink> selectedDrinks = adapter.getSelectedDrinks();
            if (selectedDrinks.isEmpty()) {
                Toast.makeText(this, "Keine Getränke ausgewählt.", Toast.LENGTH_SHORT).show();
            } else {
                loeschBestaetigungsDialogAnzeigen(selectedDrinks);
            }
            return true;
        } else if (id == R.id.action_select_all) {
            adapter.checkedStates = new ArrayList<>(Collections.nCopies(getraenkeListe.size(), true));
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loeschBestaetigungsDialogAnzeigen(List<Drink> zuLoeschendeGetraenke) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Möchten Sie die ausgewählten Getränke löschen?")
                .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getraenkeLoeschen(zuLoeschendeGetraenke);
                    }
                })
                .setNegativeButton("Abbrechen", null);
        builder.create().show();
    }

    private void getraenkeLoeschen(List<Drink> zuLoeschendeGetraenke) {
        for (Drink getraenk : zuLoeschendeGetraenke) {
            drinkDataSource.deleteDrink(getraenk.getId());
        }
        getraenkeLaden();
        exitSelectionMode();
        Toast.makeText(this, "Ausgewählte Getränke gelöscht.", Toast.LENGTH_SHORT).show();
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