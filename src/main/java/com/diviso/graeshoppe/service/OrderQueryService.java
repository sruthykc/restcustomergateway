package com.diviso.graeshoppe.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diviso.graeshoppe.client.order.model.Address;
import com.diviso.graeshoppe.client.order.model.AuxilaryOrderLine;
import com.diviso.graeshoppe.client.order.model.Notification;
import com.diviso.graeshoppe.client.order.model.Order;
import com.diviso.graeshoppe.client.order.model.OrderLine;

public interface OrderQueryService {

	Order findById(Long id);

	Page<Order> findOrderByCustomerId(String customerId, Pageable pageable);

	List<OrderLine> findOrderLinesByOrderId(Long orderId);

	Order findOrderByOrderId(String orderId);

	Page<Order> findOrderByStatusName(String statusName, Pageable pageable);

	Page<Order> findOrderByDatebetweenAndStoreId(Instant from, Instant to, String storeId, Pageable pageable);

	Page<OrderLine> findAllOrderLinesByOrderId(Long orderId, Pageable pageable);

	Long findNotificationCountByReceiverIdAndStatusName(String receiverId, String status);

	Page<Notification> findNotificationByReceiverId(String receiverId, Pageable pageable);

	Page<AuxilaryOrderLine> findAuxilaryOrderLineByOrderLineId(Long orderLineId, Pageable pageable);

	Page<Address> findAllSavedAddresses(String customerId, Pageable pageable);

	
	
}
