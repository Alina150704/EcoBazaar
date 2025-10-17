package com.ecobazaar.ecobazaar.repository;
import com.ecobazaar.ecobazaar.model.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartRepository extends JpaRepository<CartItem, Long> {

List<CartItem> findByUserId(Long userId); 

}
