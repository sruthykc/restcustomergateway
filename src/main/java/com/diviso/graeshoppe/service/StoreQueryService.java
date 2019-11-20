package com.diviso.graeshoppe.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diviso.graeshoppe.client.store.domain.DeliveryInfo;
import com.diviso.graeshoppe.client.store.domain.Review;
import com.diviso.graeshoppe.client.store.domain.Store;
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

	//Page<Cart> findByLocationNear(Pageable pageable);

	Page<Store> findStoreByLocationName(String locationName, Pageable pageable);

	Page<Store> findAndSortStoreByMinAount(Pageable pageable);

	Page<StoreType> findStoreTypeByStoreId(String storeId, Pageable pageable);

	Page<Store> facetSearchByStoreTypeName(List<String> storeTypeNames, Pageable pageable);

	Page<Store> findAllStores(Pageable pageable);


}
