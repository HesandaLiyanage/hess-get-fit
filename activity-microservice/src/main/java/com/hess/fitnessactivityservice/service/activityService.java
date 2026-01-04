package com.hess.fitnessactivityservice.service;

import com.hess.fitnessactivityservice.dto.ActivityRequest;
import com.hess.fitnessactivityservice.dto.ActivityResponse;
import com.hess.fitnessactivityservice.model.Activity;
import com.hess.fitnessactivityservice.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class activityService {

    private final ActivityRepository activityRepository;
    private final userValidationService userValidationService;

    public activityService(ActivityRepository activityRepository , userValidationService userVavlidationService){

        this.activityRepository = activityRepository;
        this.userValidationService = userVavlidationService;
    }

    public ActivityResponse trackActivity(ActivityRequest request) {
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser)
            throw new RuntimeException("User not found for this id :: "+request.getUserId());
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
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(this::mapToResponse).toList();
    }

    public ActivityResponse getActivity(String activityId) {
        Optional<Activity> gotActivity = activityRepository.findById(activityId);
        return gotActivity.map(this::mapToResponse).orElseThrow(
                () -> new RuntimeException("Activity not found for this id :: "+activityId)
        );
    }
}