package com.ecommerce.order.dto;

import lombok.Data;

@Data
public class AddressDTO {

    private String Street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
