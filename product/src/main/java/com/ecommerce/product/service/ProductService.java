package com.ecommerce.product.service;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import io.micrometer.observation.ObservationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createproduct(ProductRequest productRequest) {

        Product product = new Product();
        updateProductFromRequest(product, productRequest);

        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);

    }


    private ProductResponse mapToProductResponse(Product savedProduct) {

        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setActive(savedProduct.getActive());
        response.setCategory(savedProduct.getCategory());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setImageurl(savedProduct.getImageurl());
        response.setStockQuantity(savedProduct.getStockQuantity());


        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {


        product.setName(productRequest.getName());

        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageurl(productRequest.getImageurl());
        product.setStockQuantity(productRequest.getStockQuantity());
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {

            return null;
        }

        Product existingProduct = product.get();
        updateProductFromRequest(existingProduct, productRequest);

        Product savedupdateProduct = productRepository.save(existingProduct);

        return mapToProductResponse(savedupdateProduct);


    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findByActiveTrue();
        List<ProductResponse> list = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            ProductResponse temp = mapToProductResponse(productList.get(i));
            list.add(temp);
        }
        return list;
    }

    public Boolean deleteProduct(Long id) {

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            System.out.println("product not found");
            return false;
        }
        Product product1 = product.get();
        product1.setActive(false);// not delete we make the product deactive

        Product save = productRepository.save(product1);
        return true;
    }

    public ResponseEntity<List<ProductResponse>> searchProduct(String keyword) {
        List<Product> productList = productRepository.searchProduct(keyword);
        List<ProductResponse> response = new ArrayList<>();
        for (int i = 0; i <= productList.size(); i++) {

            ProductResponse save = mapToProductResponse(productList.get(i));
            response.add(save);


        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Optional<ProductResponse> getProductById(String id) {
        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                .map(this::mapToProductResponse);
    }
}
