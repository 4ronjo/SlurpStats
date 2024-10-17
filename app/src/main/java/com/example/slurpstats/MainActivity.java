package com.example.slurpstats;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText Koepergewicht;
    private EditText Alkoholmenge;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Koepergewicht = findViewById(R.id.Koerpergewicht);
        Alkoholmenge = findViewById(R.id.Alkoholmenge);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Koepergewicht_input = Koepergewicht.getText().toString();
                String Alkoholmenge_input = Alkoholmenge.getText().toString();
            }
        });
    }
}