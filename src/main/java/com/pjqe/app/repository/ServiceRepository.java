package com.pjqe.app.repository;

import com.pjqe.app.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// A correção principal está aqui: JpaRepository<Service, Long>
// Isso informa ao Spring que a chave primária da entidade Service é do tipo Long.
public interface ServiceRepository extends JpaRepository<Service, Long> {
}