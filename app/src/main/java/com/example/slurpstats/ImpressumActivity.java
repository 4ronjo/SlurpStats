package com.example.slurpstats;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ImpressumActivity extends BaseActivity {

    private TextView textViewImpressum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);

        setupNavigationDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Impressum");
        }

        textViewImpressum = findViewById(R.id.text_view_impressum);
        textViewImpressum.setMovementMethod(new ScrollingMovementMethod()); // Optional: Für Scrollen
        textViewImpressum.setText(getImpressumText());
    }

    private String getImpressumText() {
        return this.getString(R.string.impressum_text);
    }
}
