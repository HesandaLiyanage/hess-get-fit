package com.hess.fitnessactivityservice.repository;


import com.hess.fitnessactivityservice.model.Activity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ActivityRepository extends ReactiveMongoRepository<Activity,String> {
    Flux<Activity> findByUserId(String userId);
}