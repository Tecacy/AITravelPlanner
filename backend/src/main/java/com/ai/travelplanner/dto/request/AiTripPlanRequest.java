package com.ai.travelplanner.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record AiTripPlanRequest(
        @NotBlank(message = "请提供旅行需求描述")
        String prompt,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal budget,
        Integer travelers,
        List<String> preferences
) {
}

