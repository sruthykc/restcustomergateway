package com.diviso.graeshoppe.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diviso.graeshoppe.client.store.domain.DeliveryInfo;
import com.diviso.graeshoppe.client.store.domain.Review;
import com.diviso.graeshoppe.client.store.domain.Store;
import com.diviso.graeshoppe.client.store.domain.StoreAddress;
import com.diviso.graeshoppe.client.store.domain.StoreSettings;
import com.diviso.graeshoppe.client.store.domain.StoreType;
import com.diviso.graeshoppe.client.store.domain.Type;
import com.diviso.graeshoppe.client.store.domain.UserRating;
import com.diviso.graeshoppe.domain.Cart;
import com.diviso.graeshoppe.domain.ResultBucket;

public interface StoreQueryService {

	Page<Review> findAllReviews(Pageable pageable);

	Page<UserRating> findAllUserRatings(Pageable pageable);

	Store findStoreByRegNo(String regNo);

	Long findReviewCountByStoreId(String storeId);

	Page<Review> findReviewByStoreId(String storeId, Pageable pageable);

	Page<UserRating> findUserRatingByRegNo(String regNo, Pageable pageable);

	UserRating findRatingByStoreIdAndCustomerName(String storeId, String name);

	UserRating findRatingByStoreId(String storeId);

	UserRating findRatingByName(String name);

	Review findReviewByStoreIdAndCustomerName(String storeId, String name);

	List<ResultBucket> findRatingCount(Pageable pageable);

	Page<Store> findStoreByDeliveryType(String deliveryType, Pageable pageable);

	Page<Store> findStoreByType(String name, Pageable pageable);

	Page<Store> findStoreByTypeName(String name, Pageable pageable);

	Page<Store> findStoreBySearchTerm(String searchTerm, Pageable pageable);

	Page<Type> findAllDeliveryTypesByStoreId(Long storeId, Pageable pageable);

	DeliveryInfo findDeliveryInfoById(Long id);

	Page<Store> findStoreByRating(Pageable pageable);

	Page<DeliveryInfo> findDeliveryInfoByStoreId(String storeId, Pageable pageable);

	Page<Store> findStoreByLocationName(String locationName, Pageable pageable);

	Page<Store> findAndSortStoreByMinAount(Pageable pageable);

	Page<StoreType> findStoreTypeByStoreId(String storeId, Pageable pageable);

	void facetSearchByStoreTypeName(List<String> storeTypeNames, Pageable pageable);

	Page<Store> findAllStores(Pageable pageable);

	List<ResultBucket> findStoreTypeAndCount(Pageable pageable);

	Page<Store> findByLocationNear(Double lat,Double lon, Double distance ,String distanceUnit,Pageable pageable);

	Page<Store> headerSearch(String searchTerm, Pageable pageable) throws IOException;

	StoreSettings getStoreSettings(String iDPCode);

	StoreAddress getStoreAddress(String iDPCode);

	Store findStoreById(Long id);
	
	Page<Store> test(List<String> name);


}
