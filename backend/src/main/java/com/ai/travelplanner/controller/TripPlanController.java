package com.ai.travelplanner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.travelplanner.dto.ApiResponse;
import com.ai.travelplanner.dto.request.TripPlanRequest;
import com.ai.travelplanner.dto.response.TripPlanResponse;
import com.ai.travelplanner.service.TripPlanService;

@RestController
@RequestMapping("/api/trips")
public class TripPlanController {

    private final TripPlanService tripPlanService;

    public TripPlanController(TripPlanService tripPlanService) {
        this.tripPlanService = tripPlanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TripPlanResponse>> createTrip(@Validated @RequestBody TripPlanRequest request) {
        TripPlanResponse response = tripPlanService.createTripPlan(request);
        return ResponseEntity.ok(ApiResponse.success(response, "行程创建成功"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TripPlanResponse>>> listTrips(
            @RequestParam(required = false) String userId) {
        List<TripPlanResponse> responses = tripPlanService.listTripPlans(userId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripPlanResponse>> getTrip(@PathVariable UUID id) {
        TripPlanResponse response = tripPlanService.getTripPlan(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripPlanResponse>> updateTrip(
            @PathVariable UUID id,
            @Validated @RequestBody TripPlanRequest request) {
        TripPlanResponse response = tripPlanService.updateTripPlan(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "行程更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(@PathVariable UUID id) {
        tripPlanService.deleteTripPlan(id);
        return ResponseEntity.ok(ApiResponse.successMessage("行程已删除"));
    }
}

