package com.pub.testmaps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    public static final int PERMISSION_REQUEST_CODE = 100;

    // Método genérico para ler dados de um arquivo
    public static List<String> readLinesFromFile(Context context, int resourceId) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceId)))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UTIL_ERROR", "Erro ao ler arquivo: " + e.getMessage());
        }
        return lines;
    }

    // Método para processar as linhas conforme necessário
    public static List<String> getNames(List<String> lines) {
        List<String> names = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("; ");
            names.add(parts[0]);
        }
        return names;
    }

    // Método para processar coordenadas
    public static List<LatLng> getCoordinates(List<String> lines) {
        List<LatLng> locations = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("; ")[1].split(", ");
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            locations.add(new LatLng(latitude, longitude));
        }
        return locations;
    }

    // Método para processar rotas
    public static Map<String, String> getRoutes(List<String> lines) {
        Map<String, String> routes = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(" ");
            routes.put(parts[0], parts[1]);
        }
        return routes;
    }


    // Método para verificar se a permissão foi concedida
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    // Método para solicitar permissão
    public static void requestLocationPermission(Activity activity) {
        String permission = android.Manifest.permission.ACCESS_FINE_LOCATION;
        if (!isPermissionGranted(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    // Método para lidar com o resultado da solicitação de permissão
    public static boolean handlePermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    // Método para obter a localização atual
    public static void getCurrentLocation(GoogleMap map, Activity activity) {
        // Verifica se tem permissão para acessar a localização
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission(activity);
        }
        map.setMyLocationEnabled(true);
    }

    // Método para gerar a URL das rotas
    public static String createRouteUrl(LatLng origin, LatLng destination, String apiKey) {
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude + "&" +
                "destination=" + destination.latitude + "," + destination.longitude + "&" +
                "mode=walking" + "&" + "key=" + apiKey;
    }

    // Método para plotar todos pontos no mapa
    public static void plotPoints(GoogleMap map, List<String> names, List<LatLng> locations) {

        for (int i = 0; i < names.size(); i++) {
            map.addMarker(new MarkerOptions().position(locations.get(i)).title(names.get(i)));
        }
        // Move a câmera para que todos os marcadores sejam visíveis
        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(locations.get(7), 16);
        map.moveCamera(camUpd);
    }
}

