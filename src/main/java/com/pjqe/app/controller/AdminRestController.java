package com.pjqe.app.controller;

import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.ProjectCredentials;
import com.pjqe.app.entity.Subscription;
import com.pjqe.app.entity.TranscriptionAudio;
import com.pjqe.app.model.dto.RegistrationRequest;
import com.pjqe.app.model.dto.SubscriptionUpgradeRequest;
import com.pjqe.app.repository.ClientRepository;
import com.pjqe.app.service.ClientService;
import com.pjqe.app.service.CredentialsService;
import com.pjqe.app.service.TranscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final TranscriptionService transcriptionService;
    private final CredentialsService credentialsService;

    public AdminRestController(ClientService clientService, ClientRepository clientRepository,
                               TranscriptionService transcriptionService, CredentialsService credentialsService) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.transcriptionService = transcriptionService;
        this.credentialsService = credentialsService;
    }

    // --- Endpoints de Gestão de Clientes ---

    @PostMapping("/add-client")
    public ResponseEntity<?> addClient(@RequestBody RegistrationRequest request) {
        try {
            Client newClient = clientService.registerNewClient(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar cliente: " + e.getMessage());
        }
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    @PostMapping("/clients/{id}/upgrade-subscription")
    public ResponseEntity<?> upgradeSubscription(@PathVariable Long id, @RequestBody SubscriptionUpgradeRequest request) {
        try {
            Subscription updatedSubscription = clientService.upgradeSubscription(id, request);
            return ResponseEntity.ok(updatedSubscription);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- Endpoints de Gestão de Transcrições ---

    @GetMapping("/transcriptions")
    public ResponseEntity<List<TranscriptionAudio>> getAllTranscriptions() {
        List<TranscriptionAudio> allTranscriptions = transcriptionService.getAllTranscriptions();
        return ResponseEntity.ok(allTranscriptions);
    }

    // --- Endpoints de Gestão de Credenciais ---

    @GetMapping("/credentials")
    public ResponseEntity<List<ProjectCredentials>> getCredentials() {
        return ResponseEntity.ok(credentialsService.getAllCredentials());
    }

    @PostMapping("/credentials/add")
    public ResponseEntity<?> addCredential(@RequestBody ProjectCredentials credential) {
        try {
            ProjectCredentials savedCredential = credentialsService.saveNewCredential(credential);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCredential);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/credentials/update")
    public ResponseEntity<Void> updateCredentials(@RequestBody Map<String, String> credentials) {
        credentialsService.updateCredentials(credentials);
        return ResponseEntity.ok().build();
    }
}
