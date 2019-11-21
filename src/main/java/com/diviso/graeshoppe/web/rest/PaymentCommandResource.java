package com.diviso.graeshoppe.web.rest;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diviso.graeshoppe.client.payment.model.ProcessPaymentRequest;
import com.diviso.graeshoppe.service.QueryService;
import com.diviso.graeshoppe.client.order.api.NotificationResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderCommandResourceApi;
import com.diviso.graeshoppe.client.order.model.NotificationDTO;
import com.diviso.graeshoppe.client.order.model.Order;
import com.diviso.graeshoppe.client.order.model.OrderDTO;
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

@RequestMapping("/api/command")
@RestController
public class PaymentCommandResource {

	@Autowired
	private PaymentResourceApi paymentResourceApi;
	@Autowired
	private RazorpayCommandResourceApi razorpayCommandResourceApi;

	@Autowired
	private OrderCommandResourceApi orderCommadnREsourceApi;

	@Autowired
	private QueryService queryService;

	@Autowired
	private PaypalCommandResourceApi paypalCommandResourceApi;

	@Autowired
	private BraintreeCommandResourceApi braintreeCommandResourceApi;
	
	@PostMapping("/razorpay/order")
	public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
		return razorpayCommandResourceApi.createOrderUsingPOST(orderRequest);
	}

	@PostMapping("/processPayment/{status}/{taskId}")
	public ResponseEntity<CommandResource> processPayment(@RequestBody PaymentDTO paymentDTO, @PathVariable String status,
			@PathVariable String taskId) {
		paymentDTO.setDateAndTime(OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		ResponseEntity<PaymentDTO> dto = paymentResourceApi.createPaymentUsingPOST(paymentDTO);
		Order order = queryService.findOrderByOrderId(paymentDTO.getTargetId());
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(order.getId());
		orderDTO.setDate(OffsetDateTime.ofInstant(order.getDate(), ZoneId.systemDefault()));
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setCustomerId(order.getCustomerId());
		orderDTO.setStoreId(order.getStoreId());
		orderDTO.setGrandTotal(order.getGrandTotal());
		orderDTO.setEmail(order.getEmail());
		orderDTO.setAllergyNote(order.getAllergyNote());
		if(order.getPreOrderDate()!=null) {
			orderDTO.setPreOrderDate(OffsetDateTime.ofInstant(order.getPreOrderDate(), ZoneId.systemDefault()));
		}
		orderDTO.setDeliveryInfoId(order.getDeliveryInfo().getId());
		if (order.getApprovalDetails() != null) {
			orderDTO.setApprovalDetailsId(order.getApprovalDetails().getId());
		}
		orderDTO.setPaymentRef(dto.getBody().getId() + "");
		orderDTO.setStatusId(4l);
		orderCommadnREsourceApi.updateOrderUsingPUT(orderDTO);
		ProcessPaymentRequest processPaymentRequest = new ProcessPaymentRequest();
		processPaymentRequest.setPaymentStatus(status);
		processPaymentRequest.setTaskId(taskId);
		return processPaymentRequest(processPaymentRequest);
		
	}

	public ResponseEntity<CommandResource> processPaymentRequest(
			@RequestBody ProcessPaymentRequest processPaymentRequest) {

		return paymentResourceApi.processPaymentUsingPOST(processPaymentRequest);
	}

	@PostMapping("/paypal/initiate")
	public ResponseEntity<PaymentInitiateResponse> initiatePayment(
			@RequestBody PaymentInitiateRequest paymentInitiateRequest) {
		return paypalCommandResourceApi.initiatePaymentUsingPOST(paymentInitiateRequest);
	}

	@PostMapping("/paypal/execute/{paymentId}")
	public ResponseEntity<Void> executePayment(@RequestBody PaymentExecutionRequest paymentExecutionRequest,
			@PathVariable String paymentId) {
		return paypalCommandResourceApi.executePaymentUsingPOST(paymentId, paymentExecutionRequest);
	}

	@GetMapping("/clientToken")
	public ResponseEntity<String> createClientAuthToken() {
		return braintreeCommandResourceApi.createClientAuthTokenUsingGET();
	}

	@PostMapping("/transaction")
	public ResponseEntity<PaymentTransactionResponse> createTransaction(@RequestBody PaymentTransaction paymentTransaction) {
		return braintreeCommandResourceApi.createTransactionUsingPOST(paymentTransaction);
	}

}
