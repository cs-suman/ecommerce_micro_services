package com.ecommerce.product.controller;

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
    public String getMessage(){
    return testMessage;


    }
}
