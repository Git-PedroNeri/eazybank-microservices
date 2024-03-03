package com.eazybytes.gatewayserver.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;

public class KeycloakTokenService {

    final static String  TOKEN_URL="http://localhost:7080/realms/master/protocol/openid-connect/token";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Corpo da requisição
        Map<String, String> formData = getFormData();
        // Realiza a requisição
        HttpResponse<String> response = sendPostRequest(TOKEN_URL, formData);
        // Exibe a resposta
        System.out.println("Status: " + response.statusCode());
        System.out.println("Corpo da resposta: " + response.body());
    }

    private static Map<String, String> getFormData() {
        Map<String, String> formData = new HashMap<>();
        formData.put("grant_type", "client_credentials");
        formData.put("client_id", "KEYCLOAK_CLIENT_CREDENTIALS_GRANT_TYPE_FLOW");
        formData.put("client_secret", "yGVBagwqWW842d6eJ9zCkWxjsXgMEmUO");
        formData.put("scope", "openid email profile");
        return formData;
    }

    public static HttpResponse<String> sendPostRequest(String url, Map<String, String> formData)
            throws IOException, InterruptedException {
        // Cria o cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Monta o corpo da requisição
        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (requestBody.length() > 0) {
                requestBody.append("&");
            }
            requestBody.append(entry.getKey()).append("=").append(entry.getValue());
        }

        // Cria a requisição HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Envia a requisição e retorna a resposta
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

