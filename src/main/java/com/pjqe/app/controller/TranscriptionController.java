package com.pjqe.app.controller;

import com.pjqe.app.entity.TranscriptionAudio;
import com.pjqe.app.exception.InsufficientMinutesException;
import com.pjqe.app.model.dto.RequestPayload;
import com.pjqe.app.service.TranscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    public TranscriptionController(TranscriptionService transcriptionService) {
        this.transcriptionService = transcriptionService;
    }

    @PostMapping("/clients/{clientId}/analyze")
    public ResponseEntity<?> analyzeAudio(
            @PathVariable Long clientId,
            @RequestBody RequestPayload payload) {
        try {
            Map<String, Object> result = transcriptionService.processTranscriptionAndAnalysis(payload, clientId);
            return ResponseEntity.ok(result);
        } catch (InsufficientMinutesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ocorreu um erro interno: " + e.getMessage()));
        }
    }

    // NOVO MÉTODO: Para listar todas as transcrições de um cliente
    @GetMapping("/clients/{clientId}/transcriptions")
    public ResponseEntity<?> getClientTranscriptions(@PathVariable Long clientId) {
        try {
            List<TranscriptionAudio> transcriptions = transcriptionService.getTranscriptionsByClientId(clientId);
            return ResponseEntity.ok(transcriptions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
