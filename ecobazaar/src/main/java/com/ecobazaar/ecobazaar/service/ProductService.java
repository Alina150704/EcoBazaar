package com.ecobazaar.ecobazaar.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

   
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

   
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

   
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDetails(updatedProduct.getDetails());
                    product.setPrice(updatedProduct.getPrice());
                    product.setCarbonImpact(updatedProduct.getCarbonImpact());
                    product.setEcoCertified(updatedProduct.getEcoCertified());
                    product.setSellerId(updatedProduct.getSellerId());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

   
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public List<Product> getEcoCertifiedProducts() {

    	return productRepository.findByEcoCertifiedTrue();

    	}
    public List<Product> getEcoCertifiedSortedByCarbonImpact() {

    	return productRepository.findByEcoCertifiedTrueOrderByCarbonImpactAsc();

    	}
}


