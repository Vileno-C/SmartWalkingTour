package com.pub.testmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
    private TextView textViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textViewDescription = findViewById(R.id.textViewDescription); // Inicializa o TextView

        // Define o texto do TextView com a primeira linha lida do arquivo 'description'
        textViewDescription.setText(Util.readLinesFromFile(this, R.raw.description).get(0));
    }
}