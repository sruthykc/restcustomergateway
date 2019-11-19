package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.order.model.Offer;
import com.diviso.graeshoppe.service.OfferQueryService;
import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;

import io.searchbox.client.JestClient;

@Service
public class OfferQueryServiceImpl implements OfferQueryService {
	
	@Autowired
	ElasticsearchOperations elasticsearchOperations;
	
	private final JestClient jestClient;
	private final JestElasticsearchTemplate elasticsearchTemplate;

	
	private final Logger log = LoggerFactory.getLogger(QueryServiceImpl.class);

	public OfferQueryServiceImpl(JestClient jestClient) {
		this.jestClient = jestClient;
		this.elasticsearchTemplate = new JestElasticsearchTemplate(this.jestClient);
	}

	
	
	@Override
	public List<Offer> findOfferLinesByOrderId(Long orderId) {
		StringQuery searchQuery = new StringQuery(termQuery("order.id", orderId).toString());
		return elasticsearchOperations.queryForList(searchQuery, Offer.class);
	}

	
	
	
}
