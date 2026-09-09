package com.ecommerce.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class LoggingFiitter implements GlobalFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFiitter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("incoming request to :" + exchange.getRequest().getPath());
        return chain.filter(exchange);
    }
}
