package com.exemplo.literatura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class ConverteDados {

    // ObjectMapper do Jackson - versão 2.16
    private ObjectMapper mapper = new ObjectMapper()
            // Ignora campos do JSON que não existem na classe Java
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Método genérico: converte JSON para qualquer classe
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON: " + e.getMessage());
        }
    }
}