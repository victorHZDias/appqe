package com.pjqe.app.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TranscriptionResult {

    private String id;
    private String status;
    private String text;

    @JsonProperty("language_model")
    private String languageModel;

    @JsonProperty("language_code")
    private String languageCode;

    @JsonProperty("audio_url")
    private String audioUrl;

    private List<Word> words;
    private List<Utterance> utterances;

    // Getters e Setters para todos os campos
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getLanguageModel() { return languageModel; }
    public void setLanguageModel(String languageModel) { this.languageModel = languageModel; }

    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public List<Word> getWords() { return words; }
    public void setWords(List<Word> words) { this.words = words; }

    public List<Utterance> getUtterances() { return utterances; }
    public void setUtterances(List<Utterance> utterances) { this.utterances = utterances; }

    // Classes aninhadas corrigidas
    public static class Word {
        private String text;
        private int start;
        private int end;
        private double confidence;
        private String speaker;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public int getStart() { return start; }
        public void setStart(int start) { this.start = start; }
        public int getEnd() { return end; }
        public void setEnd(int end) { this.end = end; }
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        public String getSpeaker() { return speaker; }
        public void setSpeaker(String speaker) { this.speaker = speaker; }
    }

    public static class Utterance {
        private String speaker;
        private String text;
        private double confidence;
        private int start;
        private int end;
        private List<Word> words; // Adicionamos a lista de palavras aqui

        public String getSpeaker() { return speaker; }
        public void setSpeaker(String speaker) { this.speaker = speaker; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        public int getStart() { return start; }
        public void setStart(int start) { this.start = start; }
        public int getEnd() { return end; }
        public void setEnd(int end) { this.end = end; }
        public List<Word> getWords() { return words; }
        public void setWords(List<Word> words) { this.words = words; }
    }
}