package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.action.search.SearchType.QUERY_THEN_FETCH;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
/*import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;*/
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.diviso.graeshoppe.client.customer.domain.Customer;
import com.diviso.graeshoppe.client.order.model.Address;
import com.diviso.graeshoppe.client.order.model.Order;
import com.diviso.graeshoppe.client.order.model.OrderLine;
import com.diviso.graeshoppe.client.product.model.AuxilaryLineItem;
import com.diviso.graeshoppe.client.product.model.Category;
import com.diviso.graeshoppe.client.product.model.ComboLineItem;
import com.diviso.graeshoppe.client.product.model.Discount;
import com.diviso.graeshoppe.client.product.model.Product;
import com.diviso.graeshoppe.client.product.model.StockCurrent;
import com.diviso.graeshoppe.client.store.domain.DeliveryInfo;
import com.diviso.graeshoppe.client.store.domain.HeaderSearch;
import com.diviso.graeshoppe.client.store.domain.Review;
import com.diviso.graeshoppe.client.store.domain.Store;
import com.diviso.graeshoppe.client.store.domain.StoreAddress;
import com.diviso.graeshoppe.client.store.domain.StoreSettings;
import com.diviso.graeshoppe.client.store.domain.StoreType;
import com.diviso.graeshoppe.client.store.domain.Type;
import com.diviso.graeshoppe.client.store.domain.UserRating;
import com.diviso.graeshoppe.domain.GraeshoppeGeneric;
//import com.diviso.graeshoppe.repository.search.StoreSearchRepository;
/*import com.diviso.graeshoppe.client.product.domain.Product;
import com.diviso.graeshoppe.domain.Result;*/
import com.diviso.graeshoppe.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.diviso.graeshoppe.domain.*;


@Service
public class QueryServiceImpl implements QueryService {

	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public QueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
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
	/*
	 * public Page<Product> findAllProduct(Pageable pageable) {
	 * 
	 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	 * 
	 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
	 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
	 * excludeFields);
	 * 
	 * searchSourceBuilder.query(matchAllQuery());
	 * 
	 * SearchResponse searchResponse = null; SearchRequest searchRequest =
	 * generateSearchRequest("product", pageable.getPageSize(),
	 * pageable.getPageNumber(), searchSourceBuilder); try { searchResponse =
	 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
	 * (IOException e) {
	 * 
	 * e.printStackTrace(); } return getSearchResult(searchResponse, pageable); }
	 */

