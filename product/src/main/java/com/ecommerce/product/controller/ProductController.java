package com.ecommerce.product.controller;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    @GetMapping("/simulatrfailure")
    public ResponseEntity<String> simulateFailure(@RequestParam (defaultValue = "false") boolean fail) {
        if (fail) {
            throw  new RuntimeException(" Similated failure for testing");
        }
        return  ResponseEntity.ok("produce service is ok");
    }

    @PostMapping

    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {

        return new ResponseEntity<>(productService.createproduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {

        ProductResponse response = productService.updateProduct(id, productRequest);
        if (response == null) {
            return new ResponseEntity<>("product is not present", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("product update successfully", HttpStatus.OK);
        }
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProduct() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        return productService.getProductById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Boolean delete = productService.deleteProduct(id);
        if (!delete) {

            return ResponseEntity.notFound().build();//  return new ResponseEntity<>(HttpStatus.NO_FOUND);=both are same
        }

        return ResponseEntity.noContent().build(); //return new ResponseEntity<>(HttpStatus.NO_CONTENT);=both are same
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword){
         return  productService.searchProduct(keyword);

    }

}
