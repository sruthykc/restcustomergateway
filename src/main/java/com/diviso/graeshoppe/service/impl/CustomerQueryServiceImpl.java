package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.order.model.Address;
import com.diviso.graeshoppe.service.CustomerQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class CustomerQueryServiceImpl implements CustomerQueryService{
	
	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public CustomerQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}
	
	private Page<Customer> getCustomerSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Customer> customerList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			customerList.add(objectMapper.convertValue(hit.getSourceAsMap(), Customer.class));
		}

		return new PageImpl(customerList,page,response.getHits().getTotalHits());

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
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Customer> customerList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			customerList.add(objectMapper.convertValue(hit.getSourceAsMap(), Customer.class));
		}
		return customerList.get(0);

	}
	
	@Override
	public Page<Address> findByCustomerId(String customerId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// log.info("Customer Id is " + customerId);
		searchSourceBuilder.query(termQuery("customerId.keyword", customerId));

		SearchRequest searchRequest = generateSearchRequest("address", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return getAddressSearchResult(searchResponse, pageable);

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

		SearchRequest searchRequest = generateSearchRequest("customer", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getCustomerSearchResult(searchResponse, pageable);

	}


	
	
}
