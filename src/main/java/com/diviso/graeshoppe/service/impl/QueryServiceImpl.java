package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.action.search.SearchType.QUERY_THEN_FETCH;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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
//import com.diviso.graeshoppe.repository.search.StoreSearchRepository;
/*import com.diviso.graeshoppe.client.product.domain.Product;
import com.diviso.graeshoppe.domain.Result;*/
import com.diviso.graeshoppe.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;

/*import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;*/

@Service
public class QueryServiceImpl implements QueryService {

	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public QueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

/*	@Override
	public Page<Product> findAllProductBySearchTerm(String searchTerm, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchQuery("name", searchTerm).prefixLength(2)).build();

		return elasticsearchOperations.queryForPage(searchQuery, Product.class);

	}
	
	*/
/*	
	public List<Product> findAllProductBySearchTerm(String searchTerm, Pageable pageable) {
		System.out.println("getPageNumber#################################"+pageable.getPageNumber());
		System.out.println("getPageSize******************************"+pageable.getPageSize());
		
		SearchRequest searchRequest = new SearchRequest("product");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchQuery("name", searchTerm));
		
		//searchRequest.source(searchSourceBuilder);
		generateSearchRequest("product",pageable.getPageSize(),pageable.getPageNumber(),searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getSearchResult(searchResponse);

	}
*/
	private SearchRequest generateSearchRequest(String indexName,Integer totalElement, Integer pageNumber, SearchSourceBuilder sourceBuilder) {
	    SearchRequest searchRequest = new SearchRequest(indexName);
	 
	   // int  offset =  pageNumber==0 ? totalElements:totalElements+(pageNumber *totalElements);
	   
	    int  offset = 0;
	    int totalElements=0;
	    
	    if(pageNumber==0) {
	    	offset=0;
	    	totalElements=totalElement;
	    }
	    else {
	    	offset=totalElements;
	    	totalElements=totalElement+(pageNumber *totalElement);
	    }
	    System.out.println("offffffffffffffset"+offset);
	    sourceBuilder.from(offset);
	    sourceBuilder.size(totalElements);
	
	    searchRequest.source(sourceBuilder);
	    return searchRequest;
	}

	public List<Product> findAllProduct(Pageable pageable) {
		SearchRequest searchRequest = new SearchRequest("product");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		String[] includeFields = new String[] {"product.*"};
	    String[] excludeFields = new String[] {"category.*"};
	    searchSourceBuilder.fetchSource(includeFields, excludeFields);
	  //searchSourceBuilder.fetchSource(true);*/
		searchSourceBuilder.query(matchAllQuery());
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(20);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		//SearchRequest searchRequest =	generateSearchRequest("product",pageable.getPageSize(),pageable.getPageNumber(),searchSourceBuilder);
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	return getSearchResult(searchResponse,pageable);
	}
	
	private List<Product> getSearchResult(SearchResponse response,Pageable page) {

		
		SearchHit[] searchHit = response.getHits().getHits();

		List<Product> productList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			productList.add(objectMapper.convertValue(hit.getSourceAsMap(), Product.class));
		}

		return productList;
		
		
		//return new PageImpl(productList,page,response.getHits().getTotalHits());/* setPage( productList,pageable,response.getHits().getTotalHits());*/
				
		
	}
	
