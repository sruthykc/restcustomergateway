package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.product.model.AuxilaryLineItem;
import com.diviso.graeshoppe.client.product.model.Category;
import com.diviso.graeshoppe.client.product.model.ComboLineItem;
import com.diviso.graeshoppe.client.product.model.Discount;
import com.diviso.graeshoppe.client.product.model.Product;
import com.diviso.graeshoppe.client.product.model.StockCurrent;
import com.diviso.graeshoppe.domain.ResultBucket;
import com.diviso.graeshoppe.service.ProductQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductQueryServiceImpl implements ProductQueryService{


	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public ProductQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}
	
	public Page<Product> findAllProductBySearchTerm(String searchTerm, Pageable pageable) {

		// System.out.println("findAllProductBySearchTerm>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String[] includeFields = new String[] { "iDPcode", "image" };
		String[] excludeFields = new String[] { "category.*", "brand.*" };
		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		searchSourceBuilder.query(matchQuery("name", searchTerm));

		SearchRequest searchRequest = generateSearchRequest("product", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getProductSearchResult(searchResponse, pageable,new Product());

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
	
	private <T> Page getProductSearchResult(SearchResponse response, Pageable page,T t) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<T> productList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			productList.add((T) objectMapper.convertValue(hit.getSourceAsMap(), t.getClass()));
		}

		return new PageImpl(productList,page, response.getHits().getTotalHits());

	}
	
	@Override
	public Page<Product> findProductByCategoryId(Long categoryId, String userId, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String[] includeFields = new String[] { "iDPcode", "image" };
		String[] excludeFields = new String[] { "category.*", "brand.*" };
		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("category.id", categoryId))
				.must(QueryBuilders.matchQuery("iDPcode", userId)));

		SearchRequest searchRequest = generateSearchRequest("product", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getProductSearchResult(searchResponse, pageable,new Product());

	}
	
	@Override
	public Page<StockCurrent> findStockCurrentByProductId(Long productId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(termQuery("product.id", productId));

		SearchRequest searchRequest = generateSearchRequest("stockcurrent", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStockCurrentSearchResult(searchResponse, pageable);

	}

	private Page<StockCurrent> getStockCurrentSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<StockCurrent> stockCurrentList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			stockCurrentList.add(objectMapper.convertValue(hit.getSourceAsMap(), StockCurrent.class));
		}

		return new PageImpl(stockCurrentList, page, response.getHits().getTotalHits());

	}


	@Override
	public Page<StockCurrent> findStockCurrentByProductName(String name, String storeId, Pageable pageable) {
		QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("product.name", name))
				.must(QueryBuilders.matchQuery("product.iDPcode", storeId))
				.filter(QueryBuilders.termQuery("product.isAuxilaryItem", "false"));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(query);

		SearchRequest searchRequest = generateSearchRequest("stockcurrent", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStockCurrentSearchResult(searchResponse, pageable);

	}

	private Page<Category> getCategorySearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Category> categoryList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			categoryList.add(objectMapper.convertValue(hit.getSourceAsMap(), Category.class));
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + categoryList.get(0));
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + categoryList.size());
		}

		return new PageImpl(categoryList, page, response.getHits().getTotalHits());

	}

	
	@Override
	public Page<Product> findAllProductsByStoreId(String storeId, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(termQuery("iDPcode", storeId));

		SearchRequest searchRequest = generateSearchRequest("product", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getProductSearchResult(searchResponse, pageable,new Product());
	}

	
	@Override
	public Page<StockCurrent> findStockCurrentByStoreId(String iDPcode, Pageable pageable) {

		QueryBuilder query = QueryBuilders.boolQuery()

				.must(QueryBuilders.matchQuery("iDPcode", iDPcode))
				.filter(QueryBuilders.termQuery("product.isAuxilaryItem", "false"));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(query);

		SearchRequest searchRequest = generateSearchRequest("stockcurrent", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStockCurrentSearchResult(searchResponse, pageable);

	}

	@Override
	public List<ResultBucket> findCategoryAndCount(Pageable pageable) {
		List<ResultBucket> resultBucketList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("product");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		searchSourceBuilder.aggregation(AggregationBuilders.terms("totalcategories").field("category.name.keyword"));

		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("elasticsearch response: {} totalhitssshits" + searchResponse.getHits().getTotalHits());
		System.out.println("elasticsearch response: {} hits .toostring" + searchResponse.toString());
		// searchResponse.getHits().
		Aggregations aggregations = searchResponse.getAggregations();
		Terms categoryAggregation = searchResponse.getAggregations().get("totalcategories");
		for (Terms.Bucket bucket : categoryAggregation.getBuckets()) {
			ResultBucket result = new ResultBucket();
			result.setKey(bucket.getKey().toString());
			result.setDocCount(bucket.getDocCount());
			result.setKeyAsString(bucket.getKeyAsString());
			resultBucketList.add(result);
			System.out.println("KEY:" + bucket.getKey() + "!!keyAsString:" + bucket.getKeyAsString() + "!!count:"
					+ bucket.getDocCount());

		}

		return resultBucketList;
	}
	
	@Override
	public List<ResultBucket> findCategoryAndCountByStoreId(String storeId, Pageable pageable) {
		List<ResultBucket> resultBucketList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("product");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		TermsAggregationBuilder aggregation = AggregationBuilders.terms("totalcategories")
				.field("category.name.keyword");
		aggregation.subAggregation(AggregationBuilders.terms("store").field("iDPcode.keyword"));
		searchSourceBuilder.aggregation(aggregation);

		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("elasticsearch response: {} totalhitssshits" + searchResponse.getHits().getTotalHits());
		System.out.println("elasticsearch response: {} hits .toostring" + searchResponse.toString());
		// searchResponse.getHits().
		Aggregations aggregations = searchResponse.getAggregations();
		Terms categoryAggregation = searchResponse.getAggregations().get("totalcategories");
		for (Terms.Bucket bucket : categoryAggregation.getBuckets()) {
			ResultBucket result = new ResultBucket();
			result.setKey(bucket.getKey().toString());
			result.setDocCount(bucket.getDocCount());
			result.setKeyAsString(bucket.getKeyAsString());
			resultBucketList.add(result);
			System.out.println("KEY:" + bucket.getKey() + "!!keyAsString:" + bucket.getKeyAsString() + "!!count:"
					+ bucket.getDocCount());

		}

		List<ResultBucket> storeBasedEntry = new ArrayList<>();

		categoryAggregation.getBuckets().forEach(bucket -> {

			int i = 0;

			System.out.println("SSSSSSSSSSSSSSSSSS" + bucket.getAggregations().get("store"));

			Terms storeAggregation = bucket.getAggregations().get("store");
			System.out.println("storeAggregation" + storeAggregation.getBuckets().get(i).getKeyAsString());
			String storeName = storeAggregation.getBuckets().get(i).getKeyAsString();

			if (storeName.equals(storeId)) {
				ResultBucket storeEntry = new ResultBucket();
				storeEntry.setKey(bucket.getKey().toString());
				storeEntry.setDocCount(bucket.getDocCount());
				storeEntry.setKeyAsString(bucket.getKeyAsString());

				storeBasedEntry.add(storeEntry);
			}
			i++;
		});

		return storeBasedEntry;

	}
	
	@Override
	public Page<StockCurrent> findAllStockCurrentByProductNameStoreId(String productName, String storeId,
			Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode", storeId))
				.must(QueryBuilders.termQuery("product.name", productName)));

		SearchRequest searchRequest = generateSearchRequest("stockcurrent", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStockCurrentSearchResult(searchResponse, pageable);

	}
	
	@Override
	public Page<Category> findCategoryByIDPcode(String iDPcode, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		System.out.println("helllllllllllllllllllllllllo");
		searchSourceBuilder.query(termQuery("iDPcode", iDPcode));

		SearchRequest searchRequest = generateSearchRequest("category", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return getCategorySearchResult(searchResponse, pageable);

	}

	@Override
	public Page<Product> findProductByStoreIdAndCategoryName(String userId, String categoryName, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String[] includeFields = new String[] { "iDPcode", "image" };
		String[] excludeFields = new String[] { "category.*" };
		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode.keyword", userId))
				.must(QueryBuilders.termQuery("category.name.keyword", categoryName)));

		SearchRequest searchRequest = generateSearchRequest("product", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getProductSearchResult(searchResponse, pageable,new Product());

	}
	
	@Override
	public List<StockCurrent> findStockCurrentByStoreIdAndCategoryId(String userId, Long categoryId,
			Pageable pageable) {

		QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode.keyword", userId))
				.must(QueryBuilders.termQuery("category.id", categoryId))
				.filter(QueryBuilders.termQuery("product.isAuxilaryItem", "false"));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(query);
		SearchRequest searchRequest = generateSearchRequest("product", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Product> productList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			productList.add(objectMapper.convertValue(hit.getSourceAsMap(), Product.class));
		}
		return findStockCurrentByProductId(productList);

	}

	private List<StockCurrent> findStockCurrentByProductId(List<Product> productList) {
		List<StockCurrent> resultList = new ArrayList<>();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		for (Product product : productList) {
			searchSourceBuilder.query(termQuery("product.id", product.getId()));

			SearchRequest searchRequest = new SearchRequest("stockcurrent");
			searchRequest.source(searchSourceBuilder);
			SearchResponse searchResponse = null;
			try {
				searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			} catch (IOException e) { // TODO Auto-generated
				e.printStackTrace();
			}

			SearchHit[] searchHit = searchResponse.getHits().getHits();

			List<StockCurrent> stockCurrentList = new ArrayList<>();

			for (SearchHit hit : searchHit) {
				stockCurrentList.add(objectMapper.convertValue(hit.getSourceAsMap(), StockCurrent.class));
			}
			resultList.add(stockCurrentList.get(0));

		}
		return resultList;
	}

	@Override
	public Product findProductById(Long id) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		searchSourceBuilder.query(termQuery("id", id));

		SearchRequest searchRequest = new SearchRequest("product");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Product> productList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			productList.add(objectMapper.convertValue(hit.getSourceAsMap(), Product.class));
		}
		return productList.get(0);

	}
	@Override
	public Page<StockCurrent> findAndSortProductByPrice(Double from, Double to, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(rangeQuery("sellPrice").gte(from).lte(to));
		searchSourceBuilder.sort(new FieldSortBuilder("sellPrice").order(SortOrder.DESC));

		SearchRequest searchRequest = generateSearchRequest("stockcurrent", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getStockCurrentSearchResult(searchResponse, pageable);

	}
	
	@Override
	public Page<Product> findProductsByCategoryName(String name, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String[] includeFields = new String[] { "iDPcode", "image" };
		String[] excludeFields = new String[] { "category.*", "brand.*" };
		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		searchSourceBuilder.query(matchQuery("category.name", name));

		SearchRequest searchRequest = generateSearchRequest("product", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getProductSearchResult(searchResponse, pageable,new Product());
	}
	
	@Override
	public Page<AuxilaryLineItem> findAllAuxilariesByProductId(Long productId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("product.id", productId));

		SearchRequest searchRequest = generateSearchRequest("auxilarylineitem", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getAuxilaryLineItemSearchResult(searchResponse, pageable);

	}

	private Page<AuxilaryLineItem> getAuxilaryLineItemSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<AuxilaryLineItem> auxilaryLineItemList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			auxilaryLineItemList.add(objectMapper.convertValue(hit.getSourceAsMap(), AuxilaryLineItem.class));
		}

		return new PageImpl(auxilaryLineItemList, page, response.getHits().getTotalHits());

	}

	@Override
	public Page<StockCurrent> findStockCurrentByCategoryNameAndStoreId(String categoryName, String storeId,
			Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(
				QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("product.category.name.keyword", categoryName))
						.must(QueryBuilders.matchQuery("product.iDPcode", storeId)));

		SearchRequest searchRequest = generateSearchRequest("stockcurrent", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getStockCurrentSearchResult(searchResponse, pageable);

	}
	
	@Override
	public Page<ComboLineItem> findAllCombosByProductId(Long productId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("product.id", productId));

		SearchRequest searchRequest = generateSearchRequest("combolineitem", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getComboLineItemSearchResult(searchResponse, pageable);
	}
	
	private Page<ComboLineItem> getComboLineItemSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<ComboLineItem> comboLineItemList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			comboLineItemList.add(objectMapper.convertValue(hit.getSourceAsMap(), ComboLineItem.class));
		}

		return new PageImpl(comboLineItemList, page, response.getHits().getTotalHits());

	}
	
	@Override
	public Discount findDiscountByProductId(Long productId) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("id", productId));

		SearchRequest searchRequest = new SearchRequest("product");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Product> productList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			productList.add(objectMapper.convertValue(hit.getSourceAsMap(), Product.class));
		}
		return productList.get(0).getDiscount();

	}

	
	
}
