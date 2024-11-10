package com.example.slurpstats;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class HilfeActivity extends BaseActivity {

    private TextView textViewHilfe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilfe);

        textViewHilfe = findViewById(R.id.text_view_hilfe);

        setupNavigationDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.Help));
        }
        textViewHilfe.setMovementMethod(new ScrollingMovementMethod());
        textViewHilfe.setText(getString(R.string.hilfe_text));

    }
}
