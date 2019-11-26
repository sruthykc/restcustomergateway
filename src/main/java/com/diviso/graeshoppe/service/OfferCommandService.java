package com.diviso.graeshoppe.service;

import org.springframework.http.ResponseEntity;

import com.diviso.graeshoppe.client.offer.model.OrderModel;

public interface OfferCommandService {

	ResponseEntity<OrderModel> claimOffer(OrderModel orderModel, String customerId);

}
