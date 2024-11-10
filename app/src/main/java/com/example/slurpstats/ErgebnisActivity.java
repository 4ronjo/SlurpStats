package com.example.slurpstats;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ErgebnisActivity extends BaseActivity {

    private TextView textViewErgebnisDetails;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);

        setupNavigationDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.Your_alcohol_concentration));
        }

        textViewErgebnisDetails = findViewById(R.id.ergebnis_view);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(view -> finish());

        Intent intent = getIntent();
        long ergebnisId = intent.getLongExtra("ergebnis_id", -1);

        if (ergebnisId != -1) {
            ergebnisAnzeigen(ergebnisId);
        } else {
            textViewErgebnisDetails.setText(getString(R.string.No_result));
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
        ergebnisInfo.append(getString(R.string.Title)).append(": ").append(ergebnis.getTitle()).append("\n");
        ergebnisInfo.append(getString(R.string.Date)).append(": ").append(ergebnis.getDate()).append("\n");
        ergebnisInfo.append(getString(R.string.Gender)).append(": ").append(ergebnis.getGender()).append("\n");
        ergebnisInfo.append(getString(R.string.Weight)).append(": ").append(ergebnis.getWeight()).append(" kg\n");
        ergebnisInfo.append(getString(R.string.Blood_alcohol_concentration)).append(": ").append(String.format("%.2f", ergebnis.getBloodAlcoholContent())).append("‰\n");
        ergebnisInfo.append(getString(R.string.Consumed_Drinks)).append(" :\n");

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
