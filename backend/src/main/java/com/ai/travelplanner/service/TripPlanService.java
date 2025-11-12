package com.ai.travelplanner.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ai.travelplanner.dto.request.TripPlanRequest;
import com.ai.travelplanner.dto.response.TripPlanResponse;
import com.ai.travelplanner.entity.TripPlan;
import com.ai.travelplanner.repository.TripPlanRepository;
import com.ai.travelplanner.service.mapper.TripPlanMapper;

@Service
public class TripPlanService {

    private final TripPlanRepository tripPlanRepository;
    private final TripPlanMapper tripPlanMapper;

    public TripPlanService(TripPlanRepository tripPlanRepository, TripPlanMapper tripPlanMapper) {
        this.tripPlanRepository = tripPlanRepository;
        this.tripPlanMapper = tripPlanMapper;
    }

    @Transactional
    public TripPlanResponse createTripPlan(TripPlanRequest request) {
        TripPlan plan = tripPlanMapper.toEntity(request);
        plan.setUpdatedAt(OffsetDateTime.now());
        TripPlan saved = tripPlanRepository.save(plan);
        return tripPlanMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TripPlanResponse> listTripPlans(String userId) {
        List<TripPlan> plans = userId == null ? tripPlanRepository.findAll() : tripPlanRepository.findByUserId(userId);
        return plans.stream().map(tripPlanMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TripPlanResponse getTripPlan(UUID id) {
        TripPlan plan = tripPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "行程不存在"));
        return tripPlanMapper.toResponse(plan);
    }

    @Transactional
    public TripPlanResponse updateTripPlan(UUID id, TripPlanRequest request) {
        TripPlan plan = tripPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "行程不存在"));
        tripPlanMapper.updateEntity(plan, request);
        plan.setUpdatedAt(OffsetDateTime.now());
        TripPlan saved = tripPlanRepository.save(plan);
        return tripPlanMapper.toResponse(saved);
    }

    @Transactional
    public void deleteTripPlan(UUID id) {
        tripPlanRepository.deleteById(id);
    }
}

