package com.pub.testmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private List<Item> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        // Adicione items à lista
        itemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int imageResource = getResources().getIdentifier(
                    "image" + i, "drawable", getPackageName());
            itemList.add(new Item(i, getNameItem().get(i), imageResource));
        }
        itemList.get(0).setSelected(true);

        // Inicializa o RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o adaptador
        ItemAdapter itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);
    }

    public void goToTour(View view) {
        Item item_0 = itemList.get(0);
        Tour tour = new Tour(itemList);
        String routeChosen = tour.gerarRotaEscolhida();
        int numberPoint = routeChosen.length();
        String routeOptimized = tour.processarRotas(this);

        Intent intent = new Intent(ListActivity.this, MapsActivity.class);
        if (numberPoint <= 3) {
            intent.putExtra("ACTION", routeChosen);
        } else {
            intent.putExtra("ACTION", routeOptimized);
        }
        startActivity(intent);
    }

    public List<String> getNameItem(){
        List<String> nameItem = new ArrayList<>();
        nameItem.add("Catedral de S.C.");
        nameItem.add("Observatório Astronômico");
        nameItem.add("CDCC USP");
        nameItem.add("Museu de Ciência Prof.M. Tolentino");
        nameItem.add("Praça XV");
        nameItem.add("Mercado Municipal");
        nameItem.add("Escola Estadual Álvaro Guião");
        nameItem.add("Teatro Municipal");
        nameItem.add("Palácio Conde do Pinhal");
        nameItem.add("ICMC - Museu da Computação");
        return nameItem;
    }

    public void goToTest(View view) {
        Intent intent = new Intent(ListActivity.this, TestActivity.class);
        intent.putParcelableArrayListExtra("itemList", new ArrayList<>(itemList));
        startActivity(intent);
    }
}