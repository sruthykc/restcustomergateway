package com.diviso.graeshoppe.web.rest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diviso.graeshoppe.client.payment.model.ProcessPaymentRequest;
import com.diviso.graeshoppe.service.QueryService;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.diviso.graeshoppe.client.order.api.NotificationResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderCommandResourceApi;
import com.diviso.graeshoppe.client.order.model.NotificationDTO;
import com.diviso.graeshoppe.client.order.model.Order;
import com.diviso.graeshoppe.client.order.model.OrderDTO;
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
	private NotificationResourceApi notificationResourceApi;
	@Autowired
	private QueryService queryService;
	private static BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "5nmr8r4nbybmdmx9",
			"kcvvkpxg7zpk6g42", "0891247da7e5adc1a259646835135188");
	@Autowired
	private PaypalCommandResourceApi paypalCommandResourceApi;

	public void setupBraintree() {
		// MerchantAccountRequest request = new MerchantAccountRequest().currency("USD");
	}
	
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
		orderDTO.setDeliveryInfoId(order.getDeliveryInfo().getId());
		if (order.getApprovalDetails() != null) {
			orderDTO.setApprovalDetailsId(order.getApprovalDetails().getId());
		}
		orderDTO.setPaymentRef(dto.getBody().getId() + "");
		orderDTO.setStatusId(4l);
		orderCommadnREsourceApi.updateOrderUsingPUT(orderDTO);
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setDate(OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		notificationDTO.setMessage("Congrats a new order is confirmed");
		notificationDTO.setTitle("Order Confirmed");
		notificationDTO.setTargetId(order.getOrderId());
		notificationDTO.setType("PaymenProcessed");
		notificationDTO.setStatus("unread");
		notificationDTO.setReceiverId(order.getStoreId());
		ResponseEntity<NotificationDTO> result = notificationResourceApi.createNotificationUsingPOST(notificationDTO);
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
	public String createClientAuthToken() {
		ClientTokenRequest clientTokenRequest = new ClientTokenRequest();
		String clientToken = gateway.clientToken().generate(clientTokenRequest);
		return clientToken;
	}

	@PostMapping("/transaction")
	public ResponseEntity<PaymentTransactionResponse> createTransaction(@RequestBody PaymentTransaction paymentTransaction) {

		TransactionRequest request = new TransactionRequest().amount(new BigDecimal(paymentTransaction.getAmount()))
				.paymentMethodNonce(paymentTransaction.getNounce()).
				customerId(paymentTransaction.getCustomerId())
				.options().
				submitForSettlement(true).done();
				
		Result<Transaction> result = gateway.transaction().sale(request);
		PaymentTransactionResponse paymentTransactionResponse=new PaymentTransactionResponse();
		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();
			paymentTransactionResponse.setTransactionId(transaction.getId());
			System.out.println("Success!: " + transaction.getId());
		} else if (result.getTransaction() != null) {
			Transaction transaction = result.getTransaction();
			System.out.println("Error processing transaction:");
			System.out.println("  Status: " + transaction.getStatus());
			System.out.println("  Code: " + transaction.getProcessorResponseCode());
			System.out.println("  Text: " + transaction.getProcessorResponseText());
		} else {

			for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
				System.out.println("Attribute: " + error.getAttribute());
				System.out.println("  Code: " + error.getCode());
				System.out.println("  Message: " + error.getMessage());
			}

		}
	return new ResponseEntity<PaymentTransactionResponse>(paymentTransactionResponse,HttpStatus.OK);
	}

}
