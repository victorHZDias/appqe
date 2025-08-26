package com.pjqe.app.service;

import com.pjqe.app.entity.ProjectCredentials;
import com.pjqe.app.repository.CredentialsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CredentialsService {

    private final CredentialsRepository credentialsRepository;

    public CredentialsService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    public List<ProjectCredentials> getAllCredentials() {
        return credentialsRepository.findAll();
    }

    @Transactional
    public void updateCredentials(Map<String, String> credentialsMap) {
        credentialsMap.forEach((key, value) -> {
            ProjectCredentials credential = credentialsRepository.findById(key)
                    .orElse(new ProjectCredentials()); // Cria se não existir
            credential.setKey(key);
            credential.setValue(value);
            credentialsRepository.save(credential);
        });
    }

    // MÉTODO ADICIONADO que estava a faltar
    @Transactional
    public ProjectCredentials saveNewCredential(ProjectCredentials credential) {
        if (credential.getKey() == null || credential.getKey().isBlank()) {
            throw new IllegalArgumentException("A chave da credencial não pode ser nula ou vazia.");
        }
        if (credentialsRepository.existsById(credential.getKey())) {
            throw new IllegalArgumentException("Uma credencial com esta chave já existe.");
        }
        return credentialsRepository.save(credential);
    }
}
