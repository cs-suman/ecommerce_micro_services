package com.ecommerce.product.controller;


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

    @PostMapping

    public ResponseEntity<ProductRepository.ProductResponse> createProduct(@RequestBody ProductRepository.ProductRequest productRequest) {

        return new ResponseEntity<>(productService.createproduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductRepository.ProductRequest productRequest) {

        ProductRepository.ProductResponse response = productService.updateProduct(id, productRequest);
        if (response == null) {
            return new ResponseEntity<>("product is not present", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("product update successfully", HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductRepository.ProductResponse>> getProduct() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
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
    public ResponseEntity<List<ProductRepository.ProductResponse>> searchProduct(@RequestParam String keyword){
         return  productService.searchProduct(keyword);

    }

}
