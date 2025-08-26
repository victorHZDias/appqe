package com.pjqe.app.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TranscriptionRequest {

    @JsonProperty("audio_url")
    private String audioUrl;

    @JsonProperty("auto_chapters")
    private boolean autoChapters;

    @JsonProperty("auto_highlights")
    private boolean autoHighlights;

    @JsonProperty("content_safety")
    private boolean contentSafety;

    @JsonProperty("custom_spelling")
    private List<String> customSpelling;

    private boolean disfluencies;

    @JsonProperty("entity_detection")
    private boolean entityDetection;

    @JsonProperty("filter_profanity")
    private boolean filterProfanity;

    @JsonProperty("format_text")
    private boolean formatText;

    @JsonProperty("iab_categories")
    private boolean iabCategories;

    @JsonProperty("language_code")
    private String languageCode;

    @JsonProperty("language_confidence_threshold")
    private double languageConfidenceThreshold;

    @JsonProperty("language_detection")
    private boolean languageDetection;

    private boolean multichannel;

    private boolean punctuate;

    @JsonProperty("redact_pii")
    private boolean redactPii;

    @JsonProperty("redact_pii_audio")
    private boolean redactPiiAudio;

    @JsonProperty("redact_pii_audio_quality")
    private String redactPiiAudioQuality;

    @JsonProperty("redact_pii_policies")
    private List<String> redactPiiPolicies;

    @JsonProperty("redact_pii_sub")
    private String redactPiiSub;

    @JsonProperty("sentiment_analysis")
    private boolean sentimentAnalysis;

    @JsonProperty("speaker_labels")
    private boolean speakerLabels;

    @JsonProperty("speakers_expected")
    private int speakersExpected;

    @JsonProperty("speech_threshold")
    private double speechThreshold;

    private boolean summarization;

    @JsonProperty("webhook_auth_header_name")
    private String webhookAuthHeaderName;

    @JsonProperty("webhook_auth_header_value")
    private String webhookAuthHeaderValue;

    @JsonProperty("webhook_url")
    private String webhookUrl;

    public TranscriptionRequest() {
    }

    // Getters e Setters para todos os campos
    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public boolean isAutoChapters() {
        return autoChapters;
    }

    public void setAutoChapters(boolean autoChapters) {
        this.autoChapters = autoChapters;
    }

    public boolean isAutoHighlights() {
        return autoHighlights;
    }

    public void setAutoHighlights(boolean autoHighlights) {
        this.autoHighlights = autoHighlights;
    }

    public boolean isContentSafety() {
        return contentSafety;
    }

    public void setContentSafety(boolean contentSafety) {
        this.contentSafety = contentSafety;
    }

    public List<String> getCustomSpelling() {
        return customSpelling;
    }

    public void setCustomSpelling(List<String> customSpelling) {
        this.customSpelling = customSpelling;
    }

    public boolean isDisfluencies() {
        return disfluencies;
    }

    public void setDisfluencies(boolean disfluencies) {
        this.disfluencies = disfluencies;
    }

    public boolean isEntityDetection() {
        return entityDetection;
    }

    public void setEntityDetection(boolean entityDetection) {
        this.entityDetection = entityDetection;
    }

    public boolean isFilterProfanity() {
        return filterProfanity;
    }

    public void setFilterProfanity(boolean filterProfanity) {
        this.filterProfanity = filterProfanity;
    }

    public boolean isFormatText() {
        return formatText;
    }

    public void setFormatText(boolean formatText) {
        this.formatText = formatText;
    }

    public boolean isIabCategories() {
        return iabCategories;
    }

    public void setIabCategories(boolean iabCategories) {
        this.iabCategories = iabCategories;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public double getLanguageConfidenceThreshold() {
        return languageConfidenceThreshold;
    }

    public void setLanguageConfidenceThreshold(double languageConfidenceThreshold) {
        this.languageConfidenceThreshold = languageConfidenceThreshold;
    }

    public boolean isLanguageDetection() {
        return languageDetection;
    }

    public void setLanguageDetection(boolean languageDetection) {
        this.languageDetection = languageDetection;
    }

    public boolean isMultichannel() {
        return multichannel;
    }

    public void setMultichannel(boolean multichannel) {
        this.multichannel = multichannel;
    }

    public boolean isPunctuate() {
        return punctuate;
    }

    public void setPunctuate(boolean punctuate) {
        this.punctuate = punctuate;
    }

    public boolean isRedactPii() {
        return redactPii;
    }

    public void setRedactPii(boolean redactPii) {
        this.redactPii = redactPii;
    }

    public boolean isRedactPiiAudio() {
        return redactPiiAudio;
    }

    public void setRedactPiiAudio(boolean redactPiiAudio) {
        this.redactPiiAudio = redactPiiAudio;
    }

    public String getRedactPiiAudioQuality() {
        return redactPiiAudioQuality;
    }

    public void setRedactPiiAudioQuality(String redactPiiAudioQuality) {
        this.redactPiiAudioQuality = redactPiiAudioQuality;
    }

    public List<String> getRedactPiiPolicies() {
        return redactPiiPolicies;
    }

    public void setRedactPiiPolicies(List<String> redactPiiPolicies) {
        this.redactPiiPolicies = redactPiiPolicies;
    }

    public String getRedactPiiSub() {
        return redactPiiSub;
    }

    public void setRedactPiiSub(String redactPiiSub) {
        this.redactPiiSub = redactPiiSub;
    }

    public boolean isSentimentAnalysis() {
        return sentimentAnalysis;
    }

    public void setSentimentAnalysis(boolean sentimentAnalysis) {
        this.sentimentAnalysis = sentimentAnalysis;
    }

    public boolean isSpeakerLabels() {
        return speakerLabels;
    }

    public void setSpeakerLabels(boolean speakerLabels) {
        this.speakerLabels = speakerLabels;
    }

    public int getSpeakersExpected() {
        return speakersExpected;
    }

    public void setSpeakersExpected(int speakersExpected) {
        this.speakersExpected = speakersExpected;
    }

    public double getSpeechThreshold() {
        return speechThreshold;
    }

    public void setSpeechThreshold(double speechThreshold) {
        this.speechThreshold = speechThreshold;
    }

    public boolean isSummarization() {
        return summarization;
    }

    public void setSummarization(boolean summarization) {
        this.summarization = summarization;
    }

    public String getWebhookAuthHeaderName() {
        return webhookAuthHeaderName;
    }

    public void setWebhookAuthHeaderName(String webhookAuthHeaderName) {
        this.webhookAuthHeaderName = webhookAuthHeaderName;
    }

    public String getWebhookAuthHeaderValue() {
        return webhookAuthHeaderValue;
    }

    public void setWebhookAuthHeaderValue(String webhookAuthHeaderValue) {
        this.webhookAuthHeaderValue = webhookAuthHeaderValue;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}