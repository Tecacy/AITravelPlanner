package com.ai.travelplanner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.travelplanner.dto.ApiResponse;
import com.ai.travelplanner.dto.request.ExpenseRequest;
import com.ai.travelplanner.dto.response.ExpenseResponse;
import com.ai.travelplanner.service.ExpenseService;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> addExpense(
            @PathVariable UUID tripId,
            @Validated @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.addExpense(tripId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "费用记录创建成功"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> listExpenses(@PathVariable UUID tripId) {
        List<ExpenseResponse> responses = expenseService.listExpenses(tripId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @PathVariable UUID tripId,
            @PathVariable UUID expenseId) {
        expenseService.deleteExpense(tripId, expenseId);
        return ResponseEntity.ok(ApiResponse.successMessage("费用记录已删除"));
    }
}

