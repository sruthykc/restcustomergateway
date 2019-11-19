package com.diviso.graeshoppe.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diviso.graeshoppe.client.order.model.Order;
import com.diviso.graeshoppe.client.order.model.OrderLine;

public interface OrderQueryService {

	Order findById(Long id);

	Page<Order> findOrderByCustomerId(String customerId, Pageable pageable);

	List<OrderLine> findOrderLinesByOrderId(Long orderId);

	Order findOrderByOrderId(String orderId);

	Page<Order> findOrderByStatusName(String statusName, Pageable pageable);

	Page<Order> findOrderByDatebetweenAndStoreId(Instant from, Instant to, String storeId, Pageable pageable);

	
	
}
