package com.ai.travelplanner.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TripDayRequest(
        @NotNull(message = "行程日期不能为空")
        LocalDate date,
        String summary,
        @Valid
        List<TripActivityRequest> activities
) {
}

