package com.ai.travelplanner.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.travelplanner.entity.TripPlan;

public interface TripPlanRepository extends JpaRepository<TripPlan, UUID> {

    List<TripPlan> findByUserId(String userId);
}

