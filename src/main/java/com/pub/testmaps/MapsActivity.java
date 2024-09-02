package com.pub.testmaps;

import androidx.fragment.app.FragmentActivity;

import com.pub.testmaps.databinding.ActivityMapsBinding;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula o layout à atividade usando view binding
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtém o SupportMapFragment e define o callback para quando o mapa estiver pronto
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // Configura o callback onMapReady para quando o mapa estiver pronto
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap; // Atribui o mapa ao objeto GoogleMap

        // Classe para desenhar rotas usando a Directions API
        RouteFetcher routeFetcher = new RouteFetcher(mMap, this);

        // Obtém a ação enviada através do Intent
        String action = getIntent().getStringExtra("ACTION");

        // Processa os nomes e as coordenadas dos pontos turísticos
        List<String> names = Util.getNames(Util.readLinesFromFile(this, R.raw.name_coord));
        List<LatLng> locations = Util.getCoordinates(Util.readLinesFromFile(this, R.raw.name_coord));


        // Mover a câmera para a localização atual
        Util.getCurrentLocation(mMap, this);

        try {
            // Se a ação for "SHOW_POINTS", plota os pontos no mapa
            if (action.equals("SHOW_POINTS")) {
                Util.plotPoints(mMap, names, locations);
            } else {
                // Caso contrário, desenha a rota no mapa
                String route = action;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (char c : route.toCharArray()) {

                    // Converte o caractere da rota para um índice de ponto
                    int point = c - '0'; // Trocar essa abordagem caso use string ao invés de char
                    LatLng location = locations.get(point);

                    // Adiciona um marcador para cada ponto na rota
                    mMap.addMarker(new MarkerOptions().position(location).title(names.get(point)));
                    builder.include(location); // Inclui o ponto nos limites da câmera
                }

                // Move a câmera para que todos os marcadores sejam visíveis
                CameraUpdate camUpd = CameraUpdateFactory.newLatLngBounds(builder.build(), 250);
                mMap.moveCamera(camUpd);

                // Gera rotas entre locais consecutivos
                for (int i = 0; i < route.length(); i++) {

                    int current = route.charAt(i) - '0'; // Índice do ponto atual
                    int next;
                    if (i == route.length() - 1) {
                        next = route.charAt(0) - '0'; // Volta ao primeiro ponto
                    } else {
                        next = route.charAt(i + 1) - '0'; // Próximo ponto na rota
                    }

                    LatLng origin = locations.get(current); // Coordenadas de origem
                    LatLng destination = locations.get(next); // Coordenadas de destino

                    // Obtém a chave da API do Google Maps
                    String apiKey = getString(R.string.google_maps_api_key);

                    // Cria a URL para a requisição da rota
                    String requestApi = Util.createRouteUrl(origin, destination, apiKey);

                    // Faz a requisição da rota e desenha no mapa
                    routeFetcher.fetchUrl(requestApi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MAPS_ERROR", "Erro: " + e.getMessage());
        }
    }

    // Método chamado quando o resultado da solicitação de permissão é retornado
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Verifica se a permissão foi concedida usando um método utilitário
        if (Util.handlePermissionResult(requestCode, grantResults)) {
            // Se a permissão for concedida, obtém a localização atual
            Util.getCurrentLocation(mMap, this);
        } else {
            // Se a permissão for negada, exibe uma mensagem ao usuário
            Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show();
        }
    }
}