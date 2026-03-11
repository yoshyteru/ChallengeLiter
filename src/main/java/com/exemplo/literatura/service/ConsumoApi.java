package com.exemplo.literatura.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    public String obterDados(String endereco) {
        // HttpClient - cria cliente HTTP
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        // HttpRequest - configura a requisição GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            // HttpResponse - envia e recebe resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica se deu erro (status diferente de 200)
            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro na requisição. Status: " + response.statusCode());
            }

            // Retorna o corpo da resposta (JSON)
            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consumir API: " + e.getMessage());
        }
    }
}