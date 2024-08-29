package com.pub.testmaps;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tour {
    private Map<String, String> rotas;
    private List<Item> itemList;
    public Tour(List<Item> itemList) {
        this.rotas = new HashMap<>();
        this.itemList = itemList;
    }

    public String processarRotas(Context context) {
        carregarRotas(context);
        String rotaEscolhida = gerarRotaEscolhida();
        String rotaOtimizada = obterRotaOtimizada(rotaEscolhida);
        return rotaOtimizada;
    }
    public void carregarRotas(Context context) {
        try (BufferedReader br = new BufferedReader( new InputStreamReader(
            context.getResources().openRawResource(R.raw.rotas)))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(" ");
                rotas.put(partes[0], partes[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "Erro ao ler arquivo: " + e.getMessage());
        }
    }
    public String gerarRotaEscolhida() {
        if (itemList.isEmpty()) {
            return "";
        }
        StringBuilder toStringBuilder = new StringBuilder();
        for (int i = 0; i < itemList.size(); i++) {
            Item elemento = itemList.get(i);
            if (elemento.isSelected()) {
                toStringBuilder.append(elemento.getId()); // concatena a string
            }
        }

        return toStringBuilder.toString();
    }

    public String obterRotaOtimizada(String rotaEscolhida) {return rotas.get(rotaEscolhida);}
}
