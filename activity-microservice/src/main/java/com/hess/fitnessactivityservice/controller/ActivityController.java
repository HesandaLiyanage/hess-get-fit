package com.hess.fitnessactivityservice.controller;

import com.hess.fitnessactivityservice.dto.ActivityRequest;
import com.hess.fitnessactivityservice.dto.ActivityResponse;
import com.hess.fitnessactivityservice.service.activityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final activityService activityService;

    public ActivityController(activityService activityService){
        this.activityService = activityService;
    }

    @PostMapping()
    public Mono<ResponseEntity<ActivityResponse>> trackActivity(@RequestBody ActivityRequest request) {
        return activityService.trackActivity(request).map(ResponseEntity::ok);
    }

    @GetMapping()
    public Flux<ActivityResponse> getUserActivities(@RequestHeader("X-User-ID") String userId ) {
        return activityService.getUserActivities(userId);
    }

    @GetMapping("/{activityId}")
    public Mono<ResponseEntity<ActivityResponse>> getActivity(@PathVariable String activityId) {
        return activityService.getActivity(activityId).map(ResponseEntity::ok);
    }

}