	private <T> Page getProductSearchResult(SearchResponse response, Pageable page,T t) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<T> productList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			productList.add((T) objectMapper.convertValue(hit.getSourceAsMap(), t.getClass()));
		}

		return new PageImpl(productList,page, response.getHits().getTotalHits());

	}

	private Page<Customer> getCustomerSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Customer> customerList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			customerList.add(objectMapper.convertValue(hit.getSourceAsMap(), Customer.class));
		}

		return new PageImpl(customerList,page,response.getHits().getTotalHits());

	}

	private Page<Type> getTypeSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Type> typeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			typeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Type.class));
		}

		return new PageImpl(typeList, page, response.getHits().getTotalHits());

	}

	private Page<StoreType> getStoreTypeSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<StoreType> storeTypeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeTypeList.add(objectMapper.convertValue(hit.getSourceAsMap(), StoreType.class));
		}

		return new PageImpl(storeTypeList, page, response.getHits().getTotalHits());

	}
	
	/* Not non*/

	private Page<Cart> getCartSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Cart> cartList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			cartList.add(objectMapper.convertValue(hit.getSourceAsMap(), Cart.class));
		}

		return new PageImpl(cartList, page, response.getHits().getTotalHits());

	}

	private Page<DeliveryInfo> getDeliveryInfoSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<DeliveryInfo> deliveryInfoList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			deliveryInfoList.add(objectMapper.convertValue(hit.getSourceAsMap(), DeliveryInfo.class));
		}

		return new PageImpl(deliveryInfoList, page, response.getHits().getTotalHits());

	}

	// for refference sample
	/*
	 * private <T> Page<T> getSearchResult(SearchResponse response, Pageable page,T
	 * type) {
	 * 
	 * SearchHit[] searchHit = response.getHits().getHits();
	 * 
	 * List<T> productList = new ArrayList<>();
	 * 
	 * Class<T> className=(Class<T>) type.getClass();
	 * 
	 * for (SearchHit hit : searchHit) {
	 * productList.add(objectMapper.convertValue(hit.getSourceAsMap(), className));
	 * }
	 * 
	 * return new PageImpl<T>(productList, page, response.getHits().getTotalHits());
	 * 
	 * }
	 */

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

	// completed
	/*
	 * @Override public Page<Product> findProductByCategoryId(Long categoryId,
	 * String userId, Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(
	 * "category.id", categoryId)) .must(QueryBuilders.matchQuery("iDPcode",
	 * userId))) .build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * Product.class); }
	 */

	// completed
	/*
	 * @Override public Customer findCustomerByReference(String reference) {
	 * 
	 * StringQuery searchQuery = new StringQuery(termQuery("reference",
	 * reference).toString()); return
	 * elasticsearchOperations.queryForObject(searchQuery, Customer.class); }
	 */
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

	// completed
	/*
	 * @Override public Page<StockCurrent> findStockCurrentByProductId(Long
	 * productId, Pageable pageable) { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("product.id",
	 * productId)).build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * StockCurrent.class); }
	 */

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

	// completed // using filters in elasticsearch
	/*
	 * @Override public Page<StockCurrent> findStockCurrentByProductName(String
	 * name, String storeId, Pageable pageable) { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder()
	 * .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(
	 * "product.name", name)) .must(QueryBuilders.matchQuery("product.iDPcode",
	 * storeId))) .build();
	 * 
	 * return new PageImpl(elasticsearchOperations.queryForPage(searchQuery,
	 * StockCurrent.class).stream() .filter(stockcurrent ->
	 * stockcurrent.getProduct().isIsAuxilaryItem() == false)
	 * .collect(Collectors.toList())); }
	 */
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

	// completed
	/*
	 * @Override public Page<Review> findAllReviews(Pageable pageable) { SearchQuery
	 * searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
	 * .build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * Review.class); }
	 */

	@Override
	public Page<Review> findAllReviews(Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(matchAllQuery());

		SearchRequest searchRequest = generateSearchRequest("review", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getReviewSearchResult(searchResponse, pageable);

	}

	private Page<Review> getReviewSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Review> reviewList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			reviewList.add(objectMapper.convertValue(hit.getSourceAsMap(), Review.class));
		}

		return new PageImpl(reviewList, page, response.getHits().getTotalHits());

	}

	private Page<UserRating> getUserRatingSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<UserRating> userRatingList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			userRatingList.add(objectMapper.convertValue(hit.getSourceAsMap(), UserRating.class));
		}

		return new PageImpl(userRatingList, page, response.getHits().getTotalHits());

	}

	private Page<Store> getStoreSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Store> storeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Store.class));
		}

		return new PageImpl(storeList, page, response.getHits().getTotalHits());

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
	// completed
	/*
	 * @Override public Page<UserRating> findAllUserRatings(Pageable pageable) {
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
	 * .build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * UserRating.class); }
	 */

	@Override
	public Page<UserRating> findAllUserRatings(Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(matchAllQuery());

		SearchRequest searchRequest = generateSearchRequest("userrating", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getUserRatingSearchResult(searchResponse, pageable);

	}

	// completed
	/*
	 * @Override public Page<Store> findAllStores(Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
	 * .build(); Page<Store> storePage =
	 * elasticsearchOperations.queryForPage(searchQuery, Store.class);
	 * 
	 * storePage.forEach(store -> { List<UserRating> userRating =
	 * findUserRatingByRegNo(store.getRegNo()).getContent();
	 * store.setUserRatings(new HashSet<UserRating>(userRating)); });
	 * 
	 * return storePage; }
	 */
	@Override
	public Page<Store> findAllStores(Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { ""}; String[] excludeFields = new
		 * String[] { "storeaddress.*","reviews.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */

		searchSourceBuilder.query(matchAllQuery());

		SearchRequest searchRequest = generateSearchRequest("store", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		Page<Store> storePage = getStoreSearchResult(searchResponse, pageable);

		storePage.forEach(store -> {
			List<UserRating> userRating = findUserRatingByRegNo(store.getRegNo(), pageable).getContent();
			store.setUserRatings(new HashSet<UserRating>(userRating));
		});

		return storePage;

	}

	// completed
	/*
	 * @Override public Page<Product> findAllProductsByStoreId(String storeId) {
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("iDPcode", storeId)).build();
	 * return elasticsearchOperations.queryForPage(searchQuery, Product.class); }
	 */

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

	/*
	 * @Override public List<Product> findAllProducts() { SearchQuery searchQuery =
	 * new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build(); return
	 * elasticsearchOperations.queryForList(searchQuery, Product.class); }
	 */
	/*
	 * public Page<Product> findAllProducts(Pageable pageable) {
	 * 
	 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	 * 
	 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
	 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
	 * excludeFields);
	 * 
	 * searchSourceBuilder.query(matchAllQuery());
	 * 
	 * SearchResponse searchResponse = null; SearchRequest searchRequest =
	 * generateSearchRequest("product", pageable.getPageSize(),
	 * pageable.getPageNumber(), searchSourceBuilder); try { searchResponse =
	 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
	 * (IOException e) {
	 * 
	 * e.printStackTrace(); } return getSearchResult(searchResponse, pageable); }
	 */

	// completed

	/*
	 * @Override public Store findStoreByRegNo(String regNo) { StringQuery
	 * stringQuery = new StringQuery(termQuery("regNo", regNo).toString()); return
	 * elasticsearchOperations.queryForObject(stringQuery, Store.class);
	 * 
	 * }
	 */
	@Override
	public Store findStoreByRegNo(String regNo) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("regNo", regNo));

		SearchRequest searchRequest = new SearchRequest("store");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Store> storeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Store.class));
		}
		return storeList.get(0);

	}

	// completed
	/*
	 * @Override public Long findReviewCountByStoreId(String storeId) { SearchQuery
	 * searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("store.regNo",
	 * storeId)).build(); return
	 * elasticsearchOperations.count(searchQuery,Review.class);
	 * 
	 * }
	 */
	@Override
	public Long findReviewCountByStoreId(String storeId) {
		CountRequest countRequest = new CountRequest("review");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("store.regNo", storeId));

		countRequest.source(searchSourceBuilder);
		CountResponse countResponse = null;
		try {
			countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return countResponse.getCount();
	}

	// completed //add pageable as arguement
	/*
	 * @Override public Page<Review> findReviewByStoreId(String storeId) {
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("store.regNo",
	 * storeId)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Review.class); }
	 */

	@Override
	public Page<Review> findReviewByStoreId(String storeId, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("store.regNo", storeId));

		SearchRequest searchRequest = generateSearchRequest("review", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getReviewSearchResult(searchResponse, pageable);
	}
	// completed// add Pageable
	/*
	 * @Override public Page<StockCurrent> findStockCurrentByStoreId(String iDPcode)
	 * { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("iDPcode", iDPcode)).build();
	 * 
	 * Page<StockCurrent> stockCurrentPage =
	 * elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);
	 * 
	 * List<StockCurrent> notAuxStockCurrentProducts = new
	 * ArrayList<StockCurrent>();
	 * 
	 * stockCurrentPage.forEach(s -> {
	 * 
	 * if (s.getProduct().isIsAuxilaryItem() == false) {
	 * notAuxStockCurrentProducts.add(s); } });
	 * 
	 * return new PageImpl(notAuxStockCurrentProducts); }
	 */

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

	// completed
	/*
	 * @Override public Page<UserRating> findUserRatingByRegNo(String regNo) {
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("store.regNo",
	 * regNo)).build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * UserRating.class); }
	 */

	@Override
	public Page<UserRating> findUserRatingByRegNo(String regNo, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(termQuery("store.regNo", regNo));

		SearchRequest searchRequest = generateSearchRequest("userrating", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getUserRatingSearchResult(searchResponse, pageable);

	}

	// completed // instead of List<Entry> to List<ResultBucket>
	/*
	 * @Override public List<Entry> findCategoryAndCount(Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery())
	 * .withSearchType(QUERY_THEN_FETCH)
	 * 
	 * .withIndices("product").withTypes("product")
	 * 
	 * .addAggregation(AggregationBuilders.terms("totalcategories").field(
	 * "category.name.keyword")) .withPageable(pageable).build();
	 * 
	 * AggregatedPage<Product> result =
	 * elasticsearchTemplate.queryForPage(searchQuery, Product.class);
	 * 
	 * TermsAggregation categoryAggregation =
	 * result.getAggregation("totalcategories", TermsAggregation.class);
	 * 
	 * return categoryAggregation.getBuckets();
	 * 
	 * }
	 */

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

	// **********************************************************************************************
	// completed
	/*
	 * @Override public List<Entry> findCategoryAndCountByStoreId(String
	 * storeId,Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery())
	 * 
	 * .withSearchType(QUERY_THEN_FETCH).withIndices("product").withTypes("product")
	 * 
	 * .addAggregation(AggregationBuilders.terms("totalcategories").field(
	 * "category.name.keyword")
	 * 
	 * .order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.
	 * aggregation("avgPrice",
	 * 
	 * true))
	 * .subAggregation(AggregationBuilders.avg("avgPrice").field("sellingPrice"))
	 * 
	 * .subAggregation(AggregationBuilders.terms("store").field("iDPcode.keyword")))
	 * .build();
	 * 
	 * AggregatedPage<Product> result =
	 * elasticsearchTemplate.queryForPage(searchQuery, Product.class);
	 * 
	 * TermsAggregation orderAgg = result.getAggregation("totalcategories",
	 * TermsAggregation.class);
	 * 
	 * List<Entry> storeBasedEntry = new ArrayList<Entry>();
	 * 
	 * 
	 * orderAgg.getBuckets().forEach(bucket -> {
	 * 
	 * int i = 0; double averagePrice =
	 * bucket.getAvgAggregation("avgPrice").getAvg();
	 * 
	 * System.out.println(String.format("Key: %s, Doc count: %d, Average Price: %f",
	 * bucket.getKey(), bucket.getCount(), averagePrice));
	 * 
	 * System.out.println("SSSSSSSSSSSSSSSSSS" + bucket.getAggregation("store",
	 * TermsAggregation.class).getBuckets().get(i).getKeyAsString());
	 * 
	 * String storeName = bucket.getAggregation("store",
	 * TermsAggregation.class).getBuckets().get(i) .getKeyAsString();
	 * 
	 * if (storeName.equals(storeId)) {
	 * 
	 * Entry storeEntry = bucket;
	 * 
	 * storeBasedEntry.add(storeEntry); } i++; System.out.println(
	 * "SSSSSSSSSSSSSSSSSS" + bucket.getAggregation("store",
	 * TermsAggregation.class).getBuckets().size()); });
	 * 
	 * return storeBasedEntry; }
	 */

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

	// **************************************************************************************************
	// completed // instead of List<Entry> to List<ResultBucket>
	/*
	 * public List<Entry> findStoreTypeAndCount(Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery())
	 * 
	 * .withSearchType(QUERY_THEN_FETCH).withIndices("storetype").withTypes(
	 * "storetype")
	 * 
	 * .addAggregation(AggregationBuilders.terms("totalstoretype").field(
	 * "name.keyword")) .withPageable(pageable).build();
	 * 
	 * AggregatedPage<StoreType> result =
	 * elasticsearchTemplate.queryForPage(searchQuery, StoreType.class);
	 * 
	 * TermsAggregation categoryAggregation =
	 * result.getAggregation("totalstoretype", TermsAggregation.class);
	 * 
	 * return categoryAggregation.getBuckets(); }
	 */

	public List<ResultBucket> findStoreTypeAndCount(Pageable pageable) {
		List<ResultBucket> resultBucketList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("storetype");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		searchSourceBuilder.aggregation(AggregationBuilders.terms("totalstoretype").field("name.keyword"));

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
		Terms categoryAggregation = searchResponse.getAggregations().get("totalstoretype");
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
	// ***************************************************************************************************

	// completed
	/*
	 * @Override public UserRating findRatingByStoreIdAndCustomerName(String
	 * storeId, String name) {
	 * System.out.println("....................... impl ................" + storeId
	 * + "      " + name); StringQuery stringQuery = new StringQuery(
	 * QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo",
	 * storeId)) .must(QueryBuilders.termQuery("userName", name)).toString());
	 * UserRating rating = elasticsearchOperations.queryForObject(stringQuery,
	 * UserRating.class); return rating; }
	 */
	@Override
	public UserRating findRatingByStoreIdAndCustomerName(String storeId, String name) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo", storeId))
				.must(QueryBuilders.termQuery("userName", name)));

		SearchRequest searchRequest = new SearchRequest("userrating");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<UserRating> userRatingList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			userRatingList.add(objectMapper.convertValue(hit.getSourceAsMap(), UserRating.class));
		}
		return userRatingList.get(0);

	}

	// completed

	/*
	 * @Override public UserRating findRatingByStoreId(String storeId) {
	 * System.out.println("....................... impl ................" +
	 * storeId); StringQuery stringQuery = new StringQuery(
	 * QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo",
	 * storeId)).toString()); UserRating rating =
	 * elasticsearchOperations.queryForObject(stringQuery, UserRating.class); return
	 * rating; }
	 */
	@Override
	public UserRating findRatingByStoreId(String storeId) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo", storeId)));

		SearchRequest searchRequest = new SearchRequest("userrating");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<UserRating> userRatingList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			userRatingList.add(objectMapper.convertValue(hit.getSourceAsMap(), UserRating.class));
		}
		return userRatingList.get(0);

	}
	// completed
	/*
	 * @Override public UserRating findRatingByName(String name) {
	 * System.out.println("....................... impl ................" + name);
	 * StringQuery stringQuery = new StringQuery(
	 * QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName",
	 * name)).toString()); UserRating rating =
	 * elasticsearchOperations.queryForObject(stringQuery, UserRating.class); return
	 * rating; }
	 */

	@Override
	public UserRating findRatingByName(String name) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.termQuery("userName", name));

		SearchRequest searchRequest = new SearchRequest("userrating");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<UserRating> userRatingList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			userRatingList.add(objectMapper.convertValue(hit.getSourceAsMap(), UserRating.class));
		}

		return userRatingList.get(0);
	}

	// completed
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#
	 * findReviewByStoreIdAndCustomerName (java.lang.String, java.lang.String)
	 */
	/*
	 * @Override public Review findReviewByStoreIdAndCustomerName(String storeId,
	 * String name) {
	 * 
	 * StringQuery stringQuery = new StringQuery(
	 * QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo",
	 * storeId)) .must(QueryBuilders.termQuery("userName", name)).toString());
	 * 
	 * return elasticsearchOperations.queryForObject(stringQuery, Review.class); }
	 */

	@Override
	public Review findReviewByStoreIdAndCustomerName(String storeId, String name) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo", storeId))
				.must(QueryBuilders.termQuery("userName", name)));

		SearchRequest searchRequest = new SearchRequest("review");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Review> reviewList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			reviewList.add(objectMapper.convertValue(hit.getSourceAsMap(), Review.class));
		}
		return reviewList.get(0);

	}

	// completed // is equivallent to findAllProductBySearchTerm
	/*
	 * @Override public Page<Product> findAllProductByName(String name) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchQuery("name", name)).build();
	 * return elasticsearchOperations.queryForPage(searchQuery, Product.class);
	 * 
	 * }
	 */
	// complete // add pageable
	/*
	 * @Override public Page<StockCurrent>
	 * findAllStockCurrentByProductNameStoreId(String productName, String storeId) {
	 * StringQuery stringQuery = new StringQuery(
	 * QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode", storeId))
	 * .must(QueryBuilders.termQuery("product.name", productName)).toString());
	 * 
	 * return elasticsearchOperations.queryForPage(stringQuery, StockCurrent.class);
	 * 
	 * }
	 */
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
	// completed
	/*
	 * @Override public List<Entry> findRatingCount(Pageable pageable) { SearchQuery
	 * searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
	 * .withSearchType(QUERY_THEN_FETCH).withIndices("userrating").withTypes(
	 * "userrating")
	 * .addAggregation(AggregationBuilders.terms("ratings").field("rating")).build()
	 * ;
	 * 
	 * AggregatedPage<UserRating> result =
	 * elasticsearchTemplate.queryForPage(searchQuery, UserRating.class);
	 * TermsAggregation categoryAggregation = result.getAggregation("ratings",
	 * TermsAggregation.class); return categoryAggregation.getBuckets(); }
	 */

	@Override
	public List<ResultBucket> findRatingCount(Pageable pageable) {
		List<ResultBucket> resultBucketList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("storetype");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		searchSourceBuilder.aggregation(AggregationBuilders.terms("ratings").field("rating"));

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
		Terms categoryAggregation = searchResponse.getAggregations().get("ratings");
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

	// ************************************************************************************************

	// ...........................................................................................
	/*
	 * @Override public Page<Address> findByCustomerId(String customerId, Pageable
	 * pageable) { log.info("Customer Id is " + customerId); SearchQuery searchQuery
	 * = new NativeSearchQueryBuilder().withQuery(termQuery("customerId.keyword",
	 * customerId))
	 * .withIndices("orderaddress").withTypes("orderaddress").withPageable(pageable)
	 * .build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * Address.class); }
	 */

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

	private Page<Address> getAddressSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Address> addressList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			addressList.add(objectMapper.convertValue(hit.getSourceAsMap(), Address.class));
		}

		return new PageImpl(addressList, page, response.getHits().getTotalHits());

	}

	// ......................................................................................
	// completed
	/*
	 * @Override public Order findById(Long id) {
	 * 
	 * StringQuery query = new StringQuery(termQuery("id", id).toString()); return
	 * elasticsearchOperations.queryForObject(query, Order.class);
	 * 
	 * }
	 */
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

	// completed
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreByType(java.lang.
	 * String)
	 */
	/*
	 * @Override public Page<Store> findStoreByDeliveryType(String deliveryType) {
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(termQuery("deliveryInfos.type.name.keyword",
	 * deliveryType)).build(); return
	 * elasticsearchOperations.queryForPage(searchQuery, Store.class); }
	 */
	@Override
	public Page<Store> findStoreByDeliveryType(String deliveryType, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(termQuery("deliveryInfos.type.name.keyword", deliveryType));

		SearchRequest searchRequest = generateSearchRequest("store", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		Page<Store> storePage = getStoreSearchResult(searchResponse, pageable);
		return storePage;
	}

	// complete
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreByType(java.lang.
	 * String)
	 */
	/*
	 * @Override public Page<Store> findStoreByType(String name, Pageable pageable)
	 * {
	 * 
	 * Set<Store> storeSet = new HashSet<>();
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("name", name))
	 * .withPageable(pageable).build(); List<StoreType> storeTypeList =
	 * elasticsearchOperations.queryForList(searchQuery, StoreType.class); for
	 * (StoreType storeType : storeTypeList) { storeSet.add(storeType.getStore()); }
	 * 
	 * return new PageImpl(new ArrayList<Store>(storeSet)); }
	 */
	@Override
	public Page<Store> findStoreByType(String name, Pageable pageable) {

		Set<Store> storeSet = new HashSet<>();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		searchSourceBuilder.query(termQuery("name", name));

		SearchRequest searchRequest = new SearchRequest("storetype");
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<StoreType> storeTypeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeTypeList.add(objectMapper.convertValue(hit.getSourceAsMap(), StoreType.class));
		}

		Page<Store> storePage = getStoreSearchResult(searchResponse, pageable);

		for (StoreType storeType : storeTypeList) {
			storeSet.add(storeType.getStore());
		}

		return new PageImpl(new ArrayList<Store>(storeSet), pageable, searchResponse.getHits().getTotalHits());
	}

	// completed
	/*
	 * to find category by storeId
	 */

	/*
	 * @Override public Page<Category> findCategoryByIDPcode(String iDPcode,
	 * Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("iDPcode", iDPcode))
	 * .withPageable(pageable).build(); return
	 * elasticsearchOperations.queryForPage(searchQuery, Category.class);
	 * 
	 * }
	 */
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

	// complete
	/*
	 * to find Product by storeId and categoryName
	 */
	/*
	 * @Override public Page<Product> findProductByStoreIdAndCategoryName(String
	 * userId, String categoryName, Pageable pageable) {
	 * 
	 * List<Category> categoryList = new ArrayList<>(); FetchSourceFilterBuilder
	 * sourceFilter = new FetchSourceFilterBuilder();
	 * sourceFilter.withExcludes("category");
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery(
	 * "iDPcode.keyword", userId))
	 * .must(QueryBuilders.termQuery("category.name.keyword", categoryName)))
	 * .withIndices("product").withTypes("product").withSourceFilter(sourceFilter.
	 * build()) .withPageable(pageable).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Product.class);
	 * 
	 * }
	 */
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

	// **************************************************************************************************************

	/*
	 * to find Store by typeName
	 */
	/*
	 * @Override public Page<Store> findStoreByTypeName(String name, Pageable
	 * pageable) { Set<Store> storeSet = new HashSet<>(); FetchSourceFilterBuilder
	 * sourceFilter = new FetchSourceFilterBuilder();
	 * sourceFilter.withExcludes("deliveryinfo", "type");
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery(
	 * "type.name.keyword", name)))
	 * .withIndices("deliveryinfo").withTypes("deliveryinfo").withSourceFilter(
	 * sourceFilter.build()) .withPageable(pageable).build();
	 * 
	 * List<DeliveryInfo> deliveryInfoList =
	 * elasticsearchOperations.queryForList(searchQuery, DeliveryInfo.class); for
	 * (DeliveryInfo delivery : deliveryInfoList) {
	 * storeSet.add(delivery.getStore()); } return new PageImpl(new
	 * ArrayList<Store>(storeSet)); }
	 */
	@Override
	public Page<Store> findStoreByTypeName(String name, Pageable pageable) {
		Set<Store> storeSet = new HashSet<>();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("type.name.keyword", name)));
		SearchRequest searchRequest = generateSearchRequest("deliveryinfo", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<DeliveryInfo> deliveryInfoList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			deliveryInfoList.add(objectMapper.convertValue(hit.getSourceAsMap(), DeliveryInfo.class));
		}
		for (DeliveryInfo delivery : deliveryInfoList) {
			storeSet.add(delivery.getStore());
		}
		return new PageImpl(new ArrayList<Store>(storeSet), pageable, searchResponse.getHits().getTotalHits());

	}

	/*
	 * @Override public List<StockCurrent>
	 * findStockCurrentByStoreIdAndCategoryId(String userId, Long categoryId,
	 * Pageable pageable) {
	 * 
	 * List<StockCurrent> stockCurrentList = new ArrayList<>();
	 * FetchSourceFilterBuilder sourceFilter = new FetchSourceFilterBuilder();
	 * sourceFilter.withExcludes("category");
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery(
	 * "iDPcode.keyword", userId)) .must(QueryBuilders.termQuery("category.id",
	 * categoryId)))
	 * .withIndices("product").withTypes("product").withSourceFilter(sourceFilter.
	 * build()) .withPageable(pageable).build();
	 * 
	 * List<Product> productList = elasticsearchOperations.queryForPage(searchQuery,
	 * Product.class).getContent();
	 * 
	 * for (Product product : productList) { System.out.println(
	 * "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=List of product:"
	 * + product); if ((product.isIsAuxilaryItem() == false)) {
	 * 
	 * StringQuery query = new StringQuery(termQuery("product.id",
	 * product.getId()).toString());
	 * 
	 * stockCurrentList.add(elasticsearchOperations.queryForObject(query,
	 * StockCurrent.class)); for (StockCurrent stockCurrent : stockCurrentList) {
	 * 
	 * System.out.println(
	 * "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=stockcurrentList"
	 * + stockCurrent); }
	 * 
	 * System.out.println("<<<<<<<stockCurrentSize:" + stockCurrentList.size()); } }
	 * 
	 * return stockCurrentList;
	 * 
	 * }
	 * 
	 */
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

	/*
	 * @Override public Page<Order> findOrderByCustomerId(String customerId,
	 * Pageable pageable) { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("customerId", customerId))
	 * .withPageable(pageable).build();
	 * 
	 * Page<Order> orderPage = elasticsearchOperations.queryForPage(searchQuery,
	 * Order.class); orderPage.forEach(order -> {
	 * 
	 * order.setOrderLines(new
	 * HashSet<OrderLine>(findOrderLinesByOrderId(order.getId())));
	 * 
	 * });
	 * 
	 * return orderPage;
	 * 
	 * }
	 * 
	 * 
	 */
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

		Page<Order> orderPage = getOrderSearchResult(searchResponse, pageable);
		orderPage.forEach(order -> {

			order.setOrderLines(new HashSet<OrderLine>(findOrderLinesByOrderId(order.getId())));

		});

		return orderPage;

	}

	// ******************************************************************************************************
	// complete
	/*
	 * @Override public List<OrderLine> findOrderLinesByOrderId(Long orderId) {
	 * StringQuery searchQuery = new StringQuery(termQuery("order.id",
	 * orderId).toString()); return
	 * elasticsearchOperations.queryForList(searchQuery, OrderLine.class); }
	 */
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

	// completed
	/*
	 * @Override public Page<Customer> findAllCustomersWithoutSearch(Pageable
	 * pageable) { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
	 * .build(); return elasticsearchOperations.queryForPage(searchQuery,
	 * Customer.class); }
	 */
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
	// complete
	/*
	 * @Override public Page<Store> findStoreBySearchTerm(String searchTerm,
	 * Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(matchQuery("name",
	 * searchTerm).prefixLength(3)).withPageable(pageable).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Store.class); }
	 */

	@Override
	public Page<Store> findStoreBySearchTerm(String searchTerm, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		searchSourceBuilder.query(matchQuery("name", searchTerm).prefixLength(3));

		SearchRequest searchRequest = generateSearchRequest("store", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStoreSearchResult(searchResponse, pageable);
	}

	// complete
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findOrderByOrderId(java.lang.
	 * String)
	 */
	/*
	 * @Override public Order findOrderByOrderId(String orderId) { StringQuery
	 * stringQuery = new StringQuery(termQuery("orderId.keyword",
	 * orderId).toString()); return
	 * elasticsearchOperations.queryForObject(stringQuery, Order.class); }
	 */
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

	// ***********************************************************************************************
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findAllDeliveryInfosByStoreId(
	 * java. lang .String)
	 */
	/*
	 * @Override public Page<Type> findAllDeliveryTypesByStoreId(Long storeId,
	 * Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("store.id", storeId))
	 * .withPageable(pageable).build();
	 * 
	 * Page<DeliveryInfo> deliveryinfos =
	 * elasticsearchOperations.queryForPage(searchQuery, DeliveryInfo.class);
	 * 
	 * List<Type> types = new ArrayList<Type>();
	 * 
	 * deliveryinfos.forEach(deliveryInfo -> { types.add(deliveryInfo.getType());
	 * 
	 * });
	 * 
	 * return new PageImpl(types);
	 * 
	 * }
	 */
	@Override
	public Page<Type> findAllDeliveryTypesByStoreId(Long storeId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("store.id", storeId));

		SearchRequest searchRequest = generateSearchRequest("deliveryinfo", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<DeliveryInfo> deliveryInfoList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			deliveryInfoList.add(objectMapper.convertValue(hit.getSourceAsMap(), DeliveryInfo.class));
		}
		Page<DeliveryInfo> deliveryinfos = getDeliveryInfoSearchResult(searchResponse, pageable);
		List<Type> types = new ArrayList<Type>();

		deliveryinfos.forEach(deliveryInfo -> {
			types.add(deliveryInfo.getType());

		});
		return new PageImpl(types);

	}

	// ***********************************************************************************************************
	// complete
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findDeliveryInfoById(java.lang
	 * .Long)
	 */
	/*
	 * @Override public DeliveryInfo findDeliveryInfoById(Long id) {
	 * 
	 * StringQuery searchQuery = new StringQuery(termQuery("id", id).toString());
	 * 
	 * return elasticsearchOperations.queryForObject(searchQuery,
	 * DeliveryInfo.class);
	 * 
	 * }
	 */
	@Override
	public DeliveryInfo findDeliveryInfoById(Long id) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		searchSourceBuilder.query(termQuery("id", id));

		SearchRequest searchRequest = new SearchRequest("deliveryinfo");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<DeliveryInfo> deliveryInfoList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			deliveryInfoList.add(objectMapper.convertValue(hit.getSourceAsMap(), DeliveryInfo.class));
		}
		return deliveryInfoList.get(0);

	}

	// complete
	/*
	 * @Override public Product findProductById(Long id) {
	 * log.info("..............id  impl ............." + id);
	 * 
	 * StringQuery searchQuery = new StringQuery(termQuery("id", id).toString());
	 * 
	 * return elasticsearchOperations.queryForObject(searchQuery, Product.class);
	 * 
	 * }
	 */
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

	// *****************************************************************************************
	// completed
	/*
	 * @Override public Page<Store> findStoreByRating() {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(rangeQuery("totalRating").gte(1).lte(5))
	 * .withSort(SortBuilders.fieldSort("totalRating").order(SortOrder.DESC)).build(
	 * );
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Store.class); }
	 */

	@Override
	public Page<Store> findStoreByRating(Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(rangeQuery("totalRating").gte(1).lte(5));
		searchSourceBuilder.sort(new FieldSortBuilder("totalRating").order(SortOrder.DESC));

		SearchRequest searchRequest = generateSearchRequest("store", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}

		return getStoreSearchResult(searchResponse, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findAndSortProductByPrice(java
	 * .lang.Double, java.lang.Double)
	 */
	/*
	 * @Override public Page<StockCurrent> findAndSortProductByPrice(Double from,
	 * Double to) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(rangeQuery("sellPrice").gte(from).lte(to
	 * ))
	 * .withSort(SortBuilders.fieldSort("sellPrice").order(SortOrder.DESC)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);
	 * 
	 * }
	 */
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

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findDeliveryInfoByStoreId(java
	 * .lang.String)
	 */
	/*
	 * @Override public Page<DeliveryInfo> findDeliveryInfoByStoreId(String storeId)
	 * { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("store.regNo",
	 * storeId)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, DeliveryInfo.class);
	 * }
	 */

	@Override
	public Page<DeliveryInfo> findDeliveryInfoByStoreId(String storeId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("store.regNo", storeId));

		SearchRequest searchRequest = generateSearchRequest("deliveryinfo", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getDeliveryInfoSearchResult(searchResponse, pageable);

	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/*
	 * public Page<Store> headerSearch(String searchTerm, Pageable pageable) {
	 * Set<Store> storeSet = new HashSet<Store>(); Set<HeaderSearch> values = new
	 * HashSet<HeaderSearch>();
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchQuery("name", searchTerm))
	 * .withPageable(pageable).build();
	 * 
	 * elasticsearchTemplate.query(searchQuery, new
	 * JestResultsExtractor<Set<HeaderSearch>>() {
	 * 
	 * @Override public Set<HeaderSearch> extract(SearchResult response) {
	 * 
	 * for (SearchResult.Hit<JsonObject, Void> searchHit :
	 * response.getHits(JsonObject.class)) { HeaderSearch result = new
	 * HeaderSearch();
	 * 
	 * if (searchHit.index.equals("store")) {
	 * result.setStoreNo(searchHit.source.get("regNo").getAsString());
	 * System.out.println("************Store*****************" +
	 * result.getStoreNo()); } else {
	 * result.setStoreNo(searchHit.source.get("iDPcode").getAsString()); }
	 * 
	 * values.add(result);
	 * 
	 * } return values; } });
	 * 
	 * for (HeaderSearch r : values) { StringQuery stringQuery = new
	 * StringQuery(termQuery("regNo.keyword", r.getStoreNo()).toString());
	 * storeSet.add(elasticsearchOperations.queryForObject(stringQuery,
	 * Store.class)); } List<Store> storeList = new ArrayList<>();
	 * storeList.addAll(storeSet);
	 * 
	 * return new PageImpl(storeList);
	 * 
	 * }
	 */
	public Page<Store> headerSearch(String searchTerm, Pageable pageable) throws IOException {

		Set<Store> storeSet = new HashSet<Store>();
		Set<HeaderSearch> values = new HashSet<HeaderSearch>();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("name", searchTerm));

		SearchRequest searchRequest = generateSearchRequest1(pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		for (SearchHit hit : searchHit) {
			String h = hit.getSourceAsString();
			HeaderSearch result = new HeaderSearch();
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			if (hit.getIndex().equals("store")) {
				result.setStoreNo((String) sourceAsMap.get("regNo"));
			} else {

				result.setStoreNo((String) sourceAsMap.get("iDPcode"));
			}

			values.add(result);
		}

		return findStoresByRegNoList(values, pageable);

	}

	private SearchRequest generateSearchRequest1(Integer totalElement, Integer pageNumber,
			SearchSourceBuilder sourceBuilder) {
		SearchRequest searchRequest = new SearchRequest("store", "product", "category");

		int offset = 0;
		int totalElements = 0;

		if (pageNumber == 0) {
			offset = 0;
			totalElements = totalElement;

		} else {

			offset = totalElements;

			totalElements = totalElement + (pageNumber * totalElement);

		}
		sourceBuilder.from(offset);
		sourceBuilder.size(totalElements);

		searchRequest.source(sourceBuilder);
		return searchRequest;
	}

	public Page<Store> findStoresByRegNoList(Set<HeaderSearch> values, Pageable pageable) throws IOException {
		Set<Store> storeSet = new HashSet<Store>();
		SearchResponse searchResponse = null;

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		for (HeaderSearch r : values) {
			searchSourceBuilder.query(termQuery("regNo.keyword", r.getStoreNo()));

			SearchRequest searchRequest = new SearchRequest("store");

			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

			SearchHit[] searchHit = searchResponse.getHits().getHits();

			for (SearchHit hit : searchHit) {
				storeSet.add(objectMapper.convertValue(hit.getSourceAsMap(), Store.class));
			}

		}
		List<Store> storeList = new ArrayList<>();
		storeList.addAll(storeSet);
		return new PageImpl(storeList, pageable, searchResponse.getHits().getTotalHits());

	}

	@Override
	public Page<Cart> findByLocationNear(/*
											 * Double lat,Double lon, Double distance ,
											 */ Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(
				QueryBuilders.geoDistanceQuery("location").point(10.767, 76.486).distance(5, DistanceUnit.KILOMETERS));

		SearchRequest searchRequest = generateSearchRequest("cart", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getCartSearchResult(searchResponse, pageable);

		/* storeSearchRepository.findByLocationNear(point,distance,pageable); */ }

	/*
	 * private CriteriaQuery getGeoQuery(Point point, Distance distance) { return
	 * new CriteriaQuery(new Criteria("location").within(point, distance)); }
	 */

	/*
	 * @Override public Page<Store> findStoreByLocationName(String locationName) {
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(matchQuery("locationName", locationName).prefixLength(3)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Store.class); }
	 */

	@Override
	public Page<Store> findStoreByLocationName(String locationName, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("locationName", locationName).prefixLength(3));

		SearchRequest searchRequest = generateSearchRequest("store", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return getStoreSearchResult(searchResponse, pageable);
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/*
	 * @Override public Page<Store> findAndSortStoreByMinAount(Pageable pageable) {
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery())
	 * .withSort(SortBuilders.fieldSort("minAmount").order(SortOrder.DESC)).
	 * withPageable(pageable).build(); return
	 * elasticsearchOperations.queryForPage(searchQuery, Store.class);
	 * 
	 * }
	 */

	@Override
	public Page<Store> findAndSortStoreByMinAount(Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchAllQuery());
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		SearchRequest searchRequest = generateSearchRequest("store", pageable.getPageSize(), pageable.getPageNumber(),
				searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStoreSearchResult(searchResponse, pageable);

	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreTypeByStoreId(java.
	 * lang.String, org.springframework.data.domain.Pageable)
	 */
	/*
	 * @Override public Page<StoreType> findStoreTypeByStoreId(String storeId,
	 * Pageable pageable) {
	 * 
	 * SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("store.regNo", storeId))
	 * .withPageable(pageable).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, StoreType.class); }
	 */
	@Override
	public Page<StoreType> findStoreTypeByStoreId(String storeId, Pageable pageable) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String[] includeFields = new String[] { "id", "name" };
		String[] excludeFields = new String[] { "store.*" };
		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		searchSourceBuilder.query(termQuery("store.regNo", storeId));

		SearchRequest searchRequest = generateSearchRequest("storetype", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
		return getStoreTypeSearchResult(searchResponse, pageable);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findProductsByCategoryName(
	 * java.lang.String)
	 */
	/*
	 * @Override public Page<Product> findProductsByCategoryName(String name) {
	 * 
	 * List<Product> products = findAllProducts();
	 * 
	 * List<Product> categoryNameBasedProduct = new ArrayList<Product>();
	 * 
	 * products.forEach(p -> {
	 * 
	 * if (p.getCategory().getName().equals(name)) {
	 * 
	 * categoryNameBasedProduct.add(p); }
	 * 
	 * });
	 * 
	 * return new PageImpl(categoryNameBasedProduct); }
	 */
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
	/*
	 * @Override public Long findOrderCountByCustomerId(String customerId) { Long
	 * count = 0l; SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(matchAllQuery())
	 * .withSearchType(QUERY_THEN_FETCH)
	 * 
	 * .withIndices("order").withTypes("order")
	 * .addAggregation(AggregationBuilders.terms("customerorder").field(
	 * "customerId.keyword")).build();
	 * 
	 * AggregatedPage<Order> result =
	 * elasticsearchTemplate.queryForPage(searchQuery, Order.class);
	 * TermsAggregation categoryAggregation = result.getAggregation("customerorder",
	 * TermsAggregation.class); count =
	 * categoryAggregation.getBuckets().stream().filter(entry ->
	 * entry.getKey().equals(customerId)).findFirst() .get().getCount();
	 * 
	 * return count;
	 * 
	 * }
	 */

	/*
	 * public StoreSettings getStoreSettings(String IDPCode) {
	 * 
	 * StringQuery searchQuery = new StringQuery(termQuery("regNo",
	 * IDPCode).toString()); Store store =
	 * elasticsearchOperations.queryForObject(searchQuery, Store.class); return
	 * store.getStoreSettings(); }
	 */
	public StoreSettings getStoreSettings(String IDPCode) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("regNo", IDPCode));

		SearchRequest searchRequest = new SearchRequest("store");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Store> storeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Store.class));
		}
		return storeList.get(0).getStoreSettings();
	}

	/*
	 * public StoreAddress getStoreAddress(String IDPCode) {
	 * 
	 * StringQuery searchQuery = new StringQuery(termQuery("regNo",
	 * IDPCode).toString()); Store store =
	 * elasticsearchOperations.queryForObject(searchQuery, Store.class); return
	 * store.getStoreAddress(); }
	 */
	public StoreAddress getStoreAddress(String iDPCode) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("regNo", iDPCode));

		SearchRequest searchRequest = new SearchRequest("store");
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Store> storeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Store.class));
		}
		return storeList.get(0).getStoreAddress();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findAllAuxilariesByProductId(
	 * java.lang.Long)
	 */
	/*
	 * @Override public Page<AuxilaryLineItem> findAllAuxilariesByProductId(Long
	 * productId) { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("product.id",
	 * productId)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery,
	 * AuxilaryLineItem.class);
	 * 
	 * }
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findStockCurrentByCategoryName
	 * (java.lang.String)
	 */
	/*
	 * @Override public Page<StockCurrent>
	 * findStockCurrentByCategoryNameAndStoreId(String categoryName, String storeId)
	 * { SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(
	 * QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(
	 * "product.category.name.keyword", categoryName))
	 * .must(QueryBuilders.matchQuery("product.iDPcode", storeId))) .build(); return
	 * elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);
	 * 
	 * }
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findOrderByStatusName(java.lang.
	 * String)
	 */
	/*
	 * @Override public Page<Order> findOrderByStatusName(String statusName) {
	 * 
	 * SearchQuery searchQuery = new NativeSearchQueryBuilder()
	 * .withQuery(matchQuery("status.name.keyword", statusName)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Order.class); }
	 */
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

		return getOrderSearchResult(searchResponse, pageable);
	}

	private Page<Order> getOrderSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Order> orderList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			orderList.add(objectMapper.convertValue(hit.getSourceAsMap(), Order.class));
		}

		return new PageImpl(orderList, page, response.getHits().getTotalHits());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findDeliveryinfobydatebetween(java
	 * .time.Instant, java.time.Instant)
	 */
	/*
	 * @Override public Page<Order> findOrderByDatebetweenAndStoreId(Instant from,
	 * Instant to, String storeId) { // ......... SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
	 * .must(termQuery("storeId",
	 * storeId)).must(rangeQuery("date").gte(from).lte(to))).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery, Order.class); }
	 */
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

		return getOrderSearchResult(searchResponse, pageable);

	}

	/*
	 * @Override public Page<ComboLineItem> findAllCombosByProductId(Long productId)
	 * { SearchQuery searchQuery = new
	 * NativeSearchQueryBuilder().withQuery(termQuery("product.id",
	 * productId)).build();
	 * 
	 * return elasticsearchOperations.queryForPage(searchQuery,
	 * ComboLineItem.class);
	 * 
	 * }
	 */
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

	/*
	 * @Override public Discount findDiscountByProductId(Long productId) {
	 * StringQuery searchQuery = new StringQuery(termQuery("id",
	 * productId).toString()); Product product =
	 * elasticsearchOperations.queryForObject(searchQuery, Product.class); return
	 * product.getDiscount(); }
	 */
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

	@Override
	public Page<Store> facetSearchByStoreTypeName(List<String> storeTypeNames, Pageable pageable) {
		List<StoreType> storeTypeList = null;
		Set<Store> storeSet = new HashSet<>();

		String[] includeFields = new String[] { "closingTime", "contactNo", "email", "id", "image", "imageContentType",
				"info", "location", "locationName", "maxDeliveryTime", "minAmount", "name", "openingTime", "regNo" };
		String[] excludeFields = new String[] { "storetype.*", "storesettings.*", "storeaddress.*" };
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.fetchSource(includeFields, excludeFields);
		for (String term : storeTypeNames) {
			searchSourceBuilder.query(QueryBuilders.termQuery("name.keyword", term));
			SearchRequest searchRequest = new SearchRequest("storetype");
			searchRequest.source(searchSourceBuilder);
			SearchResponse searchResponse = null;
			try {
				searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			} catch (IOException e) { 
				e.printStackTrace();
			}
			SearchHit[] searchHit = searchResponse.getHits().getHits();


			for (SearchHit hit : searchHit) {
				storeTypeList.add(objectMapper.convertValue(hit.getSourceAsMap(), StoreType.class));
				storeSet.add(objectMapper.convertValue(hit.getSourceAsMap(), StoreType.class).getStore());
			}

		}
		return new PageImpl(new ArrayList<Store>(storeSet));

	}

}
