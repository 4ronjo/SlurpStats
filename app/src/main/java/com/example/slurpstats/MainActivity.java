package com.example.slurpstats;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetraenkDialogFragment.GetraenkDialogListener {

    private EditText Koerpergewicht;
    private Spinner Geschlecht;
    private Button BerechnenenButton;
    private ImageButton GetraenkeHinzufuegenButton;
    private TextView AuswahlAnzeige;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Koerpergewicht = findViewById(R.id.Koerpergewicht);
        Geschlecht = findViewById(R.id.Biologisches_Geschlecht);
        BerechnenenButton = findViewById(R.id.BerechnenenButton);
        GetraenkeHinzufuegenButton = findViewById(R.id.getraenke_hinzufuegen_button);
        AuswahlAnzeige = findViewById(R.id.auswahl_anzeige);

        BerechnenenButton.setOnClickListener(this);
        GetraenkeHinzufuegenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGetraenkDialog();
            }
        });
    }

    private void openGetraenkDialog() {
        FragmentManager fm = getSupportFragmentManager();
        GetraenkDialogFragment dialog = new GetraenkDialogFragment();
        dialog.setGetraenkDialogListener(this);  // Setzt MainActivity als Listener
        dialog.show(fm, "getraenk_dialog");
    }

    public void onGetraenkSelected(String getraenk, String menge) {
        String aktuelleAuswahl = AuswahlAnzeige.getText().toString();
        String neueAuswahl = aktuelleAuswahl + "\n" + getraenk + ": " + menge + " ml";
        AuswahlAnzeige.setText(neueAuswahl);
    }

    @Override
    public void onClick(View v) {
        String koerpergewichtInput = Koerpergewicht.getText().toString();
        String geschlechtInput = Geschlecht.getSelectedItem().toString();
        // TODO: Berechnung der Alkoholblutkonzentration
    }

}