/*	private   Page<Product>  setPage(List<Product> contentList,Pageable page, Long size) {
		
		Page<Product> page1=new PageImpl<T>(contentList,page,size);
		return page1;
	
	}*/
	
	
	
	
	
	
	

	/*@Override
	public Page<Product> findProductByCategoryId(Long categoryId, String userId, Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("category.id", categoryId))
						.must(QueryBuilders.matchQuery("iDPcode", userId)))
				.build();
		return elasticsearchOperations.queryForPage(searchQuery, Product.class);
	}
*/
	/*@Override
	public Customer findCustomerByReference(String reference) {

		StringQuery searchQuery = new StringQuery(termQuery("reference", reference).toString());
		return elasticsearchOperations.queryForObject(searchQuery, Customer.class);
	}*/

	/*@Override
	public Page<StockCurrent> findStockCurrentByProductId(Long productId, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("product.id", productId)).build();
		return elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);
	}
*/
	/*@Override
	public Page<StockCurrent> findStockCurrentByProductName(String name, String storeId, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("product.name", name))
						.must(QueryBuilders.matchQuery("product.iDPcode", storeId)))
				.build();

		return new PageImpl(elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class).stream()
				.filter(stockcurrent -> stockcurrent.getProduct().isIsAuxilaryItem() == false)
				.collect(Collectors.toList()));
	}*/



	/*@Override
	public Page<Review> findAllReviews(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
				.build();
		return elasticsearchOperations.queryForPage(searchQuery, Review.class);
	}*/

	/*@Override
	public Page<UserRating> findAllUserRatings(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
				.build();
		return elasticsearchOperations.queryForPage(searchQuery, UserRating.class);
	}*/

	/*@Override
	public Page<Store> findAllStores(Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
				.build();
		Page<Store> storePage = elasticsearchOperations.queryForPage(searchQuery, Store.class);

		storePage.forEach(store -> {
			List<UserRating> userRating = findUserRatingByRegNo(store.getRegNo()).getContent();
			store.setUserRatings(new HashSet<UserRating>(userRating));
		});

		return storePage;
	}*/

	/*@Override
	public Page<Product> findAllProductsByStoreId(String storeId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("iDPcode", storeId)).build();
		return elasticsearchOperations.queryForPage(searchQuery, Product.class);
	}*/

	/*@Override
	public List<Product> findAllProducts() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build();
		return elasticsearchOperations.queryForList(searchQuery, Product.class);
	}
*/
	/*@Override
	public Store findStoreByRegNo(String regNo) {
		StringQuery stringQuery = new StringQuery(termQuery("regNo", regNo).toString());
		return elasticsearchOperations.queryForObject(stringQuery, Store.class);

	}*/

	/*@Override
	public Long findReviewCountByStoreId(String storeId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("store.regNo", storeId)).build();
		return elasticsearchOperations.count(searchQuery,Review.class);
		
	}
	*/
	/*@Override
	public Page<Review> findReviewByStoreId(String storeId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("store.regNo", storeId)).build();
		
		return elasticsearchOperations.queryForPage(searchQuery, Review.class);
	}*/

	/*@Override
	public Page<StockCurrent> findStockCurrentByStoreId(String iDPcode) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("iDPcode", iDPcode)).build();

		Page<StockCurrent> stockCurrentPage = elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);

		List<StockCurrent> notAuxStockCurrentProducts = new ArrayList<StockCurrent>();

		stockCurrentPage.forEach(s -> {

			if (s.getProduct().isIsAuxilaryItem() == false) {
				notAuxStockCurrentProducts.add(s);
			}
		});

		return new PageImpl(notAuxStockCurrentProducts);
	}*/

	/*@Override
	public Page<UserRating> findUserRatingByRegNo(String regNo) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("store.regNo", regNo)).build();
		return elasticsearchOperations.queryForPage(searchQuery, UserRating.class);
	}*/

	/*@Override
	public List<Entry> findCategoryAndCount(Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
				.withSearchType(QUERY_THEN_FETCH)

				.withIndices("product").withTypes("product")

				.addAggregation(AggregationBuilders.terms("totalcategories").field("category.name.keyword"))
				.withPageable(pageable).build();

		AggregatedPage<Product> result = elasticsearchTemplate.queryForPage(searchQuery, Product.class);

		TermsAggregation categoryAggregation = result.getAggregation("totalcategories", TermsAggregation.class);

		return categoryAggregation.getBuckets();

	}
*/
	/*@Override
	public List<Entry> findCategoryAndCountByStoreId(String storeId,Pageable pageable) {
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
				
				.withSearchType(QUERY_THEN_FETCH).withIndices("product").withTypes("product")
				
				.addAggregation(AggregationBuilders.terms("totalcategories").field("category.name.keyword")
						
						.order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.aggregation("avgPrice",
								
								true))
						.subAggregation(AggregationBuilders.avg("avgPrice").field("sellingPrice"))
						
						.subAggregation(AggregationBuilders.terms("store").field("iDPcode.keyword")))
				.build();

		AggregatedPage<Product> result = elasticsearchTemplate.queryForPage(searchQuery, Product.class);

		TermsAggregation orderAgg = result.getAggregation("totalcategories", TermsAggregation.class);

		List<Entry> storeBasedEntry = new ArrayList<Entry>();

		
		orderAgg.getBuckets().forEach(bucket -> {
			
			int i = 0;
			double averagePrice = bucket.getAvgAggregation("avgPrice").getAvg();

			System.out.println(String.format("Key: %s, Doc count: %d, Average Price: %f", bucket.getKey(),
					bucket.getCount(), averagePrice));

			System.out.println("SSSSSSSSSSSSSSSSSS"
					+ bucket.getAggregation("store", TermsAggregation.class).getBuckets().get(i).getKeyAsString());

			String storeName = bucket.getAggregation("store", TermsAggregation.class).getBuckets().get(i)
					.getKeyAsString();
			
			if (storeName.equals(storeId)) {
				
				Entry storeEntry = bucket;
				
				storeBasedEntry.add(storeEntry);
			}
			i++;
			System.out.println(
					"SSSSSSSSSSSSSSSSSS" + bucket.getAggregation("store", TermsAggregation.class).getBuckets().size());
		});
		
	return storeBasedEntry;
	}*/
