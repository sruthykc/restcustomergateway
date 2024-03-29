package com.diviso.graeshoppe.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.order.model.aggregator.Address;
import com.diviso.graeshoppe.client.order.model.aggregator.Order;
import com.diviso.graeshoppe.client.order.model.aggregator.OrderLine;
import com.diviso.graeshoppe.client.product.model.AuxilaryLineItem;
import com.diviso.graeshoppe.client.product.model.Category;
import com.diviso.graeshoppe.client.product.model.ComboLineItem;
import com.diviso.graeshoppe.client.product.model.Discount;
import com.diviso.graeshoppe.client.product.model.Product;
import com.diviso.graeshoppe.client.product.model.StockCurrent;
import com.diviso.graeshoppe.client.store.model.DeliveryInfo;
import com.diviso.graeshoppe.client.store.model.Review;
import com.diviso.graeshoppe.client.store.model.Store;
import com.diviso.graeshoppe.client.store.model.StoreAddress;
import com.diviso.graeshoppe.client.store.model.StoreSettings;
import com.diviso.graeshoppe.client.store.model.StoreType;
import com.diviso.graeshoppe.client.store.model.Type;
import com.diviso.graeshoppe.client.store.model.UserRating;
import com.diviso.graeshoppe.domain.*;

//import io.searchbox.core.search.aggregation.TermsAggregation.Entry;

public interface QueryService {
	public Page<Product> findAllProductBySearchTerm(String searchTerm, Pageable pageable);

	// public Page<Product> findAllProduct(Pageable pageable);
	public Page<Product> findProductByCategoryId(Long categoryId, String userId, Pageable pageable);

	public Page<Customer> findAllCustomersWithoutSearch(Pageable pageable);

	public Page<StockCurrent> findStockCurrentByProductId(Long productId, Pageable pageable);

	public Page<StockCurrent> findStockCurrentByProductName(String name, String storeId, Pageable pageable);

	public Page<Review> findAllReviews(Pageable pageable);

	public Page<UserRating> findAllUserRatings(Pageable pageable);

	public Page<Store> findAllStores(Pageable pageable);

	public Page<Product> findAllProductsByStoreId(String storeId, Pageable pageable);

	public Long findReviewCountByStoreId(String storeId);

	public Page<Review> findReviewByStoreId(String storeId, Pageable pageable);

	public Page<UserRating> findUserRatingByRegNo(String regNo, Pageable pageable);

	public List<ResultBucket> findCategoryAndCount(Pageable pageable);

	public List<ResultBucket> findRatingCount(Pageable pageable);

	public Customer findCustomerByReference(String reference);

	public Store findStoreByRegNo(String regNo);

	public Page<StockCurrent> findStockCurrentByStoreId(String storeId, Pageable pageable);

	public UserRating findRatingByStoreIdAndCustomerName(String storeId, String name);

	public UserRating findRatingByName(String name);

	public UserRating findRatingByStoreId(String storeId);

	public Review findReviewByStoreIdAndCustomerName(String storeId, String name);

	public Order findById(Long id);

	public Page<StockCurrent> findAllStockCurrentByProductNameStoreId(String productName, String storeId,
			Pageable pageable);

	public Page<Store> findStoreByDeliveryType(String deliveryType, Pageable pageable);

	public Page<Store> findStoreByType(String name, Pageable pageable);

	public Page<Category> findCategoryByIDPcode(String iDPcode, Pageable pageable);

	public Page<Product> findProductByStoreIdAndCategoryName(String userId, String categoryName, Pageable pageable);

	public List<OrderLine> findOrderLinesByOrderId(Long orderId);

	public Page<Store> findStoreBySearchTerm(String searchTerm, Pageable pageable);

	public Order findOrderByOrderId(String orderId);

	public DeliveryInfo findDeliveryInfoById(Long id);

	public Product findProductById(Long id);

	public Page<DeliveryInfo> findDeliveryInfoByStoreId(String storeId, Pageable pageable);

	public Page<StoreType> findStoreTypeByStoreId(String storeId, Pageable pageable);

