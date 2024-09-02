package com.pub.testmaps;

import android.content.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tour {

    private Map<String, String> rotas; // Mapa para armazenar as rotas otimizadas
    private List<Item> itemList; // Lista de itens selecionados para a rota

    public Tour(List<Item> itemList) {
        this.rotas = new HashMap<>();
        this.itemList = itemList;
    }

    /* Processa as rotas carregando as rotas disponíveis, gerando a rota escolhida
     e obtendo a rota otimizada */
    public String processarRotas(Context context) {
        carregarRotas(context);
        String rotaEscolhida = gerarRotaEscolhida();
        String rotaOtimizada = obterRotaOtimizada(rotaEscolhida);
        return rotaOtimizada;
    }

    // Carrega as rotas a partir de um arquivo de recursos
    public void carregarRotas(Context context) {
        List<String> lines = Util.readLinesFromFile(context, R.raw.rotas); // Lê as linhas do arquivo de rotas
        this.rotas = Util.getRoutes(lines);
    }

    // Gera a rota baseada nos itens que foram selecionados
    public String gerarRotaEscolhida() {
        if (itemList.isEmpty()) {
            return ""; // Retorna uma string vazia se a lista de itens estiver vazia
        }
        StringBuilder toStringBuilder = new StringBuilder(); // Para construir a string da rota
        for (int i = 0; i < itemList.size(); i++) {
            Item elemento = itemList.get(i);
            if (elemento.isSelected()) {
                toStringBuilder.append(elemento.getId()); // Adiciona o ID do item selecionado à string
            }
        }
        return toStringBuilder.toString(); // Retorna a string da rota escolhida
    }

    // Obtém a rota otimizada a partir do mapa de rotas usando a rota escolhida como chave
    public String obterRotaOtimizada(String rotaEscolhida) {
        return rotas.get(rotaEscolhida);
    }
}
