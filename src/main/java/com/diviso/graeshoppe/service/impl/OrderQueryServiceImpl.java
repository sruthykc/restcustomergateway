package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
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
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.order.model.aggregator.Address;
import com.diviso.graeshoppe.client.order.model.aggregator.AuxilaryOrderLine;
import com.diviso.graeshoppe.client.order.model.aggregator.Order;
import com.diviso.graeshoppe.client.order.model.aggregator.OrderLine;
import com.diviso.graeshoppe.client.store.model.Store;
import com.diviso.graeshoppe.client.order.model.aggregator.Notification;
import com.diviso.graeshoppe.service.OrderQueryService;
import com.diviso.graeshoppe.web.rest.util.ServiceUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilder;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

	@Autowired
	ServiceUtility serviceUtility;
	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public OrderQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public Order findById(Long id) {
		

		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(matchAllQuery())
				.filter(termQuery("id", id));

		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(dslQuery);
		SearchResponse searchResponse = serviceUtility.searchResponseForObject("order", dslQuery);

		return serviceUtility.getObjectResult(searchResponse, new Order());
		
		/*SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("id", id));

		SearchRequest searchRequest = new SearchRequest("order");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return serviceUtility.getObjectResult(searchResponse, new Order());*/

	}

	@Override
	public Page<Order> findOrderByCustomerId(String customerId, Pageable pageable) {
		
		QueryBuilder dslQuery = termQuery("customerId.keyword", customerId) ;
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(dslQuery);
		searchSourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.DESC));
		SearchRequest searchRequest = serviceUtility.generateSearchRequest("order", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		Page<Order> orderPage = serviceUtility.getPageResult(searchResponse, pageable, new Order());
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

		return serviceUtility.getObjectResult(searchResponse, new Order());
	}

	@Override
	public Page<Order> findOrderByStatusName(String statusName, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("status.name.keyword", statusName));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("order", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new Order());
	}

	@Override
	public Page<Order> findOrderByDatebetweenAndStoreId(Instant from, Instant to, String storeId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(termQuery("storeId.keyword", storeId))
				.must(rangeQuery("date").gte(from).lte(to)));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("order", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new Order());

	}

	@Override
	public Page<OrderLine> findAllOrderLinesByOrderId(Long orderId, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("order.id", orderId));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("orderline", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new OrderLine());

	}

	@Override
	public Long findNotificationCountByReceiverIdAndStatusName(String receiverId, String status) {

		CountRequest countRequest = new CountRequest("notification"); // <1>
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // <2>
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("receiverId", receiverId))
				.must(QueryBuilders.matchQuery("status", status))); // <3>
		countRequest.source(searchSourceBuilder);

		CountResponse countResponse = null;
		try {
			countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long count = countResponse.getCount();

		return count;

	}

	
	  @Override 
	  public Page<Notification> findNotificationByReceiverId(String receiverId, Pageable pageable) {
	  
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    		searchSourceBuilder.query(termQuery("receiverId.keyword", receiverId));
    		searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.DESC));

    		SearchRequest searchRequest = serviceUtility.generateSearchRequest("notification", pageable.getPageSize(),
    				pageable.getPageNumber(), searchSourceBuilder);
    		SearchResponse searchResponse = null;
    		try {
    			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
    		}

    		return serviceUtility.getPageResult(searchResponse, pageable, new Notification());
	}
	  
	  @Override
		public Page<AuxilaryOrderLine> findAuxilaryOrderLineByOrderLineId(Long orderLineId, Pageable pageable) {
			
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

			searchSourceBuilder.query(termQuery("orderLine.id", orderLineId));

			SearchRequest searchRequest = serviceUtility.generateSearchRequest("auxilaryorderline", pageable.getPageSize(),
					pageable.getPageNumber(), searchSourceBuilder);
			SearchResponse searchResponse = null;
			try {
				searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
			}

			return serviceUtility.getPageResult(searchResponse, pageable, new AuxilaryOrderLine());
	
			
			
			
			
		}

	@Override
	public Page<Address> findAllSavedAddresses(String customerId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("customerId.keyword", customerId));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("orderaddress", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new Address());
	}
	  
	  
	  
	 

}
