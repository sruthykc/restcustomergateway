package com.diviso.graeshoppe.service;

import java.util.List;

import com.diviso.graeshoppe.client.order.model.Offer;

public interface OfferQueryService {
	
	public List<Offer> findOfferLinesByOrderId(Long orderId) ;

}
