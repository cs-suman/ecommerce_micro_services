package com.ecommerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private  Integer Quantity;
    private BigDecimal price;


}
