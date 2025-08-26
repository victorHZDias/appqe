package com.pjqe.app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppCredentials {

    @Value("${api.key}")
    private String apiKey;
    private String assemblyaiApiKey; // Corresponde à chave 'assemblyai.api-key'

    // Getters e Setters
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAssemblyaiApiKey() {
        return assemblyaiApiKey;
    }

    public void setAssemblyaiApiKey(String assemblyaiApiKey) {
        this.assemblyaiApiKey = assemblyaiApiKey;
    }

    @PostConstruct
    public void validate() {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException(
                    "A chave da API (api.key) não foi configurada. " +
                            "Verifique a variável de ambiente API_KEY_APP ou forneça um valor padrão."
            );
        }
    }
}
