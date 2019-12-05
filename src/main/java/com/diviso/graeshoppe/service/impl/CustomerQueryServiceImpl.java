package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.diviso.graeshoppe.client.customer.api.ContactResourceApi;
import com.diviso.graeshoppe.client.customer.api.CustomerResourceApi;
import com.diviso.graeshoppe.client.customer.model.Contact;
import com.diviso.graeshoppe.client.customer.model.ContactDTO;
import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.customer.model.CustomerDTO;
import com.diviso.graeshoppe.client.customer.model.FavouriteProduct;
import com.diviso.graeshoppe.client.customer.model.FavouriteStore;
import com.diviso.graeshoppe.client.order.model.aggregator.Address;
import com.diviso.graeshoppe.client.store.model.Store;
import com.diviso.graeshoppe.service.CustomerQueryService;
import com.diviso.graeshoppe.service.mapper.ContactMapper;
import com.diviso.graeshoppe.service.mapper.CustomerMapper;
import com.diviso.graeshoppe.web.rest.util.ServiceUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {
	@Autowired
	ServiceUtility serviceUtility;
	@Autowired
	CustomerResourceApi customerResourceApi;
	@Autowired
	ContactResourceApi contactResourceApi;

	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	ContactMapper contactMapper;

	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public CustomerQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public CustomerDTO findCustomerByIdpCode(String idpCode) {

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(termQuery("idpCode.keyword", reference));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("customer");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
				.filter(QueryBuilders.termQuery("idpCode.keyword", idpCode));

		// QueryBuilder dslQuery = termQuery("idpCode.keyword", reference);
		SearchResponse searchResponse = serviceUtility.searchResponseForObject("customer", dslQuery);
		Customer customer = serviceUtility.getObjectResult(searchResponse, new Customer());

		return customerMapper.toDto(customer);

	}

	/*
	 * @Override public Page<Address> findByCustomerId(String customerId, Pageable
	 * pageable) {
	 * 
	 * QueryBuilder dslQuery =
	 * QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
	 * .filter(QueryBuilders.termQuery("customerId.keyword", customerId));
	 * 
	 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	 * searchSourceBuilder.query(dslQuery); SearchResponse searchResponse =
	 * serviceUtility.searchResponseForPage("address", searchSourceBuilder,
	 * pageable); return serviceUtility.getPageResult(searchResponse, pageable, new
	 * Address());
	 * 
	 * 
	 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	 * 
	 * 
	 * //QueryBuilder dslQuery = termQuery("customerId.keyword", customerId);
	 * 
	 * QueryBuilder dslQuery =
	 * QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
	 * .filter(QueryBuilders.termQuery("customerId.keyword", customerId));
	 * 
	 * searchSourceBuilder.query(dslQuery);
	 * 
	 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("address",
	 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
	 * SearchResponse searchResponse = null; try { searchResponse =
	 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
	 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
	 * 
	 * return serviceUtility.getPageResult(searchResponse, pageable, new Address());
	 * 
	 * }
	 * 
	 */

	@Override
	public Page<FavouriteProduct> findFavouriteProductsByCustomerReference(String reference, Pageable pageable) {

		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
				.filter(QueryBuilders.termQuery("customer.reference", reference));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(dslQuery);
		SearchResponse searchResponse = serviceUtility.searchResponseForPage("favouriteproduct", searchSourceBuilder,
				pageable);
		return serviceUtility.getPageResult(searchResponse, pageable, new FavouriteProduct());

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(matchQuery("customer.reference", reference));
		 * 
		 * SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("favouriteproduct",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 * 
		 * return serviceUtility.getPageResult(searchResponse, pageable, new
		 * FavouriteProduct());
		 */
	}

	@Override
	public Page<FavouriteStore> findFavouriteStoresByCustomerReference(String reference, Pageable pageable) {

		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
				.filter(QueryBuilders.termQuery("customer.reference", reference));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(dslQuery);
		SearchResponse searchResponse = serviceUtility.searchResponseForPage("favouritestore", searchSourceBuilder,
				pageable);
		return serviceUtility.getPageResult(searchResponse, pageable, new FavouriteStore());
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(matchQuery("customer.reference", reference));
		 * 
		 * SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("favouritestore",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 * 
		 * return serviceUtility.getPageResult(searchResponse, pageable, new
		 * FavouriteStore());
		 */
	}

	public ContactDTO findContactById(Long id) {
		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
				.filter(QueryBuilders.termQuery("id", id));

		SearchResponse searchResponse = serviceUtility.searchResponseForObject("contact", dslQuery);
		Contact contact = serviceUtility.getObjectResult(searchResponse, new Contact());

		return contactMapper.toDto(contact);

	}

	public CustomerDTO findByMobileNumber(Long mobileNumber) {
		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
				.filter(QueryBuilders.termQuery("mobileNumber", mobileNumber));

		SearchResponse searchResponse = serviceUtility.searchResponseForObject("customer", dslQuery);
		Customer customer = serviceUtility.getObjectResult(searchResponse, new Customer());

		return customerMapper.toDto(customer);
	}

	public Boolean checkUserExistsByIdpCode(String idpCode) {

		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery())
				.filter(QueryBuilders.termQuery("idpCode", idpCode));

		SearchResponse searchResponse = serviceUtility.searchResponseForObject("customer", dslQuery);
		Customer customer = serviceUtility.getObjectResult(searchResponse, new Customer());

		Boolean result = customer == null ? false : true;
		return result;
	}

	// delete me when all testing completed
	/*
	 * public ResponseEntity<CustomerDTO> findCustomerDTOByIdpCode( String idpCode)
	 * { return
	 * customerResourceApi.modelToDtoUsingPOST(findCustomerByIdpCode(idpCode));
	 * 
	 * 
	 * }
	 */
	// delete me when all testing completed
	/*
	 * public ResponseEntity<ContactDTO> findContactById(Long id){ return
	 * contactResourceApi.getContactUsingGET(id); }
	 */

	// delete me when all testing completed
	/*
	 * public ResponseEntity<Boolean> checkUserExists(String reference){ return
	 * customerResourceApi.checkUserExistsUsingGET(reference); }
	 */
	// delete me when all testing completed
	/*
	 * public ResponseEntity<CustomerDTO> findByMobileNumber(Long mobileNumber){
	 * return customerResourceApi.findByMobileNumberUsingGET(mobileNumber); }
	 */
}
