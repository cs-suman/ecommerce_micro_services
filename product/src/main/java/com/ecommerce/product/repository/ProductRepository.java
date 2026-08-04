package com.ecommerce.product.repository;



import com.ecommerce.product.model.Product;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {

    List<Product> findByActiveTrue();
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stockQuantity > 0 AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProduct(@Param("keyword") String keyword);

    @Data
    class ProductRequest {

        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private String category;
        private String imageurl;

    }

    @Data
    class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private  Integer stockQuantity;
        private String category;
        private String imageurl;
        private Boolean active;
    }
}
