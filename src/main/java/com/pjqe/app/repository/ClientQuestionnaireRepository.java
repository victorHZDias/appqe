package com.pjqe.app.repository;

import com.pjqe.app.entity.ClientQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientQuestionnaireRepository extends JpaRepository<ClientQuestionnaire, Long> {
}