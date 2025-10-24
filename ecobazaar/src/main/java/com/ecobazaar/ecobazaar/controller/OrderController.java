
package com.ecobazaar.ecobazaar.controller;


import java.util.List;


import org.springframework.web.bind.annotation.*;


import com.ecobazaar.ecobazaar.model.Order;

import com.ecobazaar.ecobazaar.service.OrderService;


@RestController

@RequestMapping("/api/orders")

public class OrderController {


private final OrderService orderService;

public OrderController(OrderService orderService) {

this.orderService = orderService;

}




@PostMapping("/checkout/{userId}")

public Order checkout(@PathVariable Long userId) {

return orderService.checkout(userId);

}





@GetMapping("/{userId}")

public List<Order> getUserOrders(@PathVariable Long userId) {

return orderService.getOrdersByUserId(userId);

}

}