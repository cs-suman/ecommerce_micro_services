package com.ecommerce.notification;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    private final EmailService emailService;

    public OrderEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderEvent) {

        System.out.println("Received Order Event: " + orderEvent);

        String subject = "New Order Notification";

        String body = "Hello,\n\n"
                + "A new order event has been received.\n\n"
                + "Order Details: " + orderEvent
                + "\n\nThank you!";

        emailService.sendEmail(
                "pandageek0011@gmail.com",
                subject,
                body
        );

        System.out.println("Email sent successfully");
    }
}
