package com.diviso.graeshoppe.web.rest;

import java.security.Principal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diviso.graeshoppe.client.order.api.AddressResourceApi;
import com.diviso.graeshoppe.client.order.api.ApprovalDetailsResourceApi;
import com.diviso.graeshoppe.client.order.api.AuxilaryOrderLineResourceApi;
import com.diviso.graeshoppe.client.order.api.DeliveryInfoResourceApi;
import com.diviso.graeshoppe.client.order.api.NotificationResourceApi;
import com.diviso.graeshoppe.client.order.api.OfferResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderCommandResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderLineResourceApi;
import com.diviso.graeshoppe.client.order.model.CommandResource;
import com.diviso.graeshoppe.client.order.model.OrderDTO;
import com.diviso.graeshoppe.client.order.model.OrderLineDTO;
import com.diviso.graeshoppe.client.product.model.AuxilaryLineItemDTO;
import com.diviso.graeshoppe.service.QueryService;
import com.diviso.graeshoppe.client.order.model.AcceptOrderRequest;
import com.diviso.graeshoppe.client.order.model.Address;
import com.diviso.graeshoppe.client.order.model.AddressDTO;
import com.diviso.graeshoppe.client.order.model.ApprovalDetails;
import com.diviso.graeshoppe.client.order.model.ApprovalDetailsDTO;
import com.diviso.graeshoppe.client.order.model.AuxilaryOrderLineDTO;
import com.diviso.graeshoppe.client.order.model.DeliveryInfo;
import com.diviso.graeshoppe.client.order.model.DeliveryInfoDTO;
import com.diviso.graeshoppe.client.order.model.NotificationDTO;
import com.diviso.graeshoppe.client.order.model.OfferDTO;
import com.diviso.graeshoppe.client.order.model.Order;

@RestController
@RequestMapping("/api/command")
public class OrderCommandResource {

	private static final Logger LOG = LoggerFactory.getLogger(OrderCommandResource.class);
	@Autowired
	private OrderCommandResourceApi orderCommandResourceApi;
	@Autowired
	private OrderLineResourceApi orderLineResourceApi;
	@Autowired
	private ApprovalDetailsResourceApi approvalDetailsApi;
	@Autowired
	private AuxilaryOrderLineResourceApi auxilaryOrderLineApi;
	@Autowired
	private DeliveryInfoResourceApi deliveryInfoCommandApi;

	@Autowired
	private NotificationResourceApi notificationResourceApi;

	@Autowired
	private OfferResourceApi offerResourceApi;
	@Autowired
	private QueryService queryService;

	@Autowired
	private AddressResourceApi addressResourceApi;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/sendMessage")
	public String send(Principal principal) {
		messagingTemplate.convertAndSendToUser("test", "/queue/notification", "Message from " + principal.getName());
		return "done";
	}

	@PostMapping("/order/initiateOrder")
	public ResponseEntity<CommandResource> initiateOrder(@RequestBody Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerId(order.getCustomerId());
		orderDTO.setStoreId(order.getStoreId());
		orderDTO.setGrandTotal(order.getGrandTotal());
		orderDTO.setEmail(order.getEmail());
		orderDTO.setStatusId(1l);
		ResponseEntity<CommandResource> orderDTOResponse = createOrder(orderDTO);
		order.getOrderLines().forEach(orderLine -> {
			OrderLineDTO orderLineDTO = new OrderLineDTO();
			orderLineDTO.setPricePerUnit(orderLine.getPricePerUnit());
			orderLineDTO.setProductId(orderLine.getProductId());
			orderLineDTO.setQuantity(orderLine.getQuantity());
			orderLineDTO.setTotal(orderLine.getTotal());
			orderLineDTO.setOrderId(orderDTOResponse.getBody().getSelfId());
			OrderLineDTO lineDTOResult = createOrderLine(orderLineDTO).getBody();
			orderLine.getRequiedAuxilaries().forEach(auxilaryIem -> {
				AuxilaryOrderLineDTO auxilaryOrderLineDTO = new AuxilaryOrderLineDTO();
				auxilaryOrderLineDTO.setOrderLineId(lineDTOResult.getId());
				auxilaryOrderLineDTO.setPricePerUnit(auxilaryIem.getPricePerUnit());
				auxilaryOrderLineDTO.setProductId(auxilaryIem.getProductId());
				auxilaryOrderLineDTO.setQuantity(auxilaryIem.getQuantity());
				auxilaryOrderLineDTO.setTotal(auxilaryIem.getTotal());
				createAuxilaryLineItem(auxilaryOrderLineDTO);
			});

		});
		LOG.info("Applied Offers are " + order.getAppliedOffers());

		order.getAppliedOffers().forEach(offer -> {
			OfferDTO offerDTO = new OfferDTO();
			offerDTO.setOfferRef(offer.getOfferRef());
			offerDTO.setOrderId(orderDTOResponse.getBody().getSelfId());
			createOfferLine(offerDTO);
		});

		return orderDTOResponse;
	}

