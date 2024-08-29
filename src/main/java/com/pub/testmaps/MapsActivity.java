package com.pub.testmaps;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.UiSettings;
import com.pub.testmaps.databinding.ActivityMapsBinding;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtenha o SupportMapFragment e seja notificado quando o mapa estiver pronto para ser usado
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String action = getIntent().getStringExtra("ACTION");
        ListActivity listActivity = new ListActivity();
        List<String> names = listActivity.getNameItem();
        List<LatLng> locations = getLocations();

        // Chama getCurrentLocation para mover a câmera para a localização atual
        getCurrentLocation();

        try{
            if (action.equals("SHOW_POINTS")){
                plotAllPoint(names, locations);
            } else {
                String route = action;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (char c : route.toCharArray()) {

                    int point = c - '0'; // Trocar essa aboradagem caso use string ao invés de char
                    LatLng location = locations.get(point);

                    mMap.addMarker(new MarkerOptions().position(location).title(names.get(point)));
                    builder.include(location);
                }

                // Move a câmera para que todos os marcadores sejam visíveis
                CameraUpdate camUpd = CameraUpdateFactory.newLatLngBounds(builder.build(), 250);
                mMap.moveCamera(camUpd);

                //Gera rotas entre locais consecutivos
                for (int i = 0; i < route.length(); i++) {

                    int current = route.charAt(i) - '0';
                    int next;
                    if (i == route.length() - 1) {
                        next = route.charAt(0) - '0';  // Volta ao primeiro ponto
                    } else {
                        next = route.charAt(i + 1) - '0';  // Próximo ponto na rota
                    }

                    LatLng origin = locations.get(current);
                    LatLng destination = locations.get(next);
                    String requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=" + origin.latitude + "," + origin.longitude + "&" +
                            "destination=" + destination.latitude + "," + destination.longitude + "&" +
                            "mode=walking" + "&" +
                            "key=AIzaSyAfVH2B-3p-XW0GSqWyLWd74iQi6sLLz8Q";

                    fetchUrl(requestApi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MAPS_ERROR", "Erro: " + e.getMessage());
        }
    }

    private void plotAllPoint(List<String> names, List<LatLng> locations){

        for (int i = 0; i < names.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(names.get(i)));
        }
        // Move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(7),16));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // Se a solicitação for cancelada, o array de resultados estará vazio.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissão concedida, então podemos obter a localização atual
                    getCurrentLocation();
                } else {
                    // Permissão negada, você pode mostrar uma mensagem ao usuário
                }
            }
        }
    }

    private void getCurrentLocation() {

        // Verifica se tem permissão para acessar a localização
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        mMap.setMyLocationEnabled(true);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private List<LatLng> getLocations(){
        // Adicionando coordenadas para cada local
        List<LatLng> locations = new ArrayList<>();
        locations.add(new LatLng(-22.018270665425366, -47.890969226317225)); // Catedral de S.C. Borromeu (Marco zero)
        locations.add(new LatLng(-22.011190547342242, -47.89631873688992)); // Observatório Astronômico da USP
        locations.add(new LatLng(-22.018985859429932, -47.89281029587647)); // CDCC USP
        locations.add(new LatLng(-22.016767557392395, -47.89065900177282)); // Museu de Ciência Prof.
        locations.add(new LatLng(-22.011967233250427, -47.89543093371435)); // Praça XV
        locations.add(new LatLng(-22.019767089562574, -47.8912092288818));  // Mercado Municipal
        locations.add(new LatLng(-22.013799214489435, -47.890420180214115)); // EE. Álvaro Guião
        locations.add(new LatLng(-22.01577971255527, -47.89355120151492));  // Teatro Municipal
        locations.add(new LatLng(-22.017702748745066, -47.8906203585379));  // Palácio Conde do Pinhal
        locations.add(new LatLng(-22.007288506973985, -47.89456424037163)); // ICMC - Museu da Computação
        locations.add(new LatLng(-22.018270665425366, -47.890969226317225));
        return locations;
    }


    /////////////////////////// Métodos para gerar rotas ////////////////////////////////////

    private void drawRoute(String jsonString) {
        try {

            Log.d("DirectionAPIResponse", jsonString);

            if (jsonString == null) {
                Log.e("drawRoute", "Erro ao obter a rota");
                return;
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray routes = jsonObject.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONObject poly = route.getJSONObject("overview_polyline");
            String polyline = poly.getString("points");

            List<LatLng> list = decodePoly(polyline);

            mMap.addPolyline(new PolylineOptions().addAll(list).width(12).color(Color.BLUE));

        } catch (JSONException e) {
            Log.e("drawRoute", "Erro ao processar JSON", e);
        } catch (Exception e) {
            Log.e("drawRoute", "Erro desconhecido", e);
        }
    }

    private class FetchUrlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Log.e("fetchUrl", "Erro ao obter a rota", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Chame o método drawRoute ou qualquer outra lógica que dependa do resultado
            if (result != null) {
                drawRoute(result);
            }
        }
    }

    private void fetchUrl(String requestUrl) {
        new FetchUrlTask().execute(requestUrl);
    }


    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }

        return poly;
    }

}