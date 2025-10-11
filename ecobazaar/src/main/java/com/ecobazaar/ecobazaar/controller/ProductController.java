package com.ecobazaar.ecobazaar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    @GetMapping("/eco")

    public List<Product> getEcoCertified() {

    return productService.getEcoCertifiedProducts();

    }
    @GetMapping("/eco/sorted")

    public List<Product> getEcoCertifiedSorted() {

    return productService.getEcoCertifiedSortedByCarbonImpact();

    }
}


