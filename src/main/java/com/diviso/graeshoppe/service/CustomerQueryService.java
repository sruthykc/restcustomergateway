package com.diviso.graeshoppe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.diviso.graeshoppe.client.customer.model.ContactDTO;
import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.customer.model.CustomerDTO;
import com.diviso.graeshoppe.client.customer.model.FavouriteProduct;
import com.diviso.graeshoppe.client.customer.model.FavouriteStore;
import com.diviso.graeshoppe.client.order.model.aggregator.Address;

public interface CustomerQueryService {

	Customer findCustomerByIdpCode(String idpCode);

	/*Page<Address> findByCustomerId(String customerId, Pageable pageable);*/

	Page<FavouriteProduct> findFavouriteProductsByCustomerReference(String reference, Pageable pageable);

	Page<FavouriteStore> findFavouriteStoresByCustomerReference(String reference, Pageable pageable);
	public ResponseEntity<CustomerDTO> findByMobileNumber(Long mobileNumber);
	public /*ResponseEntity<CustomerDTO>*/ CustomerDTO findCustomerDTOByIdpCode( String idpCode) ;
	public ResponseEntity<ContactDTO> findContactById(Long id);
	public ResponseEntity<Boolean> checkUserExists(String reference);
}