/*	public List<Entry> findStoreTypeAndCount(Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())

				.withSearchType(QUERY_THEN_FETCH).withIndices("storetype").withTypes("storetype")

				.addAggregation(AggregationBuilders.terms("totalstoretype").field("name.keyword"))
				.withPageable(pageable).build();

		AggregatedPage<StoreType> result = elasticsearchTemplate.queryForPage(searchQuery, StoreType.class);

		TermsAggregation categoryAggregation = result.getAggregation("totalstoretype", TermsAggregation.class);

		return categoryAggregation.getBuckets();
	}*/

	/*@Override
	public UserRating findRatingByStoreIdAndCustomerName(String storeId, String name) {
		System.out.println("....................... impl ................" + storeId + "      " + name);
		StringQuery stringQuery = new StringQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo", storeId))
						.must(QueryBuilders.termQuery("userName", name)).toString());
		UserRating rating = elasticsearchOperations.queryForObject(stringQuery, UserRating.class);
		return rating;
	}*/

/*	@Override
	public UserRating findRatingByStoreId(String storeId) {
		System.out.println("....................... impl ................" + storeId);
		StringQuery stringQuery = new StringQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo", storeId)).toString());
		UserRating rating = elasticsearchOperations.queryForObject(stringQuery, UserRating.class);
		return rating;
	}
*/
	/*@Override
	public UserRating findRatingByName(String name) {
		System.out.println("....................... impl ................" + name);
		StringQuery stringQuery = new StringQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", name)).toString());
		UserRating rating = elasticsearchOperations.queryForObject(stringQuery, UserRating.class);
		return rating;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#
	 * findReviewByStoreIdAndCustomerName (java.lang.String, java.lang.String)
	 */
	/*@Override
	public Review findReviewByStoreIdAndCustomerName(String storeId, String name) {

		StringQuery stringQuery = new StringQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo", storeId))
						.must(QueryBuilders.termQuery("userName", name)).toString());

		return elasticsearchOperations.queryForObject(stringQuery, Review.class);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findAllProductByName(java.
	 * lang. String)
	 */
	/*@Override
	public Page<Product> findAllProductByName(String name) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name", name)).build();
		return elasticsearchOperations.queryForPage(searchQuery, Product.class);

	}*/

	/*@Override
	public Page<StockCurrent> findAllStockCurrentByProductNameStoreId(String productName, String storeId) {
		StringQuery stringQuery = new StringQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode", storeId))
						.must(QueryBuilders.termQuery("product.name", productName)).toString());

		return elasticsearchOperations.queryForPage(stringQuery, StockCurrent.class);

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findRatingCount(org.
	 * springframework.data.domain.Pageable)
	 */
	/*@Override
	public List<Entry> findRatingCount(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
				.withSearchType(QUERY_THEN_FETCH).withIndices("userrating").withTypes("userrating")
				.addAggregation(AggregationBuilders.terms("ratings").field("rating")).build();

		AggregatedPage<UserRating> result = elasticsearchTemplate.queryForPage(searchQuery, UserRating.class);
		TermsAggregation categoryAggregation = result.getAggregation("ratings", TermsAggregation.class);
		return categoryAggregation.getBuckets();
	}*/

	/*@Override
	public Page<Address> findByCustomerId(String customerId, Pageable pageable) {
		log.info("Customer Id is " + customerId);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("customerId.keyword", customerId))
				.withIndices("orderaddress").withTypes("orderaddress").withPageable(pageable).build();
		return elasticsearchOperations.queryForPage(searchQuery, Address.class);
	}*/

	/*@Override
	public Order findById(Long id) {

		StringQuery query = new StringQuery(termQuery("id", id).toString());
		return elasticsearchOperations.queryForObject(query, Order.class);

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreByType(java.lang.
	 * String)
	 */
	/*@Override
	public Page<Store> findStoreByDeliveryType(String deliveryType) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(termQuery("deliveryInfos.type.name.keyword", deliveryType)).build();
		return elasticsearchOperations.queryForPage(searchQuery, Store.class);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreByType(java.lang.
	 * String)
	 */
	/*@Override
	public Page<Store> findStoreByType(String name, Pageable pageable) {

		Set<Store> storeSet = new HashSet<>();

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("name", name))
				.withPageable(pageable).build();
		List<StoreType> storeTypeList = elasticsearchOperations.queryForList(searchQuery, StoreType.class);
		for (StoreType storeType : storeTypeList) {
			storeSet.add(storeType.getStore());
		}

		return new PageImpl(new ArrayList<Store>(storeSet));
	}
*/
	/*
	 * to find category by storeId
	 */

	/*@Override
	public Page<Category> findCategoryByIDPcode(String iDPcode, Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("iDPcode", iDPcode))
				.withPageable(pageable).build();
		return elasticsearchOperations.queryForPage(searchQuery, Category.class);

	}*/

	/*
	 * to find Product by storeId and categoryName
	 */
