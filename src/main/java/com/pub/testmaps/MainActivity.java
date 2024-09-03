package com.pub.testmaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declaração das opções de layout, que serão utilizadas como botões na interface
    private LinearLayout listViewOption;
    private LinearLayout mapViewOption;
    private LinearLayout infoViewOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Associa as variáveis de layout aos elementos da interface, utilizando seus IDs
        listViewOption = findViewById(R.id.listViewOption);
        mapViewOption = findViewById(R.id.mapViewOption);
        infoViewOption = findViewById(R.id.infoViewOption);

        // Inicializa os listeners de clique para cada uma das opções
        initializeOnClickListener();
    }

    private void initializeOnClickListener() {

        // Define o que acontece quando a opção listViewOption é clicada
        listViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ListActivity.class); // Chama o método para iniciar a ListActivity
            }
        });

        // Define o que acontece quando a opção mapViewOption é clicada
        mapViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(MapsActivity.class); // Chama o método para iniciar a MapsActivity
            }
        });

        // Define o que acontece quando a opção infoViewOption é clicada
        infoViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(InfoActivity.class); // Chama o método para iniciar a InfoActivity
            }
        });
    }

    // Método para mudar de tela ao clicar em uma das opções
    public void goToActivity(Class<?> destinationClass) {

        // Cria um Intent para iniciar a nova atividade
        Intent intent = new Intent(MainActivity.this, destinationClass);

        // Adiciona um extra ao Intent se a atividade de destino for MapsActivity
        if (MapsActivity.class.equals(destinationClass)) {
            intent.putExtra("ACTION", "SHOW_POINTS"); // Adiciona a ação "SHOW_POINTS" ao Intent
        }

        // Inicia a nova atividade
        startActivity(intent);
    }
}