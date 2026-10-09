package com.ecommerce.order.services;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponce;
import com.ecommerce.order.model.CartItem;
//import com.app.ecome.model.Product;
import com.ecommerce.order.repository.CartItemRepository;
//import com.app.ecome.repository.ProductRepository;
//import com.app.ecome.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import com.app.ecome.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    //    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    int attempt=0;
//@CircuitBreaker(name = "productServices" , fallbackMethod = "addToCartFallBack")
    @Retry(name = "retryBreaker" , fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        System.out.println("attempt count"+ ++attempt);
    ProductResponse productResponse=productServiceClient.getproductdetails(String.valueOf(cartItemRequest.getProductId()));

        if(productResponse==null){
            return  false;
        }
        if ((productResponse.getStockQuantity()<cartItemRequest.getQuantity())){
            return false;
        }

        UserResponce userResponce = userServiceClient.getUserDetails(userId);

        if(userResponce==null){
            return false;
        }


//        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
//
//        if (productOpt.isEmpty()) {
//            return false;
//        }
//
//        Product product = productOpt.get();
//
//        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//
//        if (userOpt.isEmpty()) {
//            return false;
//        }
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(cartItemRequest.getProductId()));

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1919191919.19));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();

            cartItem.setUserId(userId);
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1919191919.19));

            cartItemRepository.save(cartItem);
        }

        return true;
    }
    // make sure both  original method and falback method have same parameter
    public  boolean addToCartFallBack(String userId, CartItemRequest cartItemRequest , Exception  e){
        System.out.println(" folback for addToCart Called");

     return  false;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(productId));

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }


        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void cleraCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