	public List<ResultBucket> findCategoryAndCountByStoreId(String storeId, Pageable pageable);

	public Page<Store> headerSearch(String searchTerm, Pageable pageable) throws IOException;

	Page<Cart> findByLocationNear(/* Double lat,Double lon, Double distance, */ Pageable pageable);

	public Page<Address> findByCustomerId(String customerId, Pageable pageable);

	public Page<Store> findAndSortStoreByMinAount(Pageable pageable);

	public Page<Store> findStoreByLocationName(String locationName, Pageable pageable);

	public Page<Store> findStoreByRating(Pageable pageable);

	public Page<StockCurrent> findAndSortProductByPrice(Double from, Double to, Pageable pageable);

	public Page<AuxilaryLineItem> findAllAuxilariesByProductId(Long productId, Pageable pageable);

	public Page<StockCurrent> findStockCurrentByCategoryNameAndStoreId(String categoryName, String storeId,
			Pageable pageable);

	public StoreSettings getStoreSettings(String IDPCode);

	public StoreAddress getStoreAddress(String iDPCode);

	public Page<Order> findOrderByStatusName(String statusName, Pageable pageable);

	public Page<Order> findOrderByDatebetweenAndStoreId(Instant from, Instant to, String storeId, Pageable pageable);

	public Page<ComboLineItem> findAllCombosByProductId(Long productId, Pageable pageable);

	public Discount findDiscountByProductId(Long productId);

	public Page<Store> findStoreByTypeName(String name, Pageable pageable);

	public List<StockCurrent> findStockCurrentByStoreIdAndCategoryId(String userId, Long categoryId,
			Pageable pageable);
	public Page<Order> findOrderByCustomerId(String customerId, Pageable
			  pageable);
	public Page<Type> findAllDeliveryTypesByStoreId(Long storeId, Pageable pageable);
	public Page<Product> findProductsByCategoryName(String name,Pageable pageable);
	public List<ResultBucket> findStoreTypeAndCount(Pageable pageable);
	public  Page<Store> facetSearchByStoreTypeName(List<String> storeTypeNames, Pageable pageable);
	/*
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 *
								 * 
								 * 
								 * 
								 * 
								 * public Page<Product> findAllProductByName(String name);
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 *
								 * 
								 * 
								 * 
								 * 
								 * 
								 *
								 * 
								 *
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * //public OrderMaster findOrderMasterByOrderId(String orderId);
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 * 
								 *
								 * 
								 * 
								 * 
								 *//**
									 * @param storeId
									 * @param pageable
									 * @return
									 */
	/*
	 *
	 *//**
		 * @param name
		 * @return
		 */
	/*
	 * 
	 * 
	 *//**
		 * @return
		 */
	/*
	 * List<Product> findAllProducts();
	 * 
	 *//**
		 * @param productId
		 */
	/*
	 *
	 * 
	 *//**
		 * @param categoryName
		 */
	/*
	 * 
	 * 
	 *//**
		 * @param storeId
		 * @param pageable
		 * @return
		 */
	/*
	 * // List<Entry> findAllDeliveryCountByStoreId(String storeId, Pageable
	 * pageable);
	 * 
	 *//**
		 * @param point
		 * @param distance
		 * @return
		 */
	/*
	 * //public Page<Store> findByLocationNear(Point point, Distance
	 * distance,Pageable pageable);
	 * 
	 * 
	 * 
	 * 
	 *
	 * 
	 * Long findOrderCountByCustomerId(String customerId );
	 * 
	 *//**
		 * @param storeId
		 * @param pageable
		 * @return
		 */
	/*
	 
	 * 
	 *//**
		 * @param statusName
		 */
	/*
	 * public Page<Order> findOrderByStatusName(String statusName);
	 * 
	 *//**
		 * @param from
		 * @param to
		 * @return
		 */
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *//**
		 * @param iDPcode
		 * @param pageable
		 * @return
		 */
	/*
	 * //public Page<Product> findNotAuxilaryProducts(String iDPcode, Pageable
	 * pageable);
	 * 
	 *//**
		 * @param categoryName
		 * @param storeId
		 * @return
		 *//*
			*/
}
