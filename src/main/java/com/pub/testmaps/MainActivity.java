package com.pub.testmaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout listViewOption;
    private LinearLayout mapViewOption;
    private LinearLayout infoViewOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewOption = findViewById(R.id.listViewOption);
        mapViewOption = findViewById(R.id.mapViewOption);
        infoViewOption = findViewById(R.id.infoViewOption);

        initializeOnClickListener();
    }

    private void initializeOnClickListener(){

        listViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ListActivity.class);
            }
        });
        mapViewOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){goToActivity(MapsActivity.class);}
        });
        infoViewOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ goToActivity(InfoActivity.class);}
        });
    }

    // Método para mudar de tela através do click
    public void goToActivity(Class<?> destinationClass) {

        Intent intent = new Intent(MainActivity.this, destinationClass);
        if (MapsActivity.class.equals(destinationClass)){
            intent.putExtra("ACTION", "SHOW_POINTS");
        }
        startActivity(intent);
    }
}