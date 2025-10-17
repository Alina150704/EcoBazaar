
package com.ecobazaar.ecobazaar.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobazaar.ecobazaar.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {

List<Order> findByUserId(Long userId);

}