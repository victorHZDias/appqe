package com.pjqe.app.controller;

import com.pjqe.app.entity.Plan;
import com.pjqe.app.entity.ProjectCredentials;
import com.pjqe.app.entity.Service;
import com.pjqe.app.entity.SystemUser;
import com.pjqe.app.repository.*;
import com.pjqe.app.service.CredentialsService;
import com.pjqe.app.service.SystemUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ClientRepository clientRepository;
    private final PlanRepository planRepository;
    private final ServiceRepository serviceRepository;
    private final SystemUserService systemUserService;
    private final CredentialsRepository credentialsRepository;
    private final SystemUserRepository systemUserRepository;
    private final CredentialsService credentialsService;

    public AdminController(ClientRepository clientRepository, PlanRepository planRepository,
                           ServiceRepository serviceRepository, SystemUserService systemUserService,
                           CredentialsRepository credentialsRepository, SystemUserRepository systemUserRepository,
                           CredentialsService credentialsService) {
        this.clientRepository = clientRepository;
        this.planRepository = planRepository;
        this.serviceRepository = serviceRepository;
        this.systemUserService = systemUserService;
        this.credentialsRepository = credentialsRepository;
        this.systemUserRepository = systemUserRepository;
        this.credentialsService = credentialsService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("plans", planRepository.findAll());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("credentials", credentialsRepository.findAll());
        model.addAttribute("systemUsers", systemUserRepository.findAll());
        return "admin";
    }

    // --- Métodos de Gestão de Usuários, Planos e Serviços ---
    @PostMapping("/add-system-user")
    public String addSystemUser(@ModelAttribute SystemUser systemUser) {
        systemUserService.registerSystemUser(systemUser.getUsername(), systemUser.getPassword(), systemUser.getRole());
        return "redirect:/admin";
    }

    @PostMapping("/delete-system-user/{id}")
    public String deleteSystemUser(@PathVariable Long id) {
        systemUserRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit-system-user/{id}")
    public String showEditSystemUserForm(@PathVariable Long id, Model model) {
        SystemUser user = systemUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuário inválido:" + id));
        model.addAttribute("systemUser", user);
        return "edit-system-user";
    }

    @PostMapping("/update-system-user/{id}")
    public String updateSystemUser(@PathVariable Long id, @ModelAttribute("systemUser") SystemUser user) {
        systemUserService.updateSystemUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/add-plan")
    public String addPlan(@ModelAttribute Plan plan, @RequestParam(value = "services", required = false) List<Long> serviceIds) {
        if (serviceIds != null && !serviceIds.isEmpty()) {
            List<Service> selectedServices = serviceRepository.findAllById(serviceIds);
            plan.setServices(new HashSet<>(selectedServices));
        }
        planRepository.save(plan);
        return "redirect:/admin";
    }

    @PostMapping("/add-service")
    public String addService(@ModelAttribute Service service) {
        serviceRepository.save(service);
        return "redirect:/admin";
    }

    @PostMapping("/delete-service/{id}")
    public String deleteService(@PathVariable Long id) {
        serviceRepository.deleteById(id);
        return "redirect:/admin";
    }

    // --- Métodos de Gestão de Credenciais ---
    @PostMapping("/credentials/add")
    public String addCredential(@RequestParam String key, @RequestParam String value, RedirectAttributes redirectAttributes) {
        try {
            ProjectCredentials credential = new ProjectCredentials();
            credential.setKey(key);
            credential.setValue(value);
            credentialsService.saveNewCredential(credential);
            redirectAttributes.addFlashAttribute("successMessage", "Credencial adicionada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao adicionar credencial: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/credentials/update")
    public String updateCredentials(@RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
        try {
            credentialsService.updateCredentials(allParams);
            redirectAttributes.addFlashAttribute("successMessage", "Credenciais atualizadas com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar credenciais: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
