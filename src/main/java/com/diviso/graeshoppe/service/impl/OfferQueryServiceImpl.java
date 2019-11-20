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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.order.model.Offer;
import com.diviso.graeshoppe.client.store.domain.Review;
import com.diviso.graeshoppe.service.OfferQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;




@Service
public class OfferQueryServiceImpl implements OfferQueryService {
	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public OfferQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}
	
	/*@Override
	public List<Offer> findOfferLinesByOrderId(Long orderId) {
		StringQuery searchQuery = new StringQuery(termQuery("order.id", orderId).toString());
		return elasticsearchOperations.queryForList(searchQuery, Offer.class);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 
		searchSourceBuilder.query(termQuery("order.id", orderId));

		SearchRequest searchRequest = generateSearchRequest("review", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getResult(searchResponse, pageable,new Review());
		
		
		return null;
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

	
	
	
*/
	
	
	
}
