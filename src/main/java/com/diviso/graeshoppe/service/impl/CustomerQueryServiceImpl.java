package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.customer.model.FavouriteProduct;
import com.diviso.graeshoppe.client.customer.model.FavouriteStore;
import com.diviso.graeshoppe.client.order.model.Address;
import com.diviso.graeshoppe.client.store.domain.Store;
import com.diviso.graeshoppe.service.CustomerQueryService;
import com.diviso.graeshoppe.web.rest.util.ServiceUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {
	@Autowired
	ServiceUtility serviceUtility;
	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public CustomerQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public Customer findCustomerByReference(String reference) {
		// System.out.println("findAllProductBySearchTerm>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("reference", reference));

		SearchRequest searchRequest = new SearchRequest("customer");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return serviceUtility.getObjectResult(searchResponse, new Customer());

	}

	@Override
	public Page<Address> findByCustomerId(String customerId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// log.info("Customer Id is " + customerId);
		searchSourceBuilder.query(termQuery("customerId.keyword", customerId));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("address", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new Address());

	}

	@Override
	public Page<Customer> findAllCustomersWithoutSearch(Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		searchSourceBuilder.query(matchAllQuery());

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("customer", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return serviceUtility.getPageResult(searchResponse, pageable, new Customer());

	}

	@Override
	public Page<FavouriteProduct> findFavouriteProductsByCustomerReference(String reference, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("customer.reference", reference));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("favouriteproduct", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new FavouriteProduct());
	}

	@Override
	public Page<FavouriteStore> findFavouriteStoresByCustomerReference(String reference, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("customer.reference", reference));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("favouritestore", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new FavouriteStore());
	}

}

/*
 * private Page<Customer> getCustomerSearchResult(SearchResponse response,
 * Pageable page) {
 * 
 * SearchHit[] searchHit = response.getHits().getHits();
 * 
 * List<Customer> customerList = new ArrayList<>();
 * 
 * for (SearchHit hit : searchHit) {
 * customerList.add(objectMapper.convertValue(hit.getSourceAsMap(),
 * Customer.class)); }
 * 
 * return new PageImpl(customerList,page,response.getHits().getTotalHits());
 * 
 * }
 */
/*
 * private SearchRequest generateSearchRequest(String indexName, Integer
 * totalElement, Integer pageNumber, SearchSourceBuilder sourceBuilder) {
 * SearchRequest searchRequest = new SearchRequest(indexName);
 * 
 * int offset = 0; int totalElements = 0;
 * 
 * if (pageNumber == 0) { offset = 0; totalElements = totalElement;
 * 
 * System.out.
 * println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&offset in00000000Page" +
 * offset);
 * 
 * System.out.
 * println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&totalelements in 00000000Page"
 * + totalElements); } else {
 * 
 * offset = totalElement;
 * 
 * totalElements = (pageNumber * totalElement);
 * System.out.println("****************************offset in else Page"+offset);
 * System.out.println("************************totalelements in elsePage"
 * +totalElements);
 * 
 * } sourceBuilder.from(offset); sourceBuilder.size(totalElements);
 * 
 * searchRequest.source(sourceBuilder); return searchRequest; } private <T> Page
 * getResult(SearchResponse response, Pageable page,T t) {
 * 
 * SearchHit[] searchHit = response.getHits().getHits();
 * 
 * List<T> list = new ArrayList<>();
 * 
 * for (SearchHit hit : searchHit) { list.add((T)
 * objectMapper.convertValue(hit.getSourceAsMap(), t.getClass())); }
 * 
 * return new PageImpl(list,page, response.getHits().getTotalHits());
 * 
 * }
 */
