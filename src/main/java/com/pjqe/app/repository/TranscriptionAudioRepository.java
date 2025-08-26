package com.pjqe.app.repository;

import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.TranscriptionAudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscriptionAudioRepository extends JpaRepository<TranscriptionAudio, Long> {

    /**
     * Encontra todas as transcrições de áudio de um cliente específico.
     * @param client O cliente proprietário das transcrições.
     * @return Uma lista de objetos TranscriptionAudio.
     */
    List<TranscriptionAudio> findByClient(Client client);
}
