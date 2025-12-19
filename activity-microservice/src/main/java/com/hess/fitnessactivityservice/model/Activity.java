package com.hess.fitnessactivityservice.model;

import com.hess.fitnessactivityservice.model.ActivityType.ActivityType;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;


@Document(collection = "activities")
@Data
@Builder
@AllArgsConstructor
public class Activity {
    @Id
    private String id;
    private String name;
    private String userId;
    private ActivityType type;
    private int duration;
    private int calories;
    private LocalDateTime startTime;

    @Field("metrics")
    private Map<String , Object> additionalInfo;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}