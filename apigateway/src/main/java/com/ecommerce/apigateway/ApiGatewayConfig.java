package com.ecommerce.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {
   @Bean
    public RouteLocator CoustomRouterLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("product-service", r -> r.path("/api/products/**")
//                        .filters(f->f.rewritePath("/products(?<segment>/?.*)","/api/products${segment}"))
                        .filters(f->f.circuitBreaker(config -> config.setName("ecomBreaker")
                                .setFallbackUri("forward:/fallback/products")))

                        .uri("lb://PRODUCT-SERVICE"))
                .route("user-service", r -> r.path("/api/users/**")
//                        .filters(f->f.rewritePath("/users(?<segment>/?.*)","/api/users${segment}"))
                        .uri("lb://USER-SERVICE"))
                .route("order-service", r -> r.path("/api/orders/**", "/api/cart/**")
//                        .filters(f->f.rewritePath("/(?<segment>.*)","/api/${segment}"))
                        .uri("lb://ORDER-SERVICE"))
                .route("eureka-service", r -> r.path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/"))
                        .uri("http://localhost:8761/"))
                .route("eureka-service-static", r -> r.path("/eureka/**").uri("http://localhost:8761/"))
                .build();
    }

}
