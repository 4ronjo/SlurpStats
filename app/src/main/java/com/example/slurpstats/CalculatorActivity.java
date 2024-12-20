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

import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalculatorActivity extends BaseActivity implements
        View.OnClickListener, GetraenkDialogFragment.GetraenkDialogListener {
    private EditText editTextGewicht;
    private Spinner spinnerGeschlecht;
    private Button buttonBerechnen;
    private ImageButton buttonGetraenkHinzufuegen;
    private TextView textViewAuswahl;
    private ImageButton buttonReset;
    private Button buttonabbrechen;

    private DrinkDataSource getraenkDatenquelle;
    private List<ConsumptionDetail> ausgewaehlteVerbrauchsdetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        setupNavigationDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        editTextGewicht = findViewById(R.id.edit_text_gewicht);
        spinnerGeschlecht = findViewById(R.id.spinner_geschlecht);
        buttonBerechnen = findViewById(R.id.BerechnenenButton);
        buttonGetraenkHinzufuegen = findViewById(R.id.getraenke_hinzufuegen_button);
        textViewAuswahl = findViewById(R.id.auswahl_anzeige);
        buttonReset = findViewById(R.id.button_reset);
        buttonabbrechen = findViewById(R.id.button_abbrechen);

        buttonReset.setOnClickListener(this);
        buttonBerechnen.setOnClickListener(this);
        buttonGetraenkHinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getraenkDialogOeffnen();
            }
        });

        buttonabbrechen.setOnClickListener(view -> finish());
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
    public void onGetraenkSelected(String getraenkNameMitProzent, String mengeStr) {
        try {
            double menge = Double.parseDouble(mengeStr);

            String getraenkName = getraenkNameMitProzent.split(" \\(")[0];

            Drink getraenk = getraenkDatenquelle.getDrinkByName(getraenkName);

            if (getraenk != null) {
                ConsumptionDetail detail = new ConsumptionDetail();
                detail.setDrinkId(getraenk.getId());
                detail.setAmount(menge);
                ausgewaehlteVerbrauchsdetails.add(detail);

                String aktuelleAuswahl = textViewAuswahl.getText().toString();
                String neueAuswahl = aktuelleAuswahl + "\n" + getraenkName + ": " + menge + " ml";
                textViewAuswahl.setText(neueAuswahl);
            } else {
                Toast.makeText(this, getString(R.string.Drink_not_found), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.Valid_amount), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("CalculatorActivity", "Fehler bei der Getränkeauswahl", e);
            Toast.makeText(this, getString(R.string.Error), Toast.LENGTH_SHORT).show();
        }
    }


    public void onClick(View v) {
        if (v.getId() == R.id.BerechnenenButton) {
            berechnungDurchfuehren();
        } else if (v.getId() == R.id.button_reset) {
            auswahlZuruecksetzen();
        }
    }

    private void auswahlZuruecksetzen() {
        ausgewaehlteVerbrauchsdetails.clear();
        textViewAuswahl.setText(getString(R.string.Selected_Drinks));
        Toast.makeText(this, getString(R.string.Selection_reset), Toast.LENGTH_SHORT).show();
    }



    private void berechnungDurchfuehren() {
        String gewichtInput = editTextGewicht.getText().toString();
        String geschlechtInput = spinnerGeschlecht.getSelectedItem().toString();

        try {
            double gewicht = Double.parseDouble(gewichtInput);

            if (ausgewaehlteVerbrauchsdetails.isEmpty()) {
                Toast.makeText(this, getString(R.string.Atleast_one_drink), Toast.LENGTH_SHORT).show();
                return;
            }

            double blutalkoholwert = berechneBlutalkoholwert(gewicht, geschlechtInput, ausgewaehlteVerbrauchsdetails);

            ResultDataSource ergebnisDatenquelle = new ResultDataSource(this);
            ergebnisDatenquelle.open();

            int ergebnisNummer = ergebnisDatenquelle.getNumberOfResults() + 1;
            String titel = getString(R.string.Result_nr) + " " + ergebnisNummer;
            String aktuellesDatum = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss",
                    Locale.getDefault()).format(new Date());

            Result ergebnis = ergebnisDatenquelle.addResult(geschlechtInput, gewicht, blutalkoholwert, aktuellesDatum, titel);

            ConsumptionDetailDataSource verbrauchsdetailDatenquelle = new ConsumptionDetailDataSource(this);
            verbrauchsdetailDatenquelle.open();
            for (ConsumptionDetail detail : ausgewaehlteVerbrauchsdetails) {
                detail.setResultId(ergebnis.getId());
                verbrauchsdetailDatenquelle.addConsumptionDetail(detail.getResultId(), detail.getDrinkId(), detail.getAmount());
            }
            verbrauchsdetailDatenquelle.close();
            ergebnisDatenquelle.close();

            Intent intent = new Intent(this, ErgebnisActivity.class);
            intent.putExtra("ergebnis_id", ergebnis.getId());
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.Valid_weigth), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("CalculatorActivity", "Fehler bei der Berechnung", e);
            Toast.makeText(this, getString(R.string.Error), Toast.LENGTH_SHORT).show();
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