/*	@Override
	public Page<Product> findProductByStoreIdAndCategoryName(String userId, String categoryName, Pageable pageable) {

		List<Category> categoryList = new ArrayList<>();
		FetchSourceFilterBuilder sourceFilter = new FetchSourceFilterBuilder();
		sourceFilter.withExcludes("category");

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode.keyword", userId))
						.must(QueryBuilders.termQuery("category.name.keyword", categoryName)))
				.withIndices("product").withTypes("product").withSourceFilter(sourceFilter.build())
				.withPageable(pageable).build();

		return elasticsearchOperations.queryForPage(searchQuery, Product.class);

	}*/

	/*
	 * to find Store by typeName
	 */
/*	@Override
	public Page<Store> findStoreByTypeName(String name, Pageable pageable) {
		Set<Store> storeSet = new HashSet<>();
		FetchSourceFilterBuilder sourceFilter = new FetchSourceFilterBuilder();
		sourceFilter.withExcludes("deliveryinfo", "type");

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("type.name.keyword", name)))
				.withIndices("deliveryinfo").withTypes("deliveryinfo").withSourceFilter(sourceFilter.build())
				.withPageable(pageable).build();

		List<DeliveryInfo> deliveryInfoList = elasticsearchOperations.queryForList(searchQuery, DeliveryInfo.class);
		for (DeliveryInfo delivery : deliveryInfoList) {
			storeSet.add(delivery.getStore());
		}
		return new PageImpl(new ArrayList<Store>(storeSet));
	}*/

	/*@Override
	public List<StockCurrent> findStockCurrentByStoreIdAndCategoryId(String userId, Long categoryId,
			Pageable pageable) {

		List<StockCurrent> stockCurrentList = new ArrayList<>();
		FetchSourceFilterBuilder sourceFilter = new FetchSourceFilterBuilder();
		sourceFilter.withExcludes("category");

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("iDPcode.keyword", userId))
						.must(QueryBuilders.termQuery("category.id", categoryId)))
				.withIndices("product").withTypes("product").withSourceFilter(sourceFilter.build())
				.withPageable(pageable).build();

		List<Product> productList = elasticsearchOperations.queryForPage(searchQuery, Product.class).getContent();

		for (Product product : productList) {
			System.out.println(
					"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=List of product:" + product);
			if ((product.isIsAuxilaryItem() == false)) {

				StringQuery query = new StringQuery(termQuery("product.id", product.getId()).toString());

				stockCurrentList.add(elasticsearchOperations.queryForObject(query, StockCurrent.class));
				for (StockCurrent stockCurrent : stockCurrentList) {

					System.out.println(
							"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=stockcurrentList"
									+ stockCurrent);
				}

				System.out.println("<<<<<<<stockCurrentSize:" + stockCurrentList.size());
			}
		}

		return stockCurrentList;

	}*/

	/*@Override
	public Page<Order> findOrderByCustomerId(String customerId, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("customerId", customerId))
				.withPageable(pageable).build();

		Page<Order> orderPage = elasticsearchOperations.queryForPage(searchQuery, Order.class);
		orderPage.forEach(order -> {

			order.setOrderLines(new HashSet<OrderLine>(findOrderLinesByOrderId(order.getId())));

		});

		return orderPage;

	}*/

