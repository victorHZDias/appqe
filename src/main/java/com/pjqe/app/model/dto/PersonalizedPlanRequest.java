package com.pjqe.app.model.dto;

import java.util.List;

public record PersonalizedPlanRequest(
        List<Long> serviceIds,
        int minutes
) {}
