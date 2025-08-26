package com.pjqe.app.model.dto;

import java.util.List;

public record RequestPayload(String audioUrl, List<String> selectedTopics) {
}