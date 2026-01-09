package com.hess.fitnessactivityservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    /* activity q added. durable is true so even if rabbitmq restarts , queue is
    gonna be there */
    @Bean
    public Queue activityQueue() {
        return new Queue("activity.queue" , true);
    }

    //since we are passing java objects to another service , we have to serialize objects first.

    @Bean
    public MessageConverter jsonMessageConvertor() {
        return new JacksonJsonMessageConverter();
    }
}
