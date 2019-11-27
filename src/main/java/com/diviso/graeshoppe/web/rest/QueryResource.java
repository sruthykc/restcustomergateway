package com.diviso.graeshoppe.web.rest;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diviso.graeshoppe.client.customer.api.ContactResourceApi;
import com.diviso.graeshoppe.client.customer.api.CustomerResourceApi;
import com.diviso.graeshoppe.client.customer.model.ContactDTO;
import com.diviso.graeshoppe.client.customer.model.CustomerDTO;
import com.diviso.graeshoppe.client.customer.model.FavouriteProduct;
import com.diviso.graeshoppe.client.customer.model.FavouriteStore;
import com.diviso.graeshoppe.client.order.api.OrderQueryResourceApi;
import com.diviso.graeshoppe.client.order.model.aggregator.Address;
import com.diviso.graeshoppe.client.order.model.aggregator.AuxilaryOrderLine;
import com.diviso.graeshoppe.client.order.model.aggregator.Offer;
import com.diviso.graeshoppe.client.order.model.OpenTask;
import com.diviso.graeshoppe.client.order.model.aggregator.Order;
import com.diviso.graeshoppe.client.order.model.aggregator.OrderLine;
import com.diviso.graeshoppe.client.order.model.aggregator.Notification;
import com.diviso.graeshoppe.client.product.api.CategoryResourceApi;
import com.diviso.graeshoppe.client.product.api.ProductResourceApi;
import com.diviso.graeshoppe.client.product.api.StockCurrentResourceApi;
import com.diviso.graeshoppe.client.product.model.AuxilaryLineItem;
import com.diviso.graeshoppe.client.product.model.Category;
import com.diviso.graeshoppe.client.product.model.ComboLineItem;
import com.diviso.graeshoppe.client.product.model.Discount;
import com.diviso.graeshoppe.client.product.model.Product;
import com.diviso.graeshoppe.client.product.model.ProductDTO;
import com.diviso.graeshoppe.client.product.model.StockCurrent;
import com.diviso.graeshoppe.client.report.model.OrderAggregator;
import com.diviso.graeshoppe.client.store.api.BannerResourceApi;
import com.diviso.graeshoppe.client.store.api.ReviewResourceApi;
import com.diviso.graeshoppe.client.store.api.StoreTypeResourceApi;
import com.diviso.graeshoppe.client.store.api.UserRatingResourceApi;
import com.diviso.graeshoppe.client.store.model.DeliveryInfo;
import com.diviso.graeshoppe.client.store.domain.RatingReview;
import com.diviso.graeshoppe.client.store.model.Review;
import com.diviso.graeshoppe.client.store.model.Store;
import com.diviso.graeshoppe.client.store.model.StoreAddress;
import com.diviso.graeshoppe.client.store.model.StoreSettings;
import com.diviso.graeshoppe.client.store.model.StoreType;
import com.diviso.graeshoppe.client.store.model.Type;
import com.diviso.graeshoppe.client.store.model.UserRating;
import com.diviso.graeshoppe.client.store.model.BannerDTO;
import com.diviso.graeshoppe.domain.ResultBucket;
import com.diviso.graeshoppe.domain.StoreTypeWrapper;
import com.diviso.graeshoppe.service.CustomerQueryService;
import com.diviso.graeshoppe.service.OfferQueryService;
import com.diviso.graeshoppe.service.OrderQueryService;
import com.diviso.graeshoppe.service.ProductQueryService;
import com.diviso.graeshoppe.service.StoreQueryService;

@RestController
@RequestMapping("/api/query")
public class QueryResource {

	private final Logger log = LoggerFactory.getLogger(QueryResource.class);
	@Autowired
	ProductQueryService productQueryService;

	@Autowired
	StoreQueryService storeQueryService;

	@Autowired
	CustomerQueryService customerQueryService;

	@Autowired
	OrderQueryService orderQueryService;

