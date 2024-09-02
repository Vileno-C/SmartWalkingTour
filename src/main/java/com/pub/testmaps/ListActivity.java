package com.pub.testmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<Item> itemList; // Lista de pontos turísticos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        itemList = new ArrayList<>(); // Inicializa a lista de itens
        initializeList(); // Chama o método para adicionar itens à lista
        initializeRecyclerView(); // Chama o método para configurar o RecyclerView
    }

    private void initializeRecyclerView() {
        // Inicializa o RecyclerView, que exibirá a lista de itens
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o adaptador para conectar a lista de itens ao RecyclerView
        ItemAdapter itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);
    }

    // Método para inicializar a lista de itens
    private void initializeList() {
        // Processa os nomes lidos de um arquivo de recursos usando uma classe de utilidade (Util)
        List<String> names = Util.getNames(
                Util.readLinesFromFile(this, R.raw.name_coord));

        // Para cada nome, cria um novo item com um identificador de imagem e o adiciona à lista
        for (int i = 0; i < names.size(); i++) {
            int imageResource = getResources().getIdentifier( // Obtém o ID do recurso de imagem
                    "image" + i, "drawable", getPackageName());
            itemList.add(new Item(i, names.get(i), imageResource)); // Adiciona o item à lista
        }
    }

    // Método para iniciar o tour, chamado ao clicar em um botão na interface
    public void goToTour(View view) {

        // Cria um novo objeto Tour com a lista de itens
        Tour tour = new Tour(itemList);
        String routeChosen = tour.gerarRotaEscolhida(); // Gera a rota escolhida pelo usuário

        // Se nenhuma rota foi escolhida, exibe uma mensagem e retorna
        if (routeChosen.isEmpty()) {
            Toast.makeText(this, "Selecione alguns pontos turísticos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria um Intent para iniciar a MapsActivity
        Intent intent = new Intent(ListActivity.this, MapsActivity.class);
        String route;

        // Se a rota tiver menos de quatro pontos, a solução do TSP é trivial
        if (routeChosen.length() < 4) {
            route = routeChosen;
        } else {
            route = tour.processarRotas(this); // Obtém a rota otimizada
        }

        // Adiciona uma rota ao Intent como uma ação
        intent.putExtra("ACTION", route);
        startActivity(intent); // Inicia a MapsActivity
    }
}
