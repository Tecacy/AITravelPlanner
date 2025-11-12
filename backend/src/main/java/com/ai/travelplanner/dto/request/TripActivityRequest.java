package com.ai.travelplanner.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public record TripActivityRequest(
        @NotBlank(message = "活动标题不能为空")
        String title,
        String time,
        String location,
        String description,
        BigDecimal estimatedCost
) {
}

