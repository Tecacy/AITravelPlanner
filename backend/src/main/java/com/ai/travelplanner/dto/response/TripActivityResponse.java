package com.ai.travelplanner.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TripActivityResponse(
        UUID id,
        String title,
        String time,
        String location,
        String description,
        BigDecimal estimatedCost
) {
}

