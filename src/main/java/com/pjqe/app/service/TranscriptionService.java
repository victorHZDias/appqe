package com.pjqe.app.service;

import com.pjqe.app.client.AssemblyAIClient;
import com.pjqe.app.client.TranscriptionRequest;
import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.Subscription;
import com.pjqe.app.entity.TranscriptionAudio;
import com.pjqe.app.exception.InsufficientMinutesException;
import com.pjqe.app.model.dto.RequestPayload;
import com.pjqe.app.repository.ClientRepository;
import com.pjqe.app.repository.SubscriptionRepository;
import com.pjqe.app.repository.TranscriptionAudioRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TranscriptionService {

    private final AssemblyAIClient assemblyAIClient;
    private final ChatModel chatModel;
    private final SubscriptionRepository subscriptionRepository;
    private final TranscriptionAudioRepository transcriptionAudioRepository;
    private final ClientRepository clientRepository;

    public TranscriptionService(AssemblyAIClient assemblyAIClient, ChatModel chatModel,
                                SubscriptionRepository subscriptionRepository,
                                TranscriptionAudioRepository transcriptionAudioRepository,
                                ClientRepository clientRepository) {
        this.assemblyAIClient = assemblyAIClient;
        this.chatModel = chatModel;
        this.subscriptionRepository = subscriptionRepository;
        this.transcriptionAudioRepository = transcriptionAudioRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Map<String, Object> processTranscriptionAndAnalysis(RequestPayload payload, Long clientId) throws IOException, InterruptedException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + clientId + " não encontrado."));

        double audioDuration = getAudioDurationFromUrl(payload.audioUrl());

        Subscription activeSubscription = subscriptionRepository.findFirstByClientOrderByStartDateDesc(client)
                .orElseThrow(() -> new RuntimeException("Nenhuma assinatura ativa encontrada para o cliente."));

        if (activeSubscription.getRemainingMinutes() < audioDuration) {
            throw new InsufficientMinutesException("Saldo de minutos insuficiente para processar o áudio.");
        }

        TranscriptionRequest transcriptionRequest = new TranscriptionRequest();
        transcriptionRequest.setAudioUrl(payload.audioUrl());
        transcriptionRequest.setLanguageCode("pt");
        transcriptionRequest.setSpeakerLabels(true);
        transcriptionRequest.setPunctuate(true);
        transcriptionRequest.setWebhookUrl("https://your-webhook-url/path");
        transcriptionRequest.setSpeakersExpected(2);
        String transcriptionId = assemblyAIClient.submitTranscriptionRequest(transcriptionRequest);
        String transcribedText = assemblyAIClient.getTranscriptionResult(transcriptionId);

        if (transcribedText == null) {
            throw new RuntimeException("Falha ao obter o texto transcrito.");
        }

        String topicListString = String.join(", ", payload.selectedTopics());
        String analysisPrompt = createAnalysisPrompt(topicListString, transcribedText);

        ChatClient chatClient = ChatClient.builder(chatModel).build();
        Map<String, Object> analysisResult = chatClient.prompt()
                .user(analysisPrompt)
                .call()
                .entity(new ParameterizedTypeReference<>() {});

        TranscriptionAudio audioRecord = new TranscriptionAudio();
        audioRecord.setClient(client);
        audioRecord.setUrl(payload.audioUrl());
        audioRecord.setDurationInMinutes(audioDuration);
        audioRecord.setStatus("completed");
        audioRecord.setRequestedAt(LocalDateTime.now());
        audioRecord.setFinishedAt(LocalDateTime.now());
        audioRecord.setTranscribedText(transcribedText);
        audioRecord.setAnalysisResult(analysisResult);
        transcriptionAudioRepository.save(audioRecord);

        activeSubscription.setMinutesUsed(activeSubscription.getMinutesUsed() + audioDuration);
        activeSubscription.setRemainingMinutes(activeSubscription.getRemainingMinutes() - audioDuration);
        subscriptionRepository.save(activeSubscription);

        return analysisResult;
    }

    public List<TranscriptionAudio> getTranscriptionsByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + clientId + " não encontrado."));
        return transcriptionAudioRepository.findByClient(client);
    }

    /**
     * Busca todas as transcrições existentes no sistema.
     * Este é o método que estava faltando.
     */
    public List<TranscriptionAudio> getAllTranscriptions() {
        return transcriptionAudioRepository.findAll();
    }

    private double getAudioDurationFromUrl(String url) {
        return 5.0; // Simulação
    }

    private String createAnalysisPrompt(String topics, String text) {
        return String.format("""
            Analise o seguinte texto e retorne um objeto JSON que contenha apenas as seguintes chaves: %s.
            - 'entidades_nomeadas': Encontre e liste todas as entidades nomeadas (pessoas, lugares, empresas).
            - 'topicos': Identifique os principais temas da conversa.
            - 'frases_chave': Extraia as frases mais importantes.
            - 'redacao_audio': Identifique se há menção a dados sensíveis (PII).
            - 'analise_sentimento': Forneça uma análise do sentimento geral da conversa (positivo, negativo, neutro).
            - 'capitulos_automaticos': Divida a conversa em capítulos com base nas mudanças de assunto.

            Texto para análise: '%s'

            Sua resposta deve ser um JSON válido contendo apenas as chaves %s. Não inclua nenhuma outra informação.
            """, topics, text, topics);
    }
}
