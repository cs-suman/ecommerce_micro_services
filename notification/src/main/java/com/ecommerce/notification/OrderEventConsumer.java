package com.ecommerce.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {
    @RabbitListener(queues= "${rabbitmq.queue.name}")
    public void handelOrderEvent(Map<String ,Object> orderEvent){
        System.out.println("REcived Order Event"+ orderEvent);
        
    }
}
