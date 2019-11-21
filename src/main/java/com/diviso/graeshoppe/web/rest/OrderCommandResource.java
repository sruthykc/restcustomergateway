package com.diviso.graeshoppe.web.rest;

import java.security.Principal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
import com.diviso.graeshoppe.client.order.api.DeliveryInfoCommandResourceApi;
import com.diviso.graeshoppe.client.order.api.DeliveryInfoResourceApi;
import com.diviso.graeshoppe.client.order.api.NotificationResourceApi;
import com.diviso.graeshoppe.client.order.api.OfferResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderCommandResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderLineCommandResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderLineResourceApi;
import com.diviso.graeshoppe.client.order.api.OrderQueryResourceApi;
import com.diviso.graeshoppe.client.order.model.CommandResource;
import com.diviso.graeshoppe.client.order.model.OrderDTO;
import com.diviso.graeshoppe.client.order.model.OrderLine;
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
	private OrderLineCommandResourceApi orderLineCommandResource;
	@Autowired
	private AuxilaryOrderLineResourceApi auxilaryOrderLineApi;
	@Autowired
	private DeliveryInfoCommandResourceApi deliveryInfoCommandApi;
	@Autowired
	private OrderQueryResourceApi orderQueryResource;
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
		orderDTO.setAllergyNote(order.getAllergyNote());
		orderDTO.setPreOrderDate(OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
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
		order.getAppliedOffers().forEach(offer -> {
			OfferDTO offerDTO = new OfferDTO();
			offerDTO.setOfferRef(offer.getOfferRef());
			offerDTO.setOrderDiscountAmount(offer.getOrderDiscountAmount());
			offerDTO.setDescription(offer.getDescription());
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
			@PathVariable String taskId, @PathVariable String orderId) {
		DeliveryInfoDTO deliveryInfoDTO = new DeliveryInfoDTO();
		deliveryInfoDTO.setDeliveryCharge(deliveryInfo.getDeliveryCharge());
		deliveryInfoDTO.setDeliveryType(deliveryInfo.getDeliveryType());
		if (deliveryInfo.getDeliveryAddress() != null) {
			deliveryInfoDTO.setDeliveryAddressId(deliveryInfo.getDeliveryAddress().getId());
		}
		deliveryInfoDTO.setDeliveryNotes(deliveryInfo.getDeliveryNotes());
		ResponseEntity<CommandResource> deliveryInfoResult = createDeliveryInfo(taskId, deliveryInfoDTO, orderId);
		return deliveryInfoResult;
	}

	public ResponseEntity<CommandResource> createOrder(@RequestBody OrderDTO orderDTO) {

		return orderCommandResourceApi.createOrderUsingPOST(orderDTO);

	}

	public ResponseEntity<OrderLineDTO> createOrderLine(@RequestBody OrderLineDTO orderLineDTO) {
		return orderLineResourceApi.createOrderLineUsingPOST(orderLineDTO);
	}

	public ResponseEntity<CommandResource> createDeliveryInfo(@PathVariable String taskId,
			@RequestBody DeliveryInfoDTO deliveryInfoDTO, String orderId) {
		return deliveryInfoCommandApi.createDeliveryInfoUsingPOST(taskId, orderId, deliveryInfoDTO);
	}

	public ResponseEntity<DeliveryInfoDTO> updateDeliveryInfo(DeliveryInfoDTO deliveryInfoDTO) {
		return deliveryInfoCommandApi.updateDeliveryInfoUsingPUT(deliveryInfoDTO);
	}

	public ResponseEntity<OrderDTO> updateOrder(OrderDTO orderDTO) {
		return orderCommandResourceApi.updateOrderUsingPUT(orderDTO);
	}

	@PutMapping("/delivery-info")
	public ResponseEntity<DeliveryInfoDTO> editDeliveryInfo(@RequestBody DeliveryInfo deliveryInfo) {
		DeliveryInfoDTO deliveryInfoDTO = new DeliveryInfoDTO();
		deliveryInfoDTO.setId(deliveryInfo.getId());
		deliveryInfoDTO.setDeliveryCharge(deliveryInfo.getDeliveryCharge());
		deliveryInfoDTO.setDeliveryType(deliveryInfo.getDeliveryType());
		if (deliveryInfo.getDeliveryAddress() != null) {
			deliveryInfoDTO.setDeliveryAddressId(deliveryInfo.getDeliveryAddress().getId());
		}
		deliveryInfoDTO.setDeliveryNotes(deliveryInfo.getDeliveryNotes());
		ResponseEntity<DeliveryInfoDTO> result = deliveryInfoCommandApi.updateDeliveryInfoUsingPUT(deliveryInfoDTO);
		ResponseEntity<OrderDTO> orderDTO = orderQueryResource.findByDeliveryInfoIdUsingGET(deliveryInfo.getId());
		orderDTO.getBody().setDeliveryInfoId(deliveryInfo.getId());
		orderCommandResourceApi.updateOrderUsingPUT(orderDTO.getBody());
		return result;
	}

	@PutMapping("/order")
	public ResponseEntity<OrderDTO> editOrder(@RequestBody Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerId(order.getCustomerId());
		orderDTO.setStoreId(order.getStoreId());
		orderDTO.setGrandTotal(order.getGrandTotal());
		orderDTO.setEmail(order.getEmail());
		orderDTO.setId(order.getId());
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setDate(OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		orderDTO.setStatusId(1l);
		if (order.getDeliveryInfo() != null) {
			orderDTO.setDeliveryInfoId(order.getDeliveryInfo().getId());
		}
		orderDTO.setAllergyNote(order.getAllergyNote());
		if (order.getPreOrderDate() != null) {
			orderDTO.setPreOrderDate(OffsetDateTime.ofInstant(order.getPreOrderDate(), ZoneId.systemDefault()));
		}
		ResponseEntity<OrderDTO> orderDTOResponse = orderCommandResourceApi.updateOrderUsingPUT(orderDTO);

		ResponseEntity<List<OrderLineDTO>> orderLines = orderLineCommandResource
				.findByOrderIdUsingGET(orderDTOResponse.getBody().getOrderId());

		order.getOrderLines().forEach(updatedOrderLine -> {

			OrderLineDTO orderLineDTO = new OrderLineDTO();
			Optional<OrderLineDTO> currentOrderLine = orderLines.getBody().stream().filter(orderline -> {
				LOG.info("Orderline filter check%%%% currentid " + orderline.getProductId() + " updated id "
						+ updatedOrderLine.getProductId());
				return orderline.getProductId() == updatedOrderLine.getProductId();
			}).findFirst();
			// check if the orderline is already present in the order if it is it will get
			// updates
			if (currentOrderLine.isPresent()) {
				orderLineDTO.setId(currentOrderLine.get().getId());
				orderLineDTO.setPricePerUnit(updatedOrderLine.getPricePerUnit());
				orderLineDTO.setProductId(updatedOrderLine.getProductId());
				orderLineDTO.setQuantity(updatedOrderLine.getQuantity());
				orderLineDTO.setTotal(updatedOrderLine.getTotal());
				orderLineDTO.setOrderId(orderDTOResponse.getBody().getId());
				OrderLineDTO lineDTOResult = orderLineResourceApi.updateOrderLineUsingPUT(orderLineDTO).getBody();
				ResponseEntity<List<AuxilaryOrderLineDTO>> auxilaryOrderLine = auxilaryOrderLineApi
						.getAllAuxilaryOrderLinesUsingGET1(currentOrderLine.get().getId());
				updatedOrderLine.getRequiedAuxilaries().forEach(updatedAux -> {
					AuxilaryOrderLineDTO auxilaryOrderLineDTO = new AuxilaryOrderLineDTO();
					Optional<AuxilaryOrderLineDTO> currentAux = auxilaryOrderLine.getBody().stream()
							.filter(aux -> aux.getProductId() == updatedAux.getProductId()).findFirst();
					if (currentAux.isPresent()) {
						auxilaryOrderLineDTO.setId(currentAux.get().getId());
					}
					auxilaryOrderLineDTO.setOrderLineId(lineDTOResult.getId());
					auxilaryOrderLineDTO.setPricePerUnit(updatedAux.getPricePerUnit());
					auxilaryOrderLineDTO.setProductId(updatedAux.getProductId());
					auxilaryOrderLineDTO.setQuantity(updatedAux.getQuantity());
					auxilaryOrderLineDTO.setTotal(updatedAux.getTotal());
					auxilaryOrderLineApi.updateAuxilaryOrderLineUsingPUT(auxilaryOrderLineDTO);

				});
			} else {
				// else the new orderline will be added to the order again
				orderLineDTO.setPricePerUnit(updatedOrderLine.getPricePerUnit());
				orderLineDTO.setProductId(updatedOrderLine.getProductId());
				orderLineDTO.setQuantity(updatedOrderLine.getQuantity());
				orderLineDTO.setTotal(updatedOrderLine.getTotal());
				orderLineDTO.setOrderId(orderDTOResponse.getBody().getId());
				OrderLineDTO lineDTOResult = orderLineResourceApi.createOrderLineUsingPOST(orderLineDTO).getBody();
				updatedOrderLine.getRequiedAuxilaries().forEach(auxNew -> {
					AuxilaryOrderLineDTO auxilaryOrderLineDTO = new AuxilaryOrderLineDTO();
					auxilaryOrderLineDTO.setOrderLineId(lineDTOResult.getId());
					auxilaryOrderLineDTO.setPricePerUnit(auxNew.getPricePerUnit());
					auxilaryOrderLineDTO.setProductId(auxNew.getProductId());
					auxilaryOrderLineDTO.setQuantity(auxNew.getQuantity());
					auxilaryOrderLineDTO.setTotal(auxNew.getTotal());
					auxilaryOrderLineApi.createAuxilaryOrderLineUsingPOST(auxilaryOrderLineDTO);
				});

			}
		});

		orderLines.getBody().stream().forEach(current -> {

			boolean isDelete = false;
			for (OrderLine updated : order.getOrderLines()) {
				if (current.getProductId() == updated.getProductId()) {
					isDelete = false;
					break;
				} else {
					isDelete = true;
				}
			}
			if (isDelete) {
				orderLineCommandResource.deleteByProductIdAndOrderIdUsingGET(current.getProductId(),
						current.getOrderId());
			}

		});

		return orderDTOResponse;

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
