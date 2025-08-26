package com.pjqe.app.repository;

import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Encontra a assinatura mais recente de um cliente específico, ordenada pela data de início.
     * Isso é útil para obter a assinatura ativa atual do cliente.
     *
     * @param client O cliente para o qual a assinatura será buscada.
     * @return um Optional contendo a assinatura mais recente, ou vazio se nenhuma for encontrada.
     */
    Optional<Subscription> findFirstByClientOrderByStartDateDesc(Client client);

    /**
     * Encontra todas as assinaturas associadas a um cliente específico.
     *
     * @param client O cliente proprietário das assinaturas.
     * @return Uma lista de objetos Subscription.
     */
    List<Subscription> findByClient(Client client);
}
