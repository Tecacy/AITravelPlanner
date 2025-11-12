package com.ai.travelplanner.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.ai.travelplanner.entity.ExpenseCategory;

public record ExpenseResponse(
        UUID id,
        ExpenseCategory category,
        BigDecimal amount,
        String currency,
        String note,
        OffsetDateTime recordedAt
) {
}

