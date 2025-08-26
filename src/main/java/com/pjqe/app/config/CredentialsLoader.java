package com.pjqe.app.config;

import com.pjqe.app.entity.ProjectCredentials;
import com.pjqe.app.repository.CredentialsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CredentialsLoader {

    private final CredentialsRepository credentialsRepository;
    private final AppCredentials appCredentials;

    public CredentialsLoader(CredentialsRepository credentialsRepository, AppCredentials appCredentials) {
        this.credentialsRepository = credentialsRepository;
        this.appCredentials = appCredentials;
    }

    @PostConstruct
    public void loadCredentials() {
        List<ProjectCredentials> credentials = credentialsRepository.findAll();
        for (ProjectCredentials credential : credentials) {
            switch (credential.getKey()) {
                case "api.key":
                    appCredentials.setApiKey(credential.getValue());
                    break;
                case "assemblyai.api-key":
                    appCredentials.setAssemblyaiApiKey(credential.getValue());
                    break;
                // Adicione outros cases para futuras chaves de API
            }
        }
        System.out.println("Credenciais carregadas do banco de dados com sucesso!");
    }
}

