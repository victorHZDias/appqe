package com.pjqe.app.controller;

import com.pjqe.app.entity.Plan;
import com.pjqe.app.entity.Service;
import com.pjqe.app.entity.Subscription;
import com.pjqe.app.repository.PlanRepository;
import com.pjqe.app.repository.ServiceRepository;
import com.pjqe.app.repository.SubscriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/management")
public class ManagementRestController {

    private final PlanRepository planRepository;
    private final ServiceRepository serviceRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ManagementRestController(PlanRepository planRepository, ServiceRepository serviceRepository, SubscriptionRepository subscriptionRepository) {
        this.planRepository = planRepository;
        this.serviceRepository = serviceRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    // --- Endpoints para Planos ---

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        return ResponseEntity.ok(plans);
    }

    // --- Endpoints para Serviços ---

    @GetMapping("/services")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    // --- Endpoints para Assinaturas ---

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return ResponseEntity.ok(subscriptions);
    }
}