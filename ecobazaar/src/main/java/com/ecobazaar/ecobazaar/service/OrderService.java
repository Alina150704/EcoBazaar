
/*package com.ecobazaar.ecobazaar.service;


import java.time.LocalDate;

import java.util.List;


import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


import com.ecobazaar.ecobazaar.model.CartItem;

import com.ecobazaar.ecobazaar.model.Order;

import com.ecobazaar.ecobazaar.model.Product;

import com.ecobazaar.ecobazaar.repository.CartRepository;

import com.ecobazaar.ecobazaar.repository.OrderRepository;

import com.ecobazaar.ecobazaar.repository.ProductRepository;


@Service

public class OrderService {


private final CartRepository cartRepository;

private final ProductRepository productRepository;

private final OrderRepository orderRepository;


public OrderService(CartRepository cartRepository,

ProductRepository productRepository,

OrderRepository orderRepository) {

this.cartRepository = cartRepository;

this.productRepository = productRepository;

this.orderRepository = orderRepository;

}




@Transactional

public Order checkout(Long userId) {

List<CartItem> cartItems = cartRepository.findByUserId(userId);


if (cartItems.isEmpty()) {

throw new RuntimeException("Cart is empty! Cannot checkout.");

}


double totalPrice = 0;

double totalCarbon = 0;


for (CartItem item : cartItems) {

Product product = productRepository.findById(item.getProductId())

.orElseThrow(() -> new RuntimeException("Product not found"));

totalPrice += product.getPrice() * item.getQuantity();

totalCarbon += product.getCarbonImpact() * item.getQuantity();

}


Order order = new Order(userId, LocalDate.now(), totalPrice, totalCarbon);

Order savedOrder = orderRepository.save(order);




cartRepository.deleteAll(cartItems);


return savedOrder;

}




public List<Order> getOrdersByUser(Long userId) {

return orderRepository.findByUserId(userId);

}

}*/
package com.ecobazaar.ecobazaar.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // ADDED: For safe product lookup
import java.util.ArrayList; // ADDED: For tracking valid cart items


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ecobazaar.ecobazaar.model.CartItem;
import com.ecobazaar.ecobazaar.model.Order;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.repository.CartRepository;
import com.ecobazaar.ecobazaar.repository.OrderRepository;
import com.ecobazaar.ecobazaar.repository.ProductRepository;


@Service
public class OrderService {


    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    public OrderService(CartRepository cartRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public Order checkout(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);


        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty! Cannot checkout.");
        }


        double totalPrice = 0;
        double totalCarbon = 0;
        
        // NEW: List to collect only the items that have valid products
        List<CartItem> validCartItems = new ArrayList<>(); 


        for (CartItem item : cartItems) {
            // REVISED LOGIC: Safely check for product existence
            Optional<Product> optionalProduct = productRepository.findById(item.getProductId());
            
            if (optionalProduct.isEmpty()) {
                // If product is NOT found (the root cause of your crash), skip it.
                System.out.println("Warning: Product ID " + item.getProductId() + " not found. Skipping item in checkout.");
                continue; 
            }
            
            // Product is valid, proceed with calculation
            Product product = optionalProduct.get();
            
            totalPrice += product.getPrice() * item.getQuantity();
            totalCarbon += product.getCarbonImpact() * item.getQuantity();
            
            // Add the valid item to the list for deletion later
            validCartItems.add(item); 
        }
        
        // Check: Stop checkout if no valid items remain after skipping
        if (validCartItems.isEmpty()) {
            throw new RuntimeException("Cart contains no valid products! Cannot checkout.");
        }


        Order order = new Order(userId, LocalDate.now(), totalPrice, totalCarbon);
        Order savedOrder = orderRepository.save(order);


        // REVISED: Delete ONLY the items that were successfully processed/validated
        cartRepository.deleteAll(validCartItems); 


        return savedOrder;
    }


    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}