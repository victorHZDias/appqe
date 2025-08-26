package com.pjqe.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
// SUBSTITUÍDO @Data por anotações específicas para evitar loops
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TranscriptionAudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    private String url;
    private String status;
    private double durationInMinutes;
    private LocalDateTime requestedAt;
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("client-audio")
    private Client client;

    @ManyToMany
    @JoinTable(
            name = "audio_services",
            joinColumns = @JoinColumn(name = "audio_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> analysisResult;

    @Column(columnDefinition = "TEXT")
    private String transcribedText;
}