	public ResponseEntity<AuxilaryOrderLineDTO> createAuxilaryLineItem(AuxilaryOrderLineDTO auxilaryOrderLineDTO) {
		return auxilaryOrderLineApi.createAuxilaryOrderLineUsingPOST(auxilaryOrderLineDTO);
	}

	public ResponseEntity<OfferDTO> createOfferLine(OfferDTO offerDTO) {
		LOG.info("OfferDTO in create Offerline is " + offerDTO);
		return offerResourceApi.createOfferUsingPOST(offerDTO);
	}

	@GetMapping("/orders/addresses/{customerId}")
	public Page<Address> getAllSavedAddress(@PathVariable String customerId, Pageable pageable) {
		return queryService.findByCustomerId(customerId, pageable);
	}

	@PostMapping("/orders/addresses")
	public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
		ResponseEntity<AddressDTO> result = addressResourceApi.createAddressUsingPOST(addressDTO);
		return result;
	}

	@PostMapping("/orders/collectDeliveryDetails/{taskId}/{orderId}")
	public ResponseEntity<CommandResource> collectDeliveryDetails(@RequestBody DeliveryInfo deliveryInfo,
			@PathVariable String taskId, @PathVariable Long orderId) {
		DeliveryInfoDTO deliveryInfoDTO = new DeliveryInfoDTO();
		deliveryInfoDTO.setDeliveryCharge(deliveryInfo.getDeliveryCharge());
		deliveryInfoDTO.setDeliveryType(deliveryInfo.getDeliveryType());
		if (deliveryInfo.getDeliveryAddress() != null) {
			deliveryInfoDTO.setDeliveryAddressId(deliveryInfo.getDeliveryAddress().getId());
		}
		deliveryInfoDTO.setDeliveryNotes(deliveryInfo.getDeliveryNotes());
		ResponseEntity<CommandResource> deliveryInfoResult = createDeliveryInfo(taskId, deliveryInfoDTO);
		long deliveryId = deliveryInfoResult.getBody().getSelfId();
		OrderDTO orderResult = orderCommandResourceApi.getOrderUsingGET(orderId).getBody();
		orderResult.setDeliveryInfoId(deliveryId);
		ResponseEntity<CommandResource> result = deliveryInfoResult;
		if (result.getBody().getNextTaskName().equals("Accept Order")) {
			updateOrder(orderResult);
			orderResult.setStatusId(2l);
		} else if (result.getBody().getNextTaskName().equals("Process Payment")) {
			orderResult.setStatusId(3l);
			updateOrder(orderResult);
			orderCommandResourceApi.publishOrderToMessagebrokerUsingPOST(orderResult.getOrderId());

		}
		System.out.println("The updated order is " + orderResult.getStatusId());
		
		return result;

	}

	public ResponseEntity<CommandResource> createOrder(@RequestBody OrderDTO orderDTO) {

		return orderCommandResourceApi.createOrderUsingPOST(orderDTO);

	}

	public ResponseEntity<OrderLineDTO> createOrderLine(@RequestBody OrderLineDTO orderLineDTO) {
		return orderLineResourceApi.createOrderLineUsingPOST(orderLineDTO);
	}

	public ResponseEntity<CommandResource> createDeliveryInfo(@PathVariable String taskId,
			@RequestBody DeliveryInfoDTO deliveryInfoDTO) {
		return deliveryInfoCommandApi.createDeliveryInfoUsingPOST(taskId, deliveryInfoDTO);
	}

	public ResponseEntity<DeliveryInfoDTO> updateDeliveryInfo(DeliveryInfoDTO deliveryInfoDTO) {
		return deliveryInfoCommandApi.updateDeliveryInfoUsingPUT(deliveryInfoDTO);
	}

	public ResponseEntity<OrderDTO> updateOrder(OrderDTO orderDTO) {
		return orderCommandResourceApi.updateOrderUsingPUT(orderDTO);
	}

	@PutMapping("/notifications")
	public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO) {
		return notificationResourceApi.updateNotificationUsingPUT(notificationDTO);
	}

	@PutMapping("/addresses")
	public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO) {
		return addressResourceApi.updateAddressUsingPUT(addressDTO);
	}

	@DeleteMapping("/addresses/{id}")
	public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
		return addressResourceApi.deleteAddressUsingDELETE(id);
	}
	
	
}
