package com.example.slurpstats;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HilfeActivity extends AppCompatActivity {

    private TextView textViewHilfe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilfe);

        textViewHilfe = findViewById(R.id.text_view_hilfe);

        String hilfeText = getString(R.string.hilfe_text);
        textViewHilfe.setText(hilfeText);
    }
}
