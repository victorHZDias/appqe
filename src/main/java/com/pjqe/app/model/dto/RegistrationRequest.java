package com.pjqe.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegistrationRequest(
        // Campos existentes
        String username,
        String password,
        String name,
        String email,
        String phone,
        @JsonProperty("isCompany")
        boolean isCompany,
        String cpf,
        String cnpj,
        String corporateName,
        String responsibleName,
        String responsibleEmail,

        // Novos campos do questionário (podem ser nulos)
        String intensaoUso,
        String profissao,
        String areaAtuacao,
        String faturamento,
        String dores
) {}