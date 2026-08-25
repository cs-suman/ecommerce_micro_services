package com.ecommerce.order.services;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.model.CartItem;
//import com.app.ecome.model.Product;
import com.ecommerce.order.repository.CartItemRepository;
//import com.app.ecome.repository.ProductRepository;
//import com.app.ecome.repository.UserRepository;
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
    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
    ProductResponse productResponse=productServiceClient.getproductdetails(String.valueOf(cartItemRequest.getProductId()));

        if(productResponse==null){
            return  false;
        }
        if ((productResponse.getStockQuantity()<cartItemRequest.getQuantity())){
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

            cartItem.setUserId(Long.valueOf(userId));
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1919191919.19));

            cartItemRepository.save(cartItem);
        }

        return true;
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
