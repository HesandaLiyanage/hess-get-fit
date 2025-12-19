package com.hess.fitnessactivityservice.dto;

import com.hess.fitnessactivityservice.model.ActivityType.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private String id;
    private String userId;
    private ActivityType type;
    private int duration;
    private int calories;
    private LocalDateTime startTime;
    private Map<String , Object> additionalInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
