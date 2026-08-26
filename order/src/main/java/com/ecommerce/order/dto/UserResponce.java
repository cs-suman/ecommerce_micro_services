package com.ecommerce.order.dto;

import com.ecommerce.order.model.UserRole;
import lombok.Data;

@Data
public class UserResponce {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole Role;
    private AddressDTO address;
}
