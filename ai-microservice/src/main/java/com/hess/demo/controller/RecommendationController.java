package com.hess.demo.controller;

import com.hess.demo.model.Recommendation;
import com.hess.demo.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendation (@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation (@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }
}
