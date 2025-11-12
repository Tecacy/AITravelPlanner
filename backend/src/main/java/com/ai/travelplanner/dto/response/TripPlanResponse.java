package com.ai.travelplanner.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TripPlanResponse(
        UUID id,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        Integer travelers,
        BigDecimal budget,
        List<String> preferences,
        String userId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<TripDayResponse> dailyPlans
) {
}

