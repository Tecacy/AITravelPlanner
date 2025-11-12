package com.ai.travelplanner.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ai.travelplanner.dto.request.AiTripPlanRequest;
import com.ai.travelplanner.dto.response.AiTripPlanResponse;
import com.ai.travelplanner.dto.response.AiTripPlanResponse.TripActivitySuggestion;
import com.ai.travelplanner.dto.response.AiTripPlanResponse.TripDaySuggestion;
import com.ai.travelplanner.dto.response.AiTripPlanResponse.TripPlanSuggestion;
import com.ai.travelplanner.service.AiPlannerService;

@Service
public class MockAiPlannerService implements AiPlannerService {

    @Override
    public AiTripPlanResponse generatePlan(AiTripPlanRequest request) {
        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now().plusDays(7);
        LocalDate endDate = request.endDate() != null ? request.endDate() : startDate.plusDays(4);
        int days = (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
        if (days <= 0) {
            endDate = startDate;
            days = 1;
        }

        BigDecimal budget = request.budget() != null ? request.budget() : BigDecimal.valueOf(8000);
        int travelers = request.travelers() != null ? request.travelers() : 2;
        String destination = StringUtils.hasText(request.destination()) ? request.destination() : "自选目的地";
        List<String> preferences = CollectionUtils.isEmpty(request.preferences())
                ? List.of("美食", "文化体验")
                : request.preferences();

        List<TripDaySuggestion> daySuggestions = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            String summary = "%s Day %d：探索当地亮点".formatted(destination, i + 1);
            List<TripActivitySuggestion> activities = List.of(
                    new TripActivitySuggestion("09:30", "晨间探索", "%s地标景点".formatted(destination), "体验当地文化与历史", BigDecimal.valueOf(200)),
                    new TripActivitySuggestion("13:00", "在地美食午餐", "%s特色餐厅".formatted(destination), "品尝当地招牌菜", BigDecimal.valueOf(150)),
                    new TripActivitySuggestion("16:00", "亲子互动/休闲时光", "%s热门活动中心".formatted(destination), "适合全家参与的体验", BigDecimal.valueOf(180))
            );
            daySuggestions.add(new TripDaySuggestion(date, summary, activities));
        }

        String summary = """
                这是一个为期 %d 天的 %s 行程，预计预算约 %.2f 元，适合 %d 人出行。根据偏好 %s，
                推荐以轻松探索与美食体验为主，每日安排包含景点参观、当地美食与家庭娱乐活动。
                """.formatted(days, destination, budget.doubleValue(), travelers, String.join("、", preferences)).trim();

        TripPlanSuggestion planSuggestion = new TripPlanSuggestion(
                destination,
                startDate,
                endDate,
                travelers,
                budget,
                preferences,
                daySuggestions
        );

        return new AiTripPlanResponse(summary, planSuggestion);
    }
}

