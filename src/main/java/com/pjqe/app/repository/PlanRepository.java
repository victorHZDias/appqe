package com.pjqe.app.repository;

import com.pjqe.app.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    /**
     * Encontra um plano pelo seu nome.
     * @param name O nome do plano.
     * @return um Optional contendo o plano, ou vazio se não for encontrado.
     */
    Optional<Plan> findByName(String name);
}
