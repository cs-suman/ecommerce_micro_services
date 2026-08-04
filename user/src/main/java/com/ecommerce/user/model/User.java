package com.ecommerce.user.model;

//import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
//@Entity(name="user_table")
@Document(collection = "users")//# table create in mongo db
//@AllArgsConstructor

//@NoArgsConstructor// no argument constructor--
public class User {
    @Id

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;// in mongodb by default String no int long concept in there
    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;
    private String phone;
    private UserRole userRole = UserRole.CUSTOMER;
    //    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="adress_id",referencedColumnName = "id")
    private Address address;

    //    @CreationTimestamp
    @CreatedDate
    private LocalDateTime createAt;

    //    @UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime updateAt;


}
