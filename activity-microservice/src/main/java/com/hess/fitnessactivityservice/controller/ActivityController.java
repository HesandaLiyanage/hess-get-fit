package com.hess.fitnessactivityservice.controller;

import com.hess.fitnessactivityservice.dto.ActivityRequest;
import com.hess.fitnessactivityservice.dto.ActivityResponse;
import com.hess.fitnessactivityservice.service.activityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final activityService activityService;

    public ActivityController(activityService activityService){
        this.activityService = activityService;
    }

    @PostMapping()
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    @GetMapping()
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-ID") String userId ) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

}