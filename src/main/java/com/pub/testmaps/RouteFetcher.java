package com.pub.testmaps;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteFetcher {

    private GoogleMap mMap;
    private Context mContext;

    public RouteFetcher(GoogleMap map, Context context) {
        this.mMap = map;
        this.mContext = context;
    }

    public void fetchUrl(String requestUrl) {
        new FetchUrlTask().execute(requestUrl);
    }

    private void drawRoute(String jsonString) {
        try {
            Log.d("DirectionAPIResponse", jsonString);

            if (jsonString == null) {
                Log.e("drawRoute", "Erro ao obter a rota: JSON nulo");
                return;
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray routes = jsonObject.getJSONArray("routes");
            if (routes.length() == 0) {
                Log.e("drawRoute", "Nenhuma rota encontrada no JSON");
                return;
            }

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
            if (result != null) {
                drawRoute(result);
            } else {
                Log.e("fetchUrl", "Resultado nulo recebido");
            }
        }
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

