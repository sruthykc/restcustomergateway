package com.diviso.graeshoppe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.customer.model.FavouriteProduct;
import com.diviso.graeshoppe.client.customer.model.FavouriteStore;
import com.diviso.graeshoppe.client.order.model.aggregator.Address;

public interface CustomerQueryService {

	Customer findCustomerByReference(String reference);

	/*Page<Address> findByCustomerId(String customerId, Pageable pageable);*/

	Page<FavouriteProduct> findFavouriteProductsByCustomerReference(String reference, Pageable pageable);

	Page<FavouriteStore> findFavouriteStoresByCustomerReference(String reference, Pageable pageable);

}