/*	@Override
	public List<OrderLine> findOrderLinesByOrderId(Long orderId) {
		StringQuery searchQuery = new StringQuery(termQuery("order.id", orderId).toString());
		return elasticsearchOperations.queryForList(searchQuery, OrderLine.class);
	}
*/
	/*@Override
	public Page<Customer> findAllCustomersWithoutSearch(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withPageable(pageable)
				.build();
		return elasticsearchOperations.queryForPage(searchQuery, Customer.class);
	}*/

	/*@Override
	public Page<Store> findStoreBySearchTerm(String searchTerm, Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchQuery("name", searchTerm).prefixLength(3)).withPageable(pageable).build();

		return elasticsearchOperations.queryForPage(searchQuery, Store.class);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findOrderByOrderId(java.lang.
	 * String)
	 */
/*	@Override
	public Order findOrderByOrderId(String orderId) {
		StringQuery stringQuery = new StringQuery(termQuery("orderId.keyword", orderId).toString());
		return elasticsearchOperations.queryForObject(stringQuery, Order.class);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findAllDeliveryInfosByStoreId(
	 * java. lang .String)
	 */
	/*@Override
	public Page<Type> findAllDeliveryTypesByStoreId(Long storeId, Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("store.id", storeId))
				.withPageable(pageable).build();

		Page<DeliveryInfo> deliveryinfos = elasticsearchOperations.queryForPage(searchQuery, DeliveryInfo.class);

		List<Type> types = new ArrayList<Type>();

		deliveryinfos.forEach(deliveryInfo -> {
			types.add(deliveryInfo.getType());

		});

		return new PageImpl(types);

	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findDeliveryInfoById(java.lang
	 * .Long)
	 */
