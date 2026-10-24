package com.ecommerce.order.services;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public Optional<OrderResponse> createOrder(String userId) {
        //validate for cart item
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems==null) {
           return Optional.empty();
        }
        //validate for  usere
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//
//        if (userOptional.isEmpty()) {
//            return Optional.empty();
//        }
//        User user = userOptional.get();
        // calculate totalprice
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (CartItem cartItem : cartItems) {
            BigDecimal price = cartItem.getPrice();
            totalPrice=totalPrice.add(price);
        }

        // create  order
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setOrder(order);

            orderItemList.add(orderItem);
        }
        order.setItem(orderItemList);
        Order saved = orderRepository.save(order);
        // clear the cart
        cartService.cleraCart(userId);
        rabbitTemplate.convertAndSend(exchangeName,routingKey
                , Map.of("orderId",saved.getId()," status","CREATED"));

        return Optional.of(mapToOderResponse(saved));
    }

    private OrderResponse mapToOderResponse(Order order) {

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getOrderStatus());
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for (OrderItem orderItem : order.getItem()) {
            OrderItemDto orderItemDto = new OrderItemDto();

            orderItemDto.setId(orderItem.getId());
            orderItemDto.setProductId(orderItem.getProductId());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setPrice(orderItem.getPrice());
            orderItemDtoList.add(orderItemDto);
        }
        orderResponse.setItems(orderItemDtoList);
        orderResponse.setCreatedAt(order.getCreatedAt());

        return orderResponse;
    }
}
