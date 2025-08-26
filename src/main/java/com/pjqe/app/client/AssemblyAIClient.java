package com.pjqe.app.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@Component // Esta anotação torna a classe um bean gerenciado pelo Spring
public class AssemblyAIClient {

    private static final String API_SUBMIT_URL = "https://api.assemblyai.com/v2/transcript";

    private final String apiKey;

    public AssemblyAIClient(@Value("${assemblyai.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String submitTranscriptionRequest(TranscriptionRequest request) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_SUBMIT_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 201) {
            String transcriptionId = objectMapper.readTree(httpResponse.body()).get("id").asText();
            System.out.println("Requisição de transcrição enviada. ID: " + transcriptionId);
            return transcriptionId;
        } else {
            System.err.println("Erro ao enviar requisição. Código: " + httpResponse.statusCode() + " Corpo: " + httpResponse.body());
            throw new RuntimeException("Falha ao submeter requisição de transcrição.");
        }
    }

    public String getTranscriptionResult(String transcriptionId) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();
        String transcriptionUrl = API_SUBMIT_URL + "/" + transcriptionId;

        // Lógica de polling
        while (true) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(transcriptionUrl))
                    .header("Authorization", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            TranscriptionResult result = objectMapper.readValue(response.body(), TranscriptionResult.class);

            if ("completed".equalsIgnoreCase(result.getStatus())) {
                System.out.println("Transcrição concluída com sucesso!");
                return result.getText();
            } else if ("failed".equalsIgnoreCase(result.getStatus())) {
                System.err.println("Transcrição falhou.");
                return null;
            }

            System.out.println("Status da transcrição: " + result.getStatus() + ". Aguardando 5 segundos...");
            TimeUnit.SECONDS.sleep(5);
        }
    }
}
