package com.ecommerce.order.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestRabbitMqBusController {
    @Value("${app.test}")
    private String testMessage;

@GetMapping("/gettermessage")
@RateLimiter(name=" ratelimiterBreaker", fallbackMethod ="fallbackRetry")
    public String getMessage(){
    return testMessage;


    }
    public String fallbackRetry(Exception e){
    return "Hello fallback";
    }
}
