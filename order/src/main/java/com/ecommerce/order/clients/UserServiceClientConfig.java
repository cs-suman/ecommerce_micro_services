package com.ecommerce.order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class UserServiceClientConfig {

    /////NOTe/////
    ///Since Both Below Beans are already declared in "ProductServiceClientConfig"  class so we do not need to declare these here//

//    @Bean
//    @Primary
//    public RestClient.Builder restClientBuilderprimaryForUser() {
//        return RestClient.builder();
//    }

//    @Bean
//    @LoadBalanced
//    public RestClient.Builder restClientBuilderForUser() {
//        return RestClient.builder();
//    }

    @Bean
    public UserServiceClient restClientInterfaceForUser(@LoadBalanced RestClient.Builder restclientBuilder) {

        RestClient restClient = restclientBuilder.baseUrl("http://USER-SERVICE")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,((request, response) -> Optional.empty()))
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();

        UserServiceClient httpInterfaceService = httpServiceProxyFactory.createClient(UserServiceClient.class);

        return httpInterfaceService;


    }
}
