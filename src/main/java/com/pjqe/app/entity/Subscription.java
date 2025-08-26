package com.pjqe.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double minutes;
    private double minutesUsed;
    private double remainingMinutes;

    // NOVO CAMPO para armazenar o preço, especialmente para planos personalizados
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
}
