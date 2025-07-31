package com.challengeAlura.ChallengeLiteratura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        if (json == null || json.isBlank()) {
            System.out.println("El JSON recibido está vacío.");
            throw new RuntimeException("La respuesta está vacía, no se puede convertir.");
        }

        try {
            return objectMapper.readValue(json, clase);
        } catch (Exception e) {
            System.out.println("Error al convertir JSON:");
            throw new RuntimeException(e);
        }
    }
}