/*	@Override
	public DeliveryInfo findDeliveryInfoById(Long id) {

		StringQuery searchQuery = new StringQuery(termQuery("id", id).toString());

		return elasticsearchOperations.queryForObject(searchQuery, DeliveryInfo.class);

	}*/

	/*@Override
	public Product findProductById(Long id) {
		log.info("..............id  impl ............." + id);

		StringQuery searchQuery = new StringQuery(termQuery("id", id).toString());

		return elasticsearchOperations.queryForObject(searchQuery, Product.class);

	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreByRating(java.lang.
	 * Double)
	 */
/*	@Override
	public Page<Store> findStoreByRating() {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(rangeQuery("totalRating").gte(1).lte(5))
				.withSort(SortBuilders.fieldSort("totalRating").order(SortOrder.DESC)).build();

		return elasticsearchOperations.queryForPage(searchQuery, Store.class);
	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findAndSortProductByPrice(java
	 * .lang.Double, java.lang.Double)
	 */
/*	@Override
	public Page<StockCurrent> findAndSortProductByPrice(Double from, Double to) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(rangeQuery("sellPrice").gte(from).lte(to))
				.withSort(SortBuilders.fieldSort("sellPrice").order(SortOrder.DESC)).build();

		return elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findDeliveryInfoByStoreId(java
	 * .lang.String)
	 */
	/*@Override
	public Page<DeliveryInfo> findDeliveryInfoByStoreId(String storeId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("store.regNo", storeId)).build();

		return elasticsearchOperations.queryForPage(searchQuery, DeliveryInfo.class);
	}*/


	/*public Page<Store> headerSearch(String searchTerm, Pageable pageable) {
		Set<Store> storeSet = new HashSet<Store>();
		Set<HeaderSearch> values = new HashSet<HeaderSearch>();

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name", searchTerm))
				.withPageable(pageable).build();

		elasticsearchTemplate.query(searchQuery, new JestResultsExtractor<Set<HeaderSearch>>() {

			@Override
			public Set<HeaderSearch> extract(SearchResult response) {

				for (SearchResult.Hit<JsonObject, Void> searchHit : response.getHits(JsonObject.class)) {
					HeaderSearch result = new HeaderSearch();

					if (searchHit.index.equals("store")) {
						result.setStoreNo(searchHit.source.get("regNo").getAsString());
						System.out.println("************Store*****************" + result.getStoreNo());
					} else {
						result.setStoreNo(searchHit.source.get("iDPcode").getAsString());
					}

					values.add(result);

				}
				return values;
			}
		});

		for (HeaderSearch r : values) {
			StringQuery stringQuery = new StringQuery(termQuery("regNo.keyword", r.getStoreNo()).toString());
			storeSet.add(elasticsearchOperations.queryForObject(stringQuery, Store.class));
		}
		List<Store> storeList = new ArrayList<>();
		storeList.addAll(storeSet);

		return new PageImpl(storeList);

	}*/

	/*
	 * @Override public Page<Store> findByLocationNear(Point point, Distance
	 * distance, Pageable pageable) { // TODO Auto-generated method stub return
	 * storeSearchRepository.findByLocationNear(point,distance,pageable); }
	 */

	/*private CriteriaQuery getGeoQuery(Point point, Distance distance) {
		return new CriteriaQuery(new Criteria("location").within(point, distance));
	}*/

	/*@Override
	public Page<Store> findStoreByLocationName(String locationName) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchQuery("locationName", locationName).prefixLength(3)).build();

		return elasticsearchOperations.queryForPage(searchQuery, Store.class);
	}*/

	/*@Override
	public Page<Store> findAndSortStoreByMinAount(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
				.withSort(SortBuilders.fieldSort("minAmount").order(SortOrder.DESC)).withPageable(pageable).build();
		return elasticsearchOperations.queryForPage(searchQuery, Store.class);

	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findStoreTypeByStoreId(java.
	 * lang.String, org.springframework.data.domain.Pageable)
	 */
	/*@Override
	public Page<StoreType> findStoreTypeByStoreId(String storeId, Pageable pageable) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("store.regNo", storeId))
				.withPageable(pageable).build();

		return elasticsearchOperations.queryForPage(searchQuery, StoreType.class);
	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findProductsByCategoryName(
	 * java.lang.String)
	 */
	/*@Override
	public Page<Product> findProductsByCategoryName(String name) {

		List<Product> products = findAllProducts();

		List<Product> categoryNameBasedProduct = new ArrayList<Product>();

		products.forEach(p -> {

			if (p.getCategory().getName().equals(name)) {

				categoryNameBasedProduct.add(p);
			}

		});

		return new PageImpl(categoryNameBasedProduct);
	}*/

	/*@Override
	public Long findOrderCountByCustomerId(String customerId) {
		Long count = 0l;
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
				.withSearchType(QUERY_THEN_FETCH)

				.withIndices("order").withTypes("order")
				.addAggregation(AggregationBuilders.terms("customerorder").field("customerId.keyword")).build();

		AggregatedPage<Order> result = elasticsearchTemplate.queryForPage(searchQuery, Order.class);
		TermsAggregation categoryAggregation = result.getAggregation("customerorder", TermsAggregation.class);
		count = categoryAggregation.getBuckets().stream().filter(entry -> entry.getKey().equals(customerId)).findFirst()
				.get().getCount();

		return count;

	}*/

	/*public StoreSettings getStoreSettings(String IDPCode) {

		StringQuery searchQuery = new StringQuery(termQuery("regNo", IDPCode).toString());
		Store store = elasticsearchOperations.queryForObject(searchQuery, Store.class);
		return store.getStoreSettings();
	}*/

	/*public StoreAddress getStoreAddress(String IDPCode) {

		StringQuery searchQuery = new StringQuery(termQuery("regNo", IDPCode).toString());
		Store store = elasticsearchOperations.queryForObject(searchQuery, Store.class);
		return store.getStoreAddress();
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diviso.graeshoppe.service.QueryService#findAllAuxilariesByProductId(
	 * java.lang.Long)
	 */
	/*@Override
	public Page<AuxilaryLineItem> findAllAuxilariesByProductId(Long productId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("product.id", productId)).build();

		return elasticsearchOperations.queryForPage(searchQuery, AuxilaryLineItem.class);

	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findStockCurrentByCategoryName
	 * (java.lang.String)
	 */
