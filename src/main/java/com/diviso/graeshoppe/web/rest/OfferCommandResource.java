package com.diviso.graeshoppe.web.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diviso.graeshoppe.client.offer.api.AggregateCommandResourceApi;
import com.diviso.graeshoppe.client.offer.model.OrderModel;
import com.diviso.graeshoppe.client.order.api.ReportQueryResourceApi;
import com.diviso.graeshoppe.service.QueryService;

@RestController
@RequestMapping("/api")
public class OfferCommandResource {

	@Autowired
	private AggregateCommandResourceApi aggregateCommandResourceApi;
	
	@Autowired
	private ReportQueryResourceApi reportQueryResource;
	
	@PostMapping("/claimOffer/{customerId}")
	public ResponseEntity<OrderModel> checkOfferEligibility(@RequestBody OrderModel orderModel,@PathVariable String customerId) {
		ResponseEntity<Long> count=reportQueryResource.findOrderCountByCustomerIdAndStatusNameUsingGET("delivered", customerId,1, 1, new ArrayList<>());
		orderModel.setOrderNumber(count.getBody()+1);
		orderModel.setPromoCode("SUPER20");
		
		ResponseEntity<OrderModel> result=aggregateCommandResourceApi.claimOfferUsingPOST(orderModel);
		return result;
	}
}
