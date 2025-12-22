package com.hess.fitnessactivityservice.service;

import com.hess.fitnessactivityservice.dto.ActivityRequest;
import com.hess.fitnessactivityservice.dto.ActivityResponse;
import com.hess.fitnessactivityservice.model.Activity;
import com.hess.fitnessactivityservice.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class activityService {

    private final ActivityRepository activityRepository;

    public activityService(ActivityRepository activityRepository){
        this.activityRepository = activityRepository;
    }

    public ActivityResponse trackActivity(ActivityRequest request) {
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .calories(request.getCalories())
                .duration(request.getDuration())
                .type(request.getType())
                .startTime(request.getStartTime())
                .additionalInfo(request.getAdditionalInfo())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);

    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setCalories(activity.getCalories());
        response.setDuration(activity.getDuration());
        response.setType(activity.getType());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalInfo(activity.getAdditionalInfo());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activites = activityRepository.findByUserId(userId);
        return activites.stream().map(this::mapToResponse).toList();
    }
}