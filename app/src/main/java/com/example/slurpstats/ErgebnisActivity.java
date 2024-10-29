package com.example.slurpstats;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ErgebnisActivity extends BaseActivity {

    private TextView textViewErgebnisDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);

        setupNavigationDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dein Alkoholblutkonzentration");
        }
        textViewErgebnisDetails = findViewById(R.id.ergebnis_view);
        Intent intent = getIntent();
        long ergebnisId = intent.getLongExtra("ergebnis_id", -1);

        if (ergebnisId != -1) {
            ergebnisAnzeigen(ergebnisId);
        } else {
            textViewErgebnisDetails.setText("Kein Ergebnis zum Anzeigen.");
        }
    }

    private void ergebnisAnzeigen(long ergebnisId) {
        ResultDataSource ergebnisDatenquelle = new ResultDataSource(this);
        ergebnisDatenquelle.open();
        Result ergebnis = ergebnisDatenquelle.getResultById(ergebnisId);
        ergebnisDatenquelle.close();

        ConsumptionDetailDataSource verbrauchsdetailDatenquelle = new ConsumptionDetailDataSource(this);
        verbrauchsdetailDatenquelle.open();
        List<ConsumptionDetail> details = verbrauchsdetailDatenquelle.getConsumptionDetailsByResultId(ergebnisId);
        verbrauchsdetailDatenquelle.close();

        StringBuilder ergebnisInfo = new StringBuilder();
        ergebnisInfo.append("Titel: ").append(ergebnis.getTitle()).append("\n");
        ergebnisInfo.append("Datum: ").append(ergebnis.getDate()).append("\n");
        ergebnisInfo.append("Geschlecht: ").append(ergebnis.getGender()).append("\n");
        ergebnisInfo.append("Gewicht: ").append(ergebnis.getWeight()).append(" kg\n");
        ergebnisInfo.append("Blutalkoholkonzentration: ").append(String.format("%.2f", ergebnis.getBloodAlcoholContent())).append("‰\n");
        ergebnisInfo.append("Konsumierte Getränke:\n");

        DrinkDataSource getraenkDatenquelle = new DrinkDataSource(this);
        getraenkDatenquelle.open();
        for (ConsumptionDetail detail : details) {
            Drink getraenk = getraenkDatenquelle.getDrinkById(detail.getDrinkId());
            ergebnisInfo.append("- ").append(getraenk.getName())
                    .append(": ").append(detail.getAmount()).append(" ml\n");
        }
        getraenkDatenquelle.close();

        textViewErgebnisDetails.setText(ergebnisInfo.toString());
    }
}
