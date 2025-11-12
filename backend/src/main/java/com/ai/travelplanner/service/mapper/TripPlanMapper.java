package com.ai.travelplanner.service.mapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ai.travelplanner.dto.request.TripActivityRequest;
import com.ai.travelplanner.dto.request.TripDayRequest;
import com.ai.travelplanner.dto.request.TripPlanRequest;
import com.ai.travelplanner.dto.response.TripActivityResponse;
import com.ai.travelplanner.dto.response.TripDayResponse;
import com.ai.travelplanner.dto.response.TripPlanResponse;
import com.ai.travelplanner.entity.TripActivity;
import com.ai.travelplanner.entity.TripDay;
import com.ai.travelplanner.entity.TripPlan;

@Component
public class TripPlanMapper {

    public TripPlan toEntity(TripPlanRequest request) {
        TripPlan plan = new TripPlan();
        updateEntity(plan, request);
        plan.setCreatedAt(OffsetDateTime.now());
        return plan;
    }

    public void updateEntity(TripPlan plan, TripPlanRequest request) {
        plan.setDestination(request.destination());
        plan.setStartDate(request.startDate());
        plan.setEndDate(request.endDate());
        plan.setTravelers(request.travelers());
        plan.setBudget(request.budget());
        plan.setPreferences(new ArrayList<>(request.preferences() == null ? List.of() : request.preferences()));
        plan.setUserId(request.userId());

        plan.getDailyPlans().forEach(day -> day.setTripPlan(null));
        plan.getDailyPlans().clear();
        if (request.dailyPlans() != null) {
            request.dailyPlans().forEach(dayRequest -> plan.addDay(toTripDay(dayRequest)));
        }
    }

    public TripPlanResponse toResponse(TripPlan plan) {
        List<String> preferences = plan.getPreferences() == null
                ? List.of()
                : List.copyOf(plan.getPreferences());
        return new TripPlanResponse(
                plan.getId(),
                plan.getDestination(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getTravelers(),
                plan.getBudget(),
                preferences,
                plan.getUserId(),
                plan.getCreatedAt(),
                plan.getUpdatedAt(),
                plan.getDailyPlans().stream()
                        .map(this::toTripDayResponse)
                        .toList()
        );
    }

    private TripDay toTripDay(TripDayRequest request) {
        TripDay day = new TripDay();
        day.setDate(request.date());
        day.setSummary(request.summary());
        if (request.activities() != null) {
            request.activities().forEach(activityRequest -> day.addActivity(toTripActivity(activityRequest)));
        }
        return day;
    }

    private TripActivity toTripActivity(TripActivityRequest request) {
        TripActivity activity = new TripActivity();
        activity.setTitle(request.title());
        activity.setTime(request.time());
        activity.setLocation(request.location());
        activity.setDescription(request.description());
        activity.setEstimatedCost(request.estimatedCost());
        return activity;
    }

    private TripDayResponse toTripDayResponse(TripDay day) {
        return new TripDayResponse(
                day.getId(),
                day.getDate(),
                day.getSummary(),
                day.getActivities().stream()
                        .map(this::toTripActivityResponse)
                        .toList()
        );
    }

    private TripActivityResponse toTripActivityResponse(TripActivity activity) {
        return new TripActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getTime(),
                activity.getLocation(),
                activity.getDescription(),
                activity.getEstimatedCost()
        );
    }
}

