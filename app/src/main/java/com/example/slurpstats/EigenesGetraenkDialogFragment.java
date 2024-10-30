package com.example.slurpstats;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EigenesGetraenkDialogFragment extends DialogFragment {

    private EditText editTextGetraenkName;
    private EditText editTextAlkoholgehalt;
    private DrinkDataSource getraenkDatenquelle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eigenes_getraenk_hinzufuegen, null);

        editTextGetraenkName = view.findViewById(R.id.edit_text_getraenk_name);
        editTextAlkoholgehalt = view.findViewById(R.id.edit_text_alkoholgehalt);
        Button buttonSpeichern = view.findViewById(R.id.button_speichern);

        getraenkDatenquelle = new DrinkDataSource(getContext());
        getraenkDatenquelle.open();

        buttonSpeichern.setOnClickListener(v -> {
            String name = editTextGetraenkName.getText().toString().trim();
            String alkoholgehaltStr = editTextAlkoholgehalt.getText().toString().trim();

            if (name.isEmpty() || alkoholgehaltStr.isEmpty()) {
                Toast.makeText(getContext(), "Bitte alle Felder ausfüllen.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double alkoholgehalt = Double.parseDouble(alkoholgehaltStr);

                getraenkDatenquelle.addDrink(name, alkoholgehalt);

                Toast.makeText(getContext(), "Getränk hinzugefügt.", Toast.LENGTH_SHORT).show();

                if (getTargetFragment() instanceof GetraenkDialogFragment) {
                    ((GetraenkDialogFragment) getTargetFragment()).aktualisiereGetraenkeListe();
                }

                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Ungültiger Alkoholgehalt.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view)
                .setTitle("Eigenes Getränk hinzufügen")
                .setNegativeButton("Abbrechen", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getraenkDatenquelle.close();
    }
}
