package com.example.slurpstats;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class GetraenkDialogFragment extends DialogFragment {

    private Spinner getraenkAuswahl;
    private EditText getraenkeMengeInput;
    private GetraenkDialogListener listener;

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

        // Layout inflating
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_getraenk_hinzufuegen, null);

        // Initialize the views
        getraenkAuswahl = view.findViewById(R.id.getraenk_auswahl);
        getraenkeMengeInput = view.findViewById(R.id.getraenke_menge_input);
        Button hinzufuegenButton = view.findViewById(R.id.hinzufuegen_button);

        hinzufuegenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedGetraenk = getraenkAuswahl.getSelectedItem().toString();
                String menge = getraenkeMengeInput.getText().toString();

                if (listener != null) {
                    listener.onGetraenkSelected(selectedGetraenk, menge);
                }

                dismiss();  // Close the dialog after adding
            }
        });

        builder.setView(view).setTitle("Getränk hinzufügen")
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
