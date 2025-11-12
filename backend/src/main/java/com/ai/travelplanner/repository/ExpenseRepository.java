package com.ai.travelplanner.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.travelplanner.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    List<Expense> findByTripPlanId(UUID tripPlanId);
}

