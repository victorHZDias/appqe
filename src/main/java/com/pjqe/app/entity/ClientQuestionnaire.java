package com.pjqe.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ClientQuestionnaire {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "client_id")
    @JsonBackReference // Evita loops na serialização JSON
    private Client client;

    // Campo comum para PF e PJ
    private String areaAtuacao;

    // Campos específicos para Pessoa Física (PF)
    private String intensaoUso;
    private String profissao;

    // Campos específicos para Pessoa Jurídica (PJ)
    private String faturamento;
    @Column(columnDefinition = "TEXT")
    private String dores;
}