	@Autowired
	OfferQueryService offerQueryService;

	@Autowired
	CategoryResourceApi categoryResourceApi;

	@Autowired
	CustomerResourceApi customerResourceApi;

	@Autowired
	private ProductResourceApi productResourceApi;

	@Autowired
	private ContactResourceApi contactResourceApi;

	@Autowired
	private StockCurrentResourceApi stockCurrentResourceApi;

	@Autowired
	private UserRatingResourceApi userRatingResourceApi;

	@Autowired
	private ReviewResourceApi reviewResourceApi;

	@Autowired
	private BannerResourceApi BannerResourceApi;

	@Autowired
	private StoreTypeResourceApi storeTypeResourceApi;

	@Autowired
	com.diviso.graeshoppe.client.report.api.QueryResourceApi queryResource;

	@Autowired
	private OrderQueryResourceApi orderQueryResourceApi;

	@GetMapping("/findCustomerByMobileNumber/{mobileNumber}")
	public ResponseEntity<CustomerDTO> findByMobileNumber(@PathVariable Long mobileNumber) {
		return customerResourceApi.findByMobileNumberUsingGET(mobileNumber);
	}

	@GetMapping("/taskDetails/{taskName}/{orderId}/{storeId}")
	public ResponseEntity<OpenTask> getTaskDetails(@PathVariable String taskName, @PathVariable String orderId,
			@PathVariable String storeId) {
		return orderQueryResourceApi.getTaskDetailsUsingGET(taskName, orderId, storeId);

	}

	@GetMapping("/findProductById/{id}")
	public ResponseEntity<Product> findProductById(@PathVariable Long id) {
		return ResponseEntity.ok().body(productQueryService.findProductById(id));

	}

	@GetMapping("/findStoreById/{id}")
	public ResponseEntity<Store> findStoreById(@PathVariable Long id) {
		return ResponseEntity.ok().body(storeQueryService.findStoreById(id));

	}

	@GetMapping("/findOfferLinesByOrderId/{id}")
	public List<Offer> findOfferLinesByOrderId(@PathVariable Long id) {
		return offerQueryService.findOfferLinesByOrderId(id);
	}

	@GetMapping("/customers/findByReference/{reference}")
	public ResponseEntity<CustomerDTO> findCustomerByReference(@PathVariable String reference) {
		return customerResourceApi.modelToDtoUsingPOST(customerQueryService.findCustomerByReference(reference));
	}

	@GetMapping("/findStockCurrentByProductNameStoreId/{name}/{storeId}")
	public Page<StockCurrent> findStockCurrentByProductNameAndStoreId(@PathVariable String name,
			@PathVariable String storeId, Pageable pageable) {
		return productQueryService.findStockCurrentByProductName(name, storeId, pageable);
	}
	
	@GetMapping("/stock-current-by-categoryname/{categoryName}/{storeId}")
	public Page<StockCurrent> findStockCurrentByCategoryNameAndStoreId(@PathVariable String categoryName,
			@PathVariable String storeId, Pageable pageable) {
		return productQueryService.findStockCurrentByCategoryNameAndStoreId(categoryName, storeId, pageable);

	}

	@GetMapping("/sortStoreByMinAmount")
	public Page<Store> findAndSortStoreBydeliveryTime(Pageable pageable) {

		return storeQueryService.findAndSortStoreByMinAount(pageable);
	}
	
	@GetMapping("/contacts/{id}")
	public ResponseEntity<ContactDTO> findContactById(@PathVariable Long id) {
		return this.contactResourceApi.getContactUsingGET(id);
	}

