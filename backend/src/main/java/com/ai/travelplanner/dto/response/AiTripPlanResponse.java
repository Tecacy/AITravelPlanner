package com.ai.travelplanner.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AiTripPlanResponse(
        String summary,
        TripPlanSuggestion plan
) {

    public record TripPlanSuggestion(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            Integer travelers,
            BigDecimal budget,
            List<String> preferences,
            List<TripDaySuggestion> dailyPlans
    ) {
    }

    public record TripDaySuggestion(
            LocalDate date,
            String summary,
            List<TripActivitySuggestion> activities
    ) {
    }

    public record TripActivitySuggestion(
            String time,
            String title,
            String location,
            String description,
            BigDecimal estimatedCost
    ) {
    }
}