/*	@Override
	public Page<StockCurrent> findStockCurrentByCategoryNameAndStoreId(String categoryName, String storeId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("product.category.name.keyword", categoryName))
						.must(QueryBuilders.matchQuery("product.iDPcode", storeId)))
				.build();
		return elasticsearchOperations.queryForPage(searchQuery, StockCurrent.class);

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findOrderByStatusName(java.lang.
	 * String)
	 */
	/*@Override
	public Page<Order> findOrderByStatusName(String statusName) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchQuery("status.name.keyword", statusName)).build();

		return elasticsearchOperations.queryForPage(searchQuery, Order.class);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diviso.graeshoppe.service.QueryService#findDeliveryinfobydatebetween(java
	 * .time.Instant, java.time.Instant)
	 */
	/*@Override
	public Page<Order> findOrderByDatebetweenAndStoreId(Instant from, Instant to, String storeId) {
		// .........
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
				.must(termQuery("storeId", storeId)).must(rangeQuery("date").gte(from).lte(to))).build();

		return elasticsearchOperations.queryForPage(searchQuery, Order.class);
	}
*/
	/*@Override
	public Page<ComboLineItem> findAllCombosByProductId(Long productId) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("product.id", productId)).build();

		return elasticsearchOperations.queryForPage(searchQuery, ComboLineItem.class);

	}*/

	/*@Override
	public Discount findDiscountByProductId(Long productId) {
		StringQuery searchQuery = new StringQuery(termQuery("id", productId).toString());
		Product product = elasticsearchOperations.queryForObject(searchQuery, Product.class);
		return product.getDiscount();
	}*/
}
