package com.hess.fitnessactivityservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class userValidationService {
    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId){
        return userServiceWebClient.get().uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class).block();
    }
}
