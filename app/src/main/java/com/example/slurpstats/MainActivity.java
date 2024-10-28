package com.example.slurpstats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Clean-Code: Verwendung von @Override
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, GetraenkDialogFragment.GetraenkDialogListener {

    private EditText editTextGewicht;
    private Spinner spinnerGeschlecht;
    private Button buttonBerechnen;
    private ImageButton buttonGetraenkHinzufuegen;
    private TextView textViewAuswahl;

    private DrinkDataSource getraenkDatenquelle;
    private List<ConsumptionDetail> ausgewählteVerbrauchsdetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout-Datei entsprechend anpassen

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        editTextGewicht = findViewById(R.id.edit_text_gewicht);
        spinnerGeschlecht = findViewById(R.id.spinner_geschlecht);
        buttonBerechnen = findViewById(R.id.BerechnenenButton);
        buttonGetraenkHinzufuegen = findViewById(R.id.getraenke_hinzufuegen_button);
        textViewAuswahl = findViewById(R.id.getraenke_hinzufuegen_text);

        buttonBerechnen.setOnClickListener(this);
        buttonGetraenkHinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getraenkDialogOeffnen();
            }
        });

        getraenkDatenquelle = new DrinkDataSource(this);
        getraenkDatenquelle.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getraenkDatenquelle != null) {
            getraenkDatenquelle.close();
        }
    }

    private void getraenkDialogOeffnen() {
        FragmentManager fm = getSupportFragmentManager();
        GetraenkDialogFragment dialog = new GetraenkDialogFragment();
        dialog.setGetraenkDialogListener(this);
        dialog.show(fm, "getraenk_dialog");
    }

    @Override
    public void onGetraenkSelected(String getraenkName, String mengeStr) {
        try {
            double menge = Double.parseDouble(mengeStr);
            Drink getraenk = getraenkDatenquelle.getDrinkByName(getraenkName);

            if (getraenk != null) {
                ConsumptionDetail detail = new ConsumptionDetail();
                detail.setDrinkId(getraenk.getId());
                detail.setAmount(menge);
                ausgewählteVerbrauchsdetails.add(detail);

                String aktuelleAuswahl = textViewAuswahl.getText().toString();
                String neueAuswahl = aktuelleAuswahl + "\n" + getraenkName + ": " + menge + " ml";
                textViewAuswahl.setText(neueAuswahl);
            }
        } catch (NumberFormatException e) {
            // Clean-Code: Exception behandeln und Nutzer informieren
            Toast.makeText(this, "Bitte geben Sie eine gültige Menge ein.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("MainActivity", "Fehler bei der Getränkeauswahl", e);
            Toast.makeText(this, "Ein Fehler ist aufgetreten.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        String gewichtInput = editTextGewicht.getText().toString();
        String geschlechtInput = spinnerGeschlecht.getSelectedItem().toString();

        try {
            double gewicht = Double.parseDouble(gewichtInput);

            double blutalkoholwert = berechneBlutalkoholwert(gewicht, geschlechtInput, ausgewählteVerbrauchsdetails);

            ResultDataSource ergebnisDatenquelle = new ResultDataSource(this);
            ergebnisDatenquelle.open();

            int ergebnisNummer = ergebnisDatenquelle.getNumberOfResults() + 1;
            String titel = "Ergebnis Nr. " + ergebnisNummer;
            String aktuellesDatum = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss",
                    Locale.getDefault()).format(new Date());

            Result ergebnis = ergebnisDatenquelle.addResult(geschlechtInput, gewicht, blutalkoholwert, aktuellesDatum, titel);

            ConsumptionDetailDataSource verbrauchsdetailDatenquelle = new ConsumptionDetailDataSource(this);
            verbrauchsdetailDatenquelle.open();
            for (ConsumptionDetail detail : ausgewählteVerbrauchsdetails) {
                detail.setResultId(ergebnis.getId());
                verbrauchsdetailDatenquelle.addConsumptionDetail(detail.getResultId(), detail.getDrinkId(), detail.getAmount());
            }
            verbrauchsdetailDatenquelle.close();
            ergebnisDatenquelle.close();

            Intent intent = new Intent(this, ErgebnisActivity.class);
            intent.putExtra("ergebnis_id", ergebnis.getId());
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Bitte geben Sie ein gültiges Gewicht ein.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("MainActivity", "Fehler bei der Berechnung", e);
            Toast.makeText(this, "Ein Fehler ist aufgetreten.", Toast.LENGTH_SHORT).show();
        }
    }

    private double berechneBlutalkoholwert(double gewicht, String geschlecht, List<ConsumptionDetail> verbrauchsdetails) {
        double gesamterAlkoholInGramm = 0.0;
        for (ConsumptionDetail detail : verbrauchsdetails) {
            Drink getraenk = getraenkDatenquelle.getDrinkById(detail.getDrinkId());
            double alkoholgehalt = getraenk.getAlcoholContent();
            double mengeInMl = detail.getAmount();

            double alkoholInGramm = (mengeInMl / 1000) * (alkoholgehalt / 100) * 0.8 * 1000;
            gesamterAlkoholInGramm += alkoholInGramm;
        }

        double reduktionsfaktor = geschlecht.equalsIgnoreCase("Männlich") ? 0.68 : 0.55;
        double blutalkoholkonzentration = (gesamterAlkoholInGramm) / (gewicht * reduktionsfaktor);

        return blutalkoholkonzentration;
    }
}
