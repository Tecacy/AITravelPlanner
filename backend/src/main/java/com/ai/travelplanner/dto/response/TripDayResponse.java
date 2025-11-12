package com.ai.travelplanner.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TripDayResponse(
        UUID id,
        LocalDate date,
        String summary,
        List<TripActivityResponse> activities
) {
}

