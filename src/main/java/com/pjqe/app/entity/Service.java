package com.pjqe.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_seq")
    @SequenceGenerator(name = "service_seq", sequenceName = "service_id_seq", allocationSize = 1)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal costPerMinute;

    @ManyToMany(mappedBy = "services")
    @JsonBackReference // <-- ESTA É A ANOTAÇÃO QUE VOCÊ PRECISA ADICIONAR
    private Set<Plan> plans = new HashSet<>();

    public Service(String description, BigDecimal costPerMinute) {
        this.description = description;
        this.costPerMinute = costPerMinute;
    }

    @PreRemove
    private void removeAssociations() {
        for (Plan plan : this.plans) {
            plan.getServices().remove(this);
        }
    }
}
