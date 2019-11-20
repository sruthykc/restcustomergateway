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
import com.diviso.graeshoppe.client.order.model.OrderLine;
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
    public List<Offer> findOfferLinesByOrderId(Long orderId) {
        List<Offer> offerList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("offer");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(termQuery("order.id", orderId));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}
       
    	SearchHit[] searchHit = searchResponse.getHits().getHits();

	;

		for (SearchHit hit : searchHit) {
			offerList.add(objectMapper.convertValue(hit.getSourceAsMap(), Offer.class));
		}
        return offerList;
    }
	
	
	
	
	
	
	

	
	
	

	
	
	
}
