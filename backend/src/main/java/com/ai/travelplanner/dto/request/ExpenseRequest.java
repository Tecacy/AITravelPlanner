package com.ai.travelplanner.dto.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.ai.travelplanner.entity.ExpenseCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExpenseRequest(
        @NotNull(message = "费用类别不能为空")
        ExpenseCategory category,
        @NotNull(message = "费用金额不能为空")
        @DecimalMin(value = "0.0", inclusive = false, message = "费用金额应大于0")
        BigDecimal amount,
        @NotBlank(message = "货币代码不能为空")
        String currency,
        String note,
        OffsetDateTime recordedAt
) {
}

