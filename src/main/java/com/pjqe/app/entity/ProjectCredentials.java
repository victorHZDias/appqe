package com.pjqe.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProjectCredentials {

    @Id
    @Column(name = "credential_key")
    private String key; // Ex: "assemblyai_api_key"

    @Column(name = "credential_value", length = 1024)
    private String value; // O valor da chave/token
}
