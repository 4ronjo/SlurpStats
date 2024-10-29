package com.example.slurpstats;

import android.os.Bundle;

public class HilfeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilfe);

        setupNavigationDrawer();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hilfe");
        }

    }
}
