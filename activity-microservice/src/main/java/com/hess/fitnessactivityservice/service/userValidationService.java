package com.hess.fitnessactivityservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class userValidationService {
    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId){
        try {
            return Boolean.TRUE.equals(userServiceWebClient.get().uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class).block());

        } catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new RuntimeException("User not found for this id :: "+userId);
            else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                throw new RuntimeException("Invalid Request");
            throw new RuntimeException(
                    "User validation failed with status: " + e.getStatusCode(), e
            );
        }
    }
}
