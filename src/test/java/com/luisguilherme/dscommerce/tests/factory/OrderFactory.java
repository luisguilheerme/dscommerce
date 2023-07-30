package com.luisguilherme.dscommerce.tests.factory;

import java.time.Instant;

import com.luisguilherme.dscommerce.entities.Order;
import com.luisguilherme.dscommerce.entities.OrderItem;
import com.luisguilherme.dscommerce.entities.OrderStatus;
import com.luisguilherme.dscommerce.entities.Payment;
import com.luisguilherme.dscommerce.entities.Product;
import com.luisguilherme.dscommerce.entities.User;

public class OrderFactory {
	
	public static Order createOrder(User user) {
		
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user, new Payment());
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		return order;
	}

}
