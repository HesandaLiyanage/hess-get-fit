package com.hess.fitnessactivityservice.service;

import com.hess.fitnessactivityservice.dto.ActivityRequest;
import com.hess.fitnessactivityservice.dto.ActivityResponse;
import com.hess.fitnessactivityservice.model.Activity;
import com.hess.fitnessactivityservice.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.management.MonitorInfo;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class activityService {

    private final ActivityRepository activityRepository;
    private final userValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public activityService(ActivityRepository activityRepository , userValidationService userValidationService, RabbitTemplate rabbitTemplate){

        this.activityRepository = activityRepository;
        this.userValidationService = userValidationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<ActivityResponse> trackActivity(ActivityRequest request) {
        Mono<ActivityResponse> savedActivity =
        userValidationService.validateUser(request.getUserId())
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new RuntimeException("User not found"));
                    }

                    Activity activity = Activity.builder()
                            .userId(request.getUserId())
                            .calories(request.getCalories())
                            .duration(request.getDuration())
                            .type(request.getType())
                            .startTime(request.getStartTime())
                            .additionalInfo(request.getAdditionalInfo())
                            .build();

                    return activityRepository.save(activity);  // Returns Mono<Activity>
                })
                .map(this::mapToResponse);



        //publisihing for rabbitmq
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey , savedActivity);

        } catch(Exception e) {
            log.error("failed to publish the acitivyt to the rabbitmq : " , e);
        }

        return savedActivity;
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

    public Flux<ActivityResponse> getUserActivities(String userId) {
        return activityRepository.findByUserId(userId).map(this::mapToResponse);
    }

    public Mono<ActivityResponse> getActivity(String activityId) {
        return activityRepository.findById(activityId).map(this::mapToResponse).switchIfEmpty(Mono.error(new RuntimeException("Activity not found: " + activityId)));
    }
}