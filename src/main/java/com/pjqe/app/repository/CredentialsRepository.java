package com.pjqe.app.repository;

import com.pjqe.app.entity.ProjectCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends JpaRepository<ProjectCredentials, String> {
}
