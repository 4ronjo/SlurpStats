package com.example.slurpstats;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ImpressumActivity extends AppCompatActivity {

    private TextView textViewImpressum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);

        textViewImpressum = findViewById(R.id.text_view_impressum);
        textViewImpressum.setText(getImpressumText());
    }

    private String getImpressumText() {
        return "Impressum\n\n" +
                "Angaben gemäß § 5 TMG:\n" +
                "Max Mustermann\n" +
                "Musterstraße 1\n" +
                "12345 Musterstadt\n" +
                "\n" +
                "Vertreten durch:\n" +
                "Max Mustermann\n" +
                "\n" +
                "Kontakt:\n" +
                "Telefon: 01234-56789\n" +
                "E-Mail: max@mustermann.de\n" +
                "\n" +
                "Haftungsausschluss:\n" +
                "Die Inhalte unserer Seiten wurden mit größter Sorgfalt erstellt. " +
                "Für die Richtigkeit, Vollständigkeit und Aktualität der Inhalte können wir jedoch keine Gewähr übernehmen.\n";
    }
}
