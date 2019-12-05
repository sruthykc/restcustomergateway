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

	CustomerDTO findCustomerByIdpCode(String idpCode);

	/*Page<Address> findByCustomerId(String customerId, Pageable pageable);*/
	public ContactDTO findContactById(Long id);
	Page<FavouriteProduct> findFavouriteProductsByCustomerReference(String reference, Pageable pageable);
	public Boolean checkUserExistsByIdpCode(String idpCode);
	public CustomerDTO findByMobileNumber(Long mobileNumber);
	Page<FavouriteStore> findFavouriteStoresByCustomerReference(String reference, Pageable pageable);
	//public ResponseEntity<CustomerDTO> findByMobileNumber(Long mobileNumber);
	//public ResponseEntity<CustomerDTO> findCustomerDTOByIdpCode( String idpCode) ;
	//public ResponseEntity<ContactDTO> findContactById(Long id);
	//public ResponseEntity<Boolean> checkUserExists(String reference);
}
