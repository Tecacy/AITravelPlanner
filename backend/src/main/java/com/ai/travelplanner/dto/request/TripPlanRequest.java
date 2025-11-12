package com.ai.travelplanner.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TripPlanRequest(
        @NotBlank(message = "目的地不能为空")
        String destination,
        @NotNull(message = "开始日期不能为空")
        LocalDate startDate,
        @NotNull(message = "结束日期不能为空")
        LocalDate endDate,
        @Positive(message = "同行人数需大于0")
        Integer travelers,
        @NotNull(message = "预算不能为空")
        @DecimalMin(value = "0.0", inclusive = false, message = "预算应大于0")
        BigDecimal budget,
        List<String> preferences,
        String userId,
        @Valid
        List<TripDayRequest> dailyPlans
) {
}

