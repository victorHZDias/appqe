package com.pjqe.app.service;

import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.ClientQuestionnaire;
import com.pjqe.app.entity.Plan;
import com.pjqe.app.entity.Subscription;
import com.pjqe.app.model.dto.PersonalizedPlanRequest;
import com.pjqe.app.model.dto.RegistrationRequest;
import com.pjqe.app.model.dto.SubscriptionUpgradeRequest;
import com.pjqe.app.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientQuestionnaireRepository questionnaireRepository; // Adicionado
    private final ServiceRepository serviceRepository;

    public ClientService(ClientRepository clientRepository, SubscriptionRepository subscriptionRepository,
                         PlanRepository planRepository, PasswordEncoder passwordEncoder,
                         ClientQuestionnaireRepository questionnaireRepository, ServiceRepository serviceRepository) { // Adicionado
        this.clientRepository = clientRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.passwordEncoder = passwordEncoder;
        this.questionnaireRepository = questionnaireRepository; // Adicionado
        this.serviceRepository = serviceRepository; // Adicionado
    }

    @Transactional
    public Client registerNewClient(RegistrationRequest request) {
        Client newClient = new Client();
        newClient.setUsername(request.username());
        newClient.setPassword(passwordEncoder.encode(request.password()));
        newClient.setName(request.name());
        newClient.setEmail(request.email());
        newClient.setPhone(request.phone());
        newClient.setCompany(request.isCompany());
        newClient.setCpf(request.cpf());
        newClient.setCnpj(request.cnpj());
        newClient.setCorporateName(request.corporateName());
        newClient.setResponsibleName(request.responsibleName());
        newClient.setResponsibleEmail(request.responsibleEmail());
        newClient.setCreatedat(LocalDateTime.now());

        Client savedClient = clientRepository.save(newClient);
        // 2. Cria e salva as respostas do questionário
        ClientQuestionnaire questionnaire = new ClientQuestionnaire();
        questionnaire.setClient(savedClient); // Vincula ao cliente
        questionnaire.setAreaAtuacao(request.areaAtuacao());
        if (request.isCompany()) { // Se for PJ
            questionnaire.setFaturamento(request.faturamento());
            questionnaire.setDores(request.dores());
        } else { // Se for PF
            questionnaire.setIntensaoUso(request.intensaoUso());
            questionnaire.setProfissao(request.profissao());
        }
        questionnaireRepository.save(questionnaire); // Salva as respostas
        Subscription freeSubscription = new Subscription();
        freeSubscription.setClient(savedClient);

        Plan freePlan = planRepository.findByName("free_plan")
                .orElseThrow(() -> new RuntimeException("Plano de teste gratuito não encontrado."));
        freeSubscription.setPlan(freePlan);

        freeSubscription.setMinutes(60);
        freeSubscription.setRemainingMinutes(60);
        freeSubscription.setStatus("free_trial");
        freeSubscription.setStartDate(LocalDateTime.now());
        subscriptionRepository.save(freeSubscription);

        return savedClient;
    }

    @Transactional
    public Subscription upgradeSubscription(Long clientId, SubscriptionUpgradeRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Subscription currentSubscription = subscriptionRepository.findFirstByClientOrderByStartDateDesc(client)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada"));

        Plan newPlan = planRepository.findById(request.planId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        currentSubscription.setPlan(newPlan);
        currentSubscription.setMinutes(newPlan.getMinutes());
        currentSubscription.setRemainingMinutes(newPlan.getMinutes());
        currentSubscription.setMinutesUsed(0);
        currentSubscription.setStatus("active");
        currentSubscription.setStartDate(LocalDateTime.now());

        return subscriptionRepository.save(currentSubscription);
    }

    @Transactional
    public Subscription createOrUpdatePersonalizedSubscription(Long clientId, PersonalizedPlanRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Plan personalPlan = planRepository.findByName("personal_plan")
                .orElseThrow(() -> new RuntimeException("Plano 'personal_plan' não encontrado no banco de dados."));

        // CORRIGIDO: Usando o nome completo da sua entidade Service
        List<com.pjqe.app.entity.Service> selectedServices = serviceRepository.findAllById(request.serviceIds());
        if (selectedServices.isEmpty()) {
            throw new IllegalArgumentException("Pelo menos um serviço deve ser selecionado.");
        }

        // LÓGICA DE CÁLCULO DE PREÇO ADICIONADA
        BigDecimal totalPrice = BigDecimal.ZERO;
        // CORRIGIDO: Usando o nome completo da sua entidade Service
        for (com.pjqe.app.entity.Service service : selectedServices) {
            // Custo do serviço = (custo por minuto do serviço) * (quantidade de minutos)
            BigDecimal serviceCost = service.getCostPerMinute().multiply(new BigDecimal(request.minutes()));
            totalPrice = totalPrice.add(serviceCost);
        }

        Subscription subscription = subscriptionRepository.findFirstByClientOrderByStartDateDesc(client)
                .orElse(new Subscription());

        subscription.setClient(client);
        subscription.setPlan(personalPlan);
        subscription.setMinutes(request.minutes());
        subscription.setRemainingMinutes(request.minutes());
        subscription.setMinutesUsed(0);
        subscription.setStatus("active_personalized");
        subscription.setStartDate(LocalDateTime.now());
        subscription.setPrice(totalPrice); // Armazena o preço calculado

        return subscriptionRepository.save(subscription);
    }
}
