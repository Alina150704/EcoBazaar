/*package com.ecobazaar.ecobazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecobazaar.ecobazaar.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findByUserId(Long userId);
	
	@Query("SELECT SUM(o.totalPrice) from Order o where o.userId = :userId")
	Double getTotalSpendByUser(Long userId);
	
	
	@Query("SELECT SUM(o.totalCarbon) from Order o where o.userId = :userId")
	Double getTotalCarbonByUser(Long userId);
	
	

}*/
package com.ecobazaar.ecobazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // NOTE: Added missing import for @Param

import com.ecobazaar.ecobazaar.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findByUserId(Long userId);
	
	// Query to calculate the total money spent (sum of totalPrice)
	@Query("SELECT SUM(o.totalPrice) from Order o where o.userId = :userId")
	Double getTotalSpendByUser(@Param("userId") Long userId); // MODIFIED: Added @Param for clarity
	
	
	// Query to calculate the total carbon impact (sum of totalCarbon)
	@Query("SELECT SUM(o.totalCarbon) from Order o where o.userId = :userId")
	Double getTotalCarbonByUser(@Param("userId") Long userId); // MODIFIED: Added @Param for clarity
	
}