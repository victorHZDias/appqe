package com.pjqe.app.controller;

import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.Subscription;
import com.pjqe.app.model.dto.PersonalizedPlanRequest;
import com.pjqe.app.model.dto.RegistrationRequest;
import com.pjqe.app.model.dto.SubscriptionUpgradeRequest;
import com.pjqe.app.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients") // Centraliza o prefixo da rota
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody RegistrationRequest request) {
        try {
            Client newClient = clientService.registerNewClient(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar cliente: " + e.getMessage());
        }
    }

    // MÉTODO ATUALIZADO: Recebe o 'id' numérico do cliente como uma variável na URL
    @PostMapping("/{id}/upgrade-subscription")
    public ResponseEntity<?> upgradeSubscription(
            @PathVariable Long id,
            @RequestBody SubscriptionUpgradeRequest request) {
        try {
            // A autenticação é feita pela API Key, e a identificação do cliente vem da URL.
            Subscription updatedSubscription = clientService.upgradeSubscription(id, request);
            return ResponseEntity.ok(updatedSubscription);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // NOVO ENDPOINT PARA PLANO PERSONALIZADO
    @PostMapping("/{id}/personalized-subscription")
    public ResponseEntity<?> createPersonalizedSubscription(@PathVariable Long id, @RequestBody PersonalizedPlanRequest request) {
        try {
            Subscription subscription = clientService.createOrUpdatePersonalizedSubscription(id, request);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