	@GetMapping("/findAllCategories/{iDPcode}")
	public ResponseEntity<Page<Category>> findAllCategories(@PathVariable String iDPcode, Pageable pageable) {
		return ResponseEntity.ok().body(productQueryService.findCategoryByIDPcode(iDPcode, pageable));
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<ProductDTO> findProduct(@PathVariable Long id) {
		return this.productResourceApi.getProductUsingGET(id);
	}

	@GetMapping("/stores")
	public ResponseEntity<Page<Store>> findAllStores(Pageable pageable) {
		return ResponseEntity.ok().body(storeQueryService.findAllStores(pageable));
	}

	@GetMapping("/store/{regNo}")
	public ResponseEntity<Store> findStoreByRegisterNumber(@PathVariable String regNo) {
		return ResponseEntity.ok().body(storeQueryService.findStoreByRegNo(regNo));
	}

	@GetMapping("/stockcurrent/{storeId}")
	public ResponseEntity<Page<StockCurrent>> findStockCurrentByStoreId(@PathVariable String storeId,
			Pageable pageable) {

		return ResponseEntity.ok().body(productQueryService.findStockCurrentByStoreId(storeId, pageable));
	}


	@GetMapping("/findCategoryAndCountBystoreId/{storeId}")
	public List<ResultBucket> findCategoryAndCountBystoreId(@PathVariable String storeId, Pageable pageable) {

		return productQueryService.findCategoryAndCountByStoreId(storeId, pageable);
	}

	@GetMapping("/findProductsByCategoryName/{name}")
	public Page<Product> findProductsByCategoryName(@PathVariable String name, Pageable pageable) {
		return productQueryService.findProductsByCategoryName(name, pageable);
	}

	@GetMapping("/findStoreTypeAndCount")
	public List<ResultBucket> findStoreAndCount(Pageable pageable) {
		return storeQueryService.findStoreTypeAndCount(pageable);
	}

	@GetMapping("/findRatingReview/{storeId}")
	public ResponseEntity<Page<RatingReview>> findRatingReviewByStoreidAndCustomerName(@PathVariable String storeId,
			Pageable pageable) {
		List<RatingReview> listOfRatingreview = new ArrayList<RatingReview>();

		List<com.diviso.graeshoppe.client.customer.model.Customer> customerList = customerQueryService
				.findAllCustomersWithoutSearch(pageable).getContent();

		for (com.diviso.graeshoppe.client.customer.model.Customer c : customerList) {

			UserRating rating = storeQueryService.findRatingByStoreIdAndCustomerName(storeId, c.getIdpCode());

			Review review = storeQueryService.findReviewByStoreIdAndCustomerName(storeId, c.getIdpCode());

			if (rating != null) {

				RatingReview ratingReview = new RatingReview();

				ratingReview.setRating(userRatingResourceApi.modelToDtoUsingPOST1(rating).getBody());

				if (review != null) {

					ratingReview.setReview(reviewResourceApi.modelToDtoUsingPOST(review).getBody());

				}

				listOfRatingreview.add(ratingReview);
			}
		}

		Page<RatingReview> page = new PageImpl(listOfRatingreview);

		return ResponseEntity.ok().body(page);

	}


	@GetMapping("/findByLocationNear/{lat}/{lon}/{distance}/{distanceUnit}")
	public Page<Store> findStoreBySear(@PathVariable Double lat, @PathVariable Double lon,
			@PathVariable Double distance, @PathVariable String distanceUnit, Pageable pageable) {
		return storeQueryService.findByLocationNear(lat, lon, distance, distanceUnit, pageable);
	}

	@GetMapping("/review-count/{storeId}")
	public Long findReviewCountByStoreId(@PathVariable String storeId) {
		Long l = storeQueryService.findReviewCountByStoreId(storeId);
		return l;
	}

	
	// List<StockCurrent> to Page<StockCurrent> 
	
	@GetMapping("/findStockCurrentByStoreIdAndCategoryId/{userId}/{categoryId}")
	public List<StockCurrent> findStockCurrentByStoreIdAndCategoryId(@PathVariable("userId") String userId,
			@PathVariable("categoryId") Long categoryId, Pageable pageable) {
		log.debug("REST request to findStockCurrentByStoreIdAndCategoryId : {}", userId, categoryId);
		return productQueryService.findStockCurrentByStoreIdAndCategoryId(userId, categoryId);
	}

	@GetMapping("/ordersByCustomerId/{customerId}")
	public Page<Order> findOrdersByCustomerId(@PathVariable String customerId, Pageable pageable) {

		return orderQueryService.findOrderByCustomerId(customerId, pageable);

	}

	@GetMapping("/deliveryTypes/{storeId}")
	public Page<Type> findAllDeliveryTypesByStoreId(@PathVariable Long storeId, Pageable pageable) {

		return storeQueryService.findAllDeliveryTypesByStoreId(storeId, pageable);

	}

	@GetMapping("/header/{searchTerm}")
	public Page<Store> header(@PathVariable("searchTerm") String searchTerm, Pageable pageable) throws IOException {
		return storeQueryService.headerSearch(searchTerm, pageable);
	}

	@GetMapping("/storeByRating")
	public Page<Store> findStoreByRating(Pageable pageable) {

		return storeQueryService.findStoreByRating(pageable);
	}

	@GetMapping("/productByPrice/{from}/{to}")
	public Page<StockCurrent> findAndSortProductByPrice(@PathVariable Double from, @PathVariable Double to,
			Pageable pageable) {

		return productQueryService.findAndSortProductByPrice(from, to, pageable);
	}

	@GetMapping("/deliveryinfoByStoreId/{storeId}")
	public Page<DeliveryInfo> findDeliveryInfoByStoreId(@PathVariable String storeId, Pageable pageable) {
		return storeQueryService.findDeliveryInfoByStoreId(storeId, pageable);
	}

	@GetMapping("/store-type/{storeId}")
	public Page<StoreType> findStoreTypeByStoreId(@PathVariable String storeId, Pageable pageable) {
		return storeQueryService.findStoreTypeByStoreId(storeId, pageable);
	}

	@GetMapping("/stores/banners")
	public ResponseEntity<List<BannerDTO>> findStoreBanners(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(value = "sort", required = false) ArrayList<String> sort) {
		return BannerResourceApi.getAllBannersUsingGET(page, size, sort);

	}

	@GetMapping("/tasks")
	public ResponseEntity<List<OpenTask>> getTasks(@RequestParam(required = false) String assignee,
			@RequestParam(required = false) String assigneeLike, @RequestParam(required = false) String candidateGroup,
			@RequestParam(required = false) String candidateGroups,
			@RequestParam(required = false) String candidateUser, @RequestParam(required = false) String createdAfter,
			@RequestParam(required = false) String createdBefore, @RequestParam(required = false) String createdOn,
			@RequestParam(required = false) String name, @RequestParam(required = false) String nameLike) {
		return orderQueryResourceApi.getTasksUsingGET(assignee, assigneeLike, candidateGroup, candidateGroups,
				candidateUser, createdAfter, createdBefore, createdOn, name, nameLike);

	}

	@GetMapping("/orderByOrderId/{orderId}")
	public ResponseEntity<Order> findOrderByOrderId(@PathVariable String orderId) {
		return ResponseEntity.ok(orderQueryService.findOrderByOrderId(orderId));
	}

	@GetMapping("/auxilaries-productId/{productId}")
	public Page<AuxilaryLineItem> findAuxilariesByProductId(@PathVariable Long productId, Pageable pageable) {
		return productQueryService.findAllAuxilariesByProductId(productId, pageable);
	}

	@GetMapping("/storeSettings/{IDPCode}")
	public StoreSettings getStoreSettings(@PathVariable String IDPCode) {

		return storeQueryService.getStoreSettings(IDPCode);

	}

	@GetMapping("/storeAddress/{IDPCode}")
	public StoreAddress getStoreAddress(@PathVariable String IDPCode) {

		return storeQueryService.getStoreAddress(IDPCode);

	}

	
	@GetMapping("/orders/addresses/{customerId}")
	public Page<Address> getAllSavedAddress(@PathVariable String customerId, Pageable pageable) {
		return orderQueryService.findAllSavedAddresses(customerId,pageable);
	}
	@GetMapping("/orderStatus/{statusName}")
	public Page<Order> findOrderByStatusName(@PathVariable String statusName, Pageable pageable) {
		return orderQueryService.findOrderByStatusName(statusName, pageable);
	}

	@GetMapping("/order/{from}/{to}/{storeId}")
	public Page<Order> findOrderByDatebetweenAndStoreId(@PathVariable Instant from, @PathVariable Instant to,
			@PathVariable String storeId, Pageable pageable) {
		return orderQueryService.findOrderByDatebetweenAndStoreId(from, to, storeId, pageable);
	}

	@GetMapping("/combos-productId/{productId}")
	public Page<ComboLineItem> findComboByProductId(@PathVariable Long productId, Pageable pageable) {
		return productQueryService.findAllCombosByProductId(productId, pageable);
	}

	@GetMapping("/discount-productId/{productId}")
	public Discount findDiscountByProductId(@PathVariable Long productId) {
		return productQueryService.findDiscountByProductId(productId);
	}

	@GetMapping("/facetSearchByStoreTypeName")
	public Page<Store> facetSearchByStoreTypeName(@RequestBody List<StoreTypeWrapper> storeTypeWrapper, Pageable pageable) {

	
		return storeQueryService.facetSearchByStoreTypeName(storeTypeWrapper, pageable);
	
	}
	@GetMapping("/favouriteproductsbycustomerreference/{reference}")
	public Page<FavouriteProduct> findFavouriteProductsByCustomerReference(@PathVariable String reference,
			Pageable pageable) {

		return customerQueryService.findFavouriteProductsByCustomerReference(reference, pageable);
	}

	@GetMapping("/favouritestoresbycustomerreference/{reference}")
	public Page<FavouriteStore> findFavouriteStoresByCustomerReference(@PathVariable String reference,
			Pageable pageable) {
		return customerQueryService.findFavouriteStoresByCustomerReference(reference, pageable);
	}

	@GetMapping("/findAllOrderLinesByOrderId/{orderId}")
	public Page<OrderLine> findAllOrderLinesByOrderId(@PathVariable Long orderId, Pageable pageable) {
		return orderQueryService.findAllOrderLinesByOrderId(orderId, pageable);
	}

	@GetMapping("/findnotificationcount/{receiverId}/{status}")
	public Long findNotificationCountByReceiverIdAndStatusName(@PathVariable String receiverId,
			@PathVariable String status) {
		return orderQueryService.findNotificationCountByReceiverIdAndStatusName(receiverId, status);
	}

	@GetMapping("/findnotificationbyreceiverid/{receiverId}")
	public Page<Notification> findNotificationByReceiverId(@PathVariable String receiverId, Pageable pageable) {
		return orderQueryService.findNotificationByReceiverId(receiverId, pageable);
	}

	@GetMapping("/findAuxilaryOrderLineByOrderLineId/{orderLineId}")
	public Page<AuxilaryOrderLine> findAuxilaryOrderLineByOrderLineId(@PathVariable Long orderLineId,
			Pageable pageable) {
		return orderQueryService.findAuxilaryOrderLineByOrderLineId(orderLineId, pageable);
	}

	@GetMapping("/checkUserExists/{reference}")
	public ResponseEntity<Boolean> checkUserExists(@PathVariable String reference) {
		return customerResourceApi.checkUserExistsUsingGET(reference);
	}

	@GetMapping("/orderaggregator/{orderNumber}")
	public ResponseEntity<OrderAggregator> getOrderAggregator(@PathVariable String orderNumber) {
		return queryResource.getOrderAggregatorUsingGET(orderNumber);
	}

}
