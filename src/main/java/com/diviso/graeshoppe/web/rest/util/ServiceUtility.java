package com.diviso.graeshoppe.web.rest.util;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Rafeek
 *
 */
@Component
public class ServiceUtility {
	@Autowired
	private ObjectMapper objectMapper;
	
	public  <T> Page getPageResult(SearchResponse response, Pageable page, T t) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<T> list = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			//System.out.println("............T............"+t);
			list.add((T)objectMapper.convertValue(hit.getSourceAsMap(), t.getClass()));
		}

		return new PageImpl(list, page, response.getHits().getTotalHits());
	}
	
	
	
	public  <T> T getObjectResult(SearchResponse response, T t) {

		SearchHit[] searchHit = response.getHits().getHits();
		T type = null;

		for (SearchHit hit : searchHit) {

			type = (T) objectMapper.convertValue(hit.getSourceAsMap(), t.getClass());
		}

		return type;

	}
	
	public SearchRequest generateSearchRequest(String indexName, Integer totalElement, Integer pageNumber,
			SearchSourceBuilder sourceBuilder) {
		SearchRequest searchRequest = new SearchRequest(indexName);

		int offset = 0;
		int totalElements = 0;

		if (pageNumber == 0) {
			offset = 0;
			totalElements = totalElement;

			
		} else {

			offset = totalElement;

			totalElements = (pageNumber * totalElement);
			

		}
		sourceBuilder.from(offset);
		sourceBuilder.size(totalElements);

		searchRequest.source(sourceBuilder);
		return searchRequest;
	}

}
