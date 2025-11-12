package com.ai.travelplanner.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ai.travelplanner.dto.request.ExpenseRequest;
import com.ai.travelplanner.dto.response.ExpenseResponse;
import com.ai.travelplanner.entity.Expense;
import com.ai.travelplanner.entity.TripPlan;
import com.ai.travelplanner.repository.ExpenseRepository;
import com.ai.travelplanner.repository.TripPlanRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripPlanRepository tripPlanRepository;

    public ExpenseService(ExpenseRepository expenseRepository, TripPlanRepository tripPlanRepository) {
        this.expenseRepository = expenseRepository;
        this.tripPlanRepository = tripPlanRepository;
    }

    @Transactional
    public ExpenseResponse addExpense(UUID tripPlanId, ExpenseRequest request) {
        TripPlan plan = tripPlanRepository.findById(tripPlanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "行程不存在"));
        Expense expense = new Expense();
        expense.setTripPlan(plan);
        expense.setCategory(request.category());
        expense.setAmount(request.amount());
        expense.setCurrency(request.currency());
        expense.setNote(request.note());
        expense.setRecordedAt(request.recordedAt() != null ? request.recordedAt() : OffsetDateTime.now());
        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> listExpenses(UUID tripPlanId) {
        return expenseRepository.findByTripPlanId(tripPlanId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteExpense(UUID tripPlanId, UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "费用记录不存在"));
        if (!expense.getTripPlan().getId().equals(tripPlanId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "费用记录不属于当前行程");
        }
        expenseRepository.delete(expense);
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getNote(),
                expense.getRecordedAt()
        );
    }
}

