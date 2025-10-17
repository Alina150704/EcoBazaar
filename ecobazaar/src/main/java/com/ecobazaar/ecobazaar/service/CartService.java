/*package com.ecobazaar.ecobazaar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecobazaar.ecobazaar.dto.CartSummaryDto;
import com.ecobazaar.ecobazaar.model.CartItem;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.repository.CartRepository;
import com.ecobazaar.ecobazaar.repository.ProductRepository;

@Service
public class CartService {
	
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	
	public CartService(CartRepository cartRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
	}
	
	public CartItem addToCart(CartItem cartItem) {
		return cartRepository.save(cartItem);
	}
	

	public CartSummaryDto getCartSummary(Long userId) {
	    List<CartItem> cartItems = cartRepository.findByUserId(userId);

	    double totalPrice = 0;
	    double totalCarbon = 0;
	    String ecoSuggestion = null;

	    for (CartItem item : cartItems) {
	        Product product = productRepository.findById(item.getProductId())
	                .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

	        totalPrice += product.getPrice() * item.getQuantity();
	        totalCarbon += product.getCarbonImpact() * item.getQuantity();

	        if (Boolean.FALSE.equals(product.getEcoCertified())) {
	            Optional<Product> ecoAlt = productRepository
	                    .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(product.getName());

	            if (ecoAlt.isPresent()) {
	                double saved = product.getCarbonImpact() - ecoAlt.get().getCarbonImpact();

	                if (saved > 0.5) {
	                    ecoSuggestion = "💡 Switch to " + ecoAlt.get().getName()
	                            + " and save " + saved + " kg CO₂!";
	                }
	            }
	        }
	    }

	    return new CartSummaryDto(cartItems, totalPrice, totalCarbon, ecoSuggestion);
	}

	
	public void removeFromCart(Long id) {
		cartRepository.deleteById(id);
	}

}*/
package com.ecobazaar.ecobazaar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecobazaar.ecobazaar.dto.CartSummaryDto;
import com.ecobazaar.ecobazaar.model.CartItem;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.repository.CartRepository;
import com.ecobazaar.ecobazaar.repository.ProductRepository;

@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    
    public CartItem addToCart(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }
    

    public CartSummaryDto getCartSummary(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        double totalPrice = 0;
        double totalCarbon = 0;
        String ecoSuggestion = null;

        for (CartItem item : cartItems) {
            
            // --- CRASH FIX: Gracefully handle products missing from the 'products' table ---
            Optional<Product> optionalProduct = productRepository.findById(item.getProductId());

            if (optionalProduct.isEmpty()) {
                // If product is missing (like ID 1 in your case), skip this cart item.
                System.out.println("Warning: Product ID " + item.getProductId() + " not found. Skipping item.");
                continue; 
            }
            
            Product product = optionalProduct.get();
            // --- END CRASH FIX ---

            totalPrice += product.getPrice() * item.getQuantity();
            totalCarbon += product.getCarbonImpact() * item.getQuantity();

            if (Boolean.FALSE.equals(product.getEcoCertified())) {
                
                // --- ECO SUGGESTION FIX: Correctly extract the search keyword ---
                String[] words = product.getName().split(" ");
                // Get the last word and remove punctuation
                String keyword = words[words.length - 1].replaceAll("[^a-zA-Z]", ""); 
                
                Optional<Product> ecoAlt = productRepository
                        .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(keyword);

                if (ecoAlt.isPresent()) {
                    double saved = product.getCarbonImpact() - ecoAlt.get().getCarbonImpact();

                    if (saved > 0.5) {
                        ecoSuggestion = "💡 Switch to " + ecoAlt.get().getName()
                                + " and save " + String.format("%.2f", saved) + " kg CO₂!";
                    }
                }
            }
        }

        return new CartSummaryDto(cartItems, totalPrice, totalCarbon, ecoSuggestion);
    }

    
    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }

}