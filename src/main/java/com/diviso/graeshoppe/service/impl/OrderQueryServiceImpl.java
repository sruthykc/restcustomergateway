package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.order.model.Address;
import com.diviso.graeshoppe.client.order.model.Order;
import com.diviso.graeshoppe.client.order.model.OrderLine;
import com.diviso.graeshoppe.service.OrderQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class OrderQueryServiceImpl implements OrderQueryService{
	
	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public OrderQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}
	
	

	@Override
	public Order findById(Long id) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("id", id));

		SearchRequest searchRequest = new SearchRequest("order");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Order> orderList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			orderList.add(objectMapper.convertValue(hit.getSourceAsMap(), Order.class));
		}
		return orderList.get(0);

	}
	
	@Override
	public Page<Order> findOrderByCustomerId(String customerId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("customerId", customerId));

		SearchRequest searchRequest = generateSearchRequest("order", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		Page<Order> orderPage = getResult(searchResponse, pageable,new Order());
		orderPage.forEach(order -> {

			order.setOrderLines(new HashSet<OrderLine>(findOrderLinesByOrderId(order.getId())));

		});

		return orderPage;

	}

	@Override
	public List<OrderLine> findOrderLinesByOrderId(Long orderId) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		searchSourceBuilder.query(termQuery("order.id", orderId));

		SearchRequest searchRequest = new SearchRequest("orderline");
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<OrderLine> orderLineList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			orderLineList.add(objectMapper.convertValue(hit.getSourceAsMap(), OrderLine.class));
		}

		return orderLineList;

	}

	
	@Override
	public Order findOrderByOrderId(String orderId) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		searchSourceBuilder.query(termQuery("orderId.keyword", orderId));

		SearchRequest searchRequest = new SearchRequest("order");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Order> orderList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			orderList.add(objectMapper.convertValue(hit.getSourceAsMap(), Order.class));
		}
		return orderList.get(0);
	}

	@Override
	public Page<Order> findOrderByStatusName(String statusName, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("status.name.keyword", statusName));

		SearchRequest searchRequest = generateSearchRequest("order", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getResult(searchResponse, pageable,new Order());
	}

	@Override
	public Page<Order> findOrderByDatebetweenAndStoreId(Instant from, Instant to, String storeId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(termQuery("storeId", storeId))
				.must(rangeQuery("date").gte(from).lte(to)));

		SearchRequest searchRequest = generateSearchRequest("order", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getResult(searchResponse, pageable,new Order());

	}
	
	private SearchRequest generateSearchRequest(String indexName, Integer totalElement, Integer pageNumber,
			SearchSourceBuilder sourceBuilder) {
		SearchRequest searchRequest = new SearchRequest(indexName);

		int offset = 0;
		int totalElements = 0;

		if (pageNumber == 0) {
			offset = 0;
			totalElements = totalElement;

		 System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&offset in00000000Page" + offset);
			
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&totalelements in 00000000Page" + totalElements);
		} else {

			offset = totalElement;

			totalElements =  (pageNumber * totalElement);
			 System.out.println("****************************offset in else Page"+offset);
			 System.out.println("************************totalelements in elsePage"+totalElements);

		}
		sourceBuilder.from(offset);
		sourceBuilder.size(totalElements);

		searchRequest.source(sourceBuilder);
		return searchRequest;
	}
	private <T> Page getResult(SearchResponse response, Pageable page,T t) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<T> list = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			list.add((T) objectMapper.convertValue(hit.getSourceAsMap(), t.getClass()));
		}

		return new PageImpl(list,page, response.getHits().getTotalHits());

	}
	
	
	/*	
	private Page<Address> getAddressSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Address> addressList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			addressList.add(objectMapper.convertValue(hit.getSourceAsMap(), Address.class));
		}

		return new PageImpl(addressList, page, response.getHits().getTotalHits());

	}
*/
	/*	private Page<Order> getOrderSearchResult(SearchResponse response, Pageable page) {

	SearchHit[] searchHit = response.getHits().getHits();

	List<Order> orderList = new ArrayList<>();

	for (SearchHit hit : searchHit) {
		orderList.add(objectMapper.convertValue(hit.getSourceAsMap(), Order.class));
	}

	return new PageImpl(orderList, page, response.getHits().getTotalHits());

}*/

	
	
	
}
