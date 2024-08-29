package com.pub.testmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    private List<LatLng> location;
    private Map<String, LatLng> locations;
    private TextView textView;
    private String textRotaOtimizada;
    private List<Item> itemList;
    private Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.textView);

        itemList = getIntent().getParcelableArrayListExtra("itemList");
        tour = new Tour(itemList);

        locations = new HashMap<>();
        location = new ArrayList<>();

        location.add(new LatLng(-22.011967233250427, -47.89543093371435)); // Praça XV
        location.add(new LatLng(-22.01577971255527, -47.89355120151492)); // Teatro Municipal
        location.add(new LatLng(-22.018985859429932, -47.89281029587647)); // CDCC
        location.add(new LatLng(-22.018270665425366, -47.890969226317225)); // Catedral
        location.add(new LatLng(-22.013799214489435, -47.890420180214115)); // E.E. Alvaro
        location.add(new LatLng(-22.011947424137762, -47.89541233873506)); // Praça XV

        for(int i = 0; i < 6; i++){
            String chave = "" + i;
            locations.put(chave, location.get(i));
        }
        setLocation();
    }

    private double calcularDistancia(LatLng pontoInicio, LatLng pontoFim) {
        // Convertendo LatLng para objetos Location
        Location localizacaoInicio = new Location("localizacaoInicio");
        localizacaoInicio.setLatitude(pontoInicio.latitude);
        localizacaoInicio.setLongitude(pontoInicio.longitude);

        Location localizacaoFim = new Location("localizacaoFim");
        localizacaoFim.setLatitude(pontoFim.latitude);
        localizacaoFim.setLongitude(pontoFim.longitude);

        // Calculando a distância
        double distanciaMetros = localizacaoInicio.distanceTo(localizacaoFim);

        //Log.e("Tipo de DistanciaKm", ""+ distanciaMetros);
        return distanciaMetros;
    }

    private void setLocation() {
        //String rotaOtimizada = tour.processarRotas(this);
        String distancia = "" + calcularDistancia(location.get(0), location.get(1));
        //String p = "" + rotaOtimizada.charAt(0);
        //tring ponto_i = locations.get(p).toString();
        textView.setText(distancia);
    }
}