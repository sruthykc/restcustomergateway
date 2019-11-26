package com.diviso.graeshoppe.service.impl;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.diviso.graeshoppe.client.order.api.OrderCommandResourceApi;
import com.diviso.graeshoppe.client.payment.api.BraintreeCommandResourceApi;
import com.diviso.graeshoppe.client.payment.api.PaymentResourceApi;
import com.diviso.graeshoppe.client.payment.api.PaypalCommandResourceApi;
import com.diviso.graeshoppe.client.payment.api.RazorpayCommandResourceApi;
import com.diviso.graeshoppe.client.payment.model.CommandResource;
import com.diviso.graeshoppe.client.payment.model.OrderRequest;
import com.diviso.graeshoppe.client.payment.model.OrderResponse;
import com.diviso.graeshoppe.client.payment.model.PaymentDTO;
import com.diviso.graeshoppe.client.payment.model.PaymentExecutionRequest;
import com.diviso.graeshoppe.client.payment.model.PaymentInitiateRequest;
import com.diviso.graeshoppe.client.payment.model.PaymentInitiateResponse;
import com.diviso.graeshoppe.client.payment.model.PaymentTransaction;
import com.diviso.graeshoppe.client.payment.model.PaymentTransactionResponse;
import com.diviso.graeshoppe.client.payment.model.ProcessPaymentRequest;
import com.diviso.graeshoppe.service.PaymentCommandService;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
	
	
	@Autowired
	private PaypalCommandResourceApi paypalCommandResourceApi;

	@Autowired
	private BraintreeCommandResourceApi braintreeCommandResourceApi;

	@Autowired
	private PaymentResourceApi paymentResourceApi;
	@Autowired
	private RazorpayCommandResourceApi razorpayCommandResourceApi;

	@Autowired
	private OrderCommandResourceApi orderCommadnREsourceApi;

	@Override
	public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
		return razorpayCommandResourceApi.createOrderUsingPOST(orderRequest);
	}

	@Override
	public ResponseEntity<CommandResource> processPayment(PaymentDTO paymentDTO, String status, String taskId) {
		paymentDTO.setDateAndTime(OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		ResponseEntity<PaymentDTO> dto = paymentResourceApi.createPaymentUsingPOST(paymentDTO);
		ProcessPaymentRequest processPaymentRequest = new ProcessPaymentRequest();
		processPaymentRequest.setPaymentStatus(status);
		processPaymentRequest.setTaskId(taskId);
		return processPaymentRequest(processPaymentRequest);
	}

	public ResponseEntity<CommandResource> processPaymentRequest(
			@RequestBody ProcessPaymentRequest processPaymentRequest) {
		return paymentResourceApi.processPaymentUsingPOST(processPaymentRequest);
	}

	@Override
	public ResponseEntity<PaymentInitiateResponse> initiatePaymentUsingPOST(
			PaymentInitiateRequest paymentInitiateRequest) {
		return paypalCommandResourceApi.initiatePaymentUsingPOST(paymentInitiateRequest);
	}

	@Override
	public ResponseEntity<Void> executePaymentUsingPOST(String paymentId,
			PaymentExecutionRequest paymentExecutionRequest) {
		return paypalCommandResourceApi.executePaymentUsingPOST(paymentId, paymentExecutionRequest);
	}

	@Override
	public ResponseEntity<String> createClientAuthTokenUsingGET() {
		return braintreeCommandResourceApi.createClientAuthTokenUsingGET();
	}

	@Override
	public ResponseEntity<PaymentTransactionResponse> createTransactionUsingPOST(
			PaymentTransaction paymentTransaction) {
		return braintreeCommandResourceApi.createTransactionUsingPOST(paymentTransaction);
	}
}
