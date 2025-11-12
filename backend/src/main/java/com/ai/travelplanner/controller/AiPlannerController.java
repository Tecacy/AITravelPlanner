package com.ai.travelplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.travelplanner.dto.ApiResponse;
import com.ai.travelplanner.dto.request.AiTripPlanRequest;
import com.ai.travelplanner.dto.response.AiTripPlanResponse;
import com.ai.travelplanner.service.AiPlannerService;

@RestController
@RequestMapping("/api/ai/trip-plans")
@Validated
public class AiPlannerController {

    private final AiPlannerService aiPlannerService;

    public AiPlannerController(AiPlannerService aiPlannerService) {
        this.aiPlannerService = aiPlannerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AiTripPlanResponse>> generatePlan(
            @Validated @RequestBody AiTripPlanRequest request) {
        AiTripPlanResponse response = aiPlannerService.generatePlan(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

