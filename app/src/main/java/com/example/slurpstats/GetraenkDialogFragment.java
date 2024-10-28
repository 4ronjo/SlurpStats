package com.example.slurpstats;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;
import java.util.stream.Collectors;

public class GetraenkDialogFragment extends DialogFragment {

    private Spinner spinnerGetraenkAuswahl;
    private EditText editTextGetraenkMenge;
    private GetraenkDialogListener listener;

    private DrinkDataSource getraenkDatenquelle;
    private List<Drink> getraenkeListe;

    public interface GetraenkDialogListener {
        void onGetraenkSelected(String getraenk, String menge);
    }

    public void setGetraenkDialogListener(GetraenkDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_getraenk_hinzufuegen, null);

        spinnerGetraenkAuswahl = view.findViewById(R.id.getraenk_auswahl);
        editTextGetraenkMenge = view.findViewById(R.id.getraenke_menge_input);
        Button buttonHinzufuegen = view.findViewById(R.id.hinzufuegen_button);

        getraenkDatenquelle = new DrinkDataSource(getContext());
        getraenkDatenquelle.open();
        getraenkeListe = getraenkDatenquelle.getAllDrinks();
        getraenkDatenquelle.close();

        // Adapter für Spinner einrichten
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                getraenkeListe.stream().map(Drink::getName).collect(Collectors.toList()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGetraenkAuswahl.setAdapter(adapter);

        buttonHinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ausgewähltesGetraenk = spinnerGetraenkAuswahl.getSelectedItem().toString();
                String menge = editTextGetraenkMenge.getText().toString();

                if (listener != null) {
                    listener.onGetraenkSelected(ausgewähltesGetraenk, menge);
                }

                dismiss();
            }
        });

        builder.setView(view)
                .setTitle("Getränk hinzufügen")
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}