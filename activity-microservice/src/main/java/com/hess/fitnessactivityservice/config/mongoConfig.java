package com.hess.fitnessactivityservice.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class mongoConfig {
}

//what is mongo auditing?
//you need it automatically fill some of the fields.