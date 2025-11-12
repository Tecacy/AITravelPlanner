package com.ai.travelplanner.service;

import com.ai.travelplanner.dto.request.AiTripPlanRequest;
import com.ai.travelplanner.dto.response.AiTripPlanResponse;

public interface AiPlannerService {

    AiTripPlanResponse generatePlan(AiTripPlanRequest request);
}

