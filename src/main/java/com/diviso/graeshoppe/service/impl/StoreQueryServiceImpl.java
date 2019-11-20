package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diviso.graeshoppe.client.store.domain.DeliveryInfo;
import com.diviso.graeshoppe.client.store.domain.HeaderSearch;
import com.diviso.graeshoppe.client.store.domain.Review;
import com.diviso.graeshoppe.client.store.domain.Store;
import com.diviso.graeshoppe.client.store.domain.StoreAddress;
import com.diviso.graeshoppe.client.store.domain.StoreSettings;
import com.diviso.graeshoppe.client.store.domain.StoreType;
import com.diviso.graeshoppe.client.store.domain.Type;
import com.diviso.graeshoppe.client.store.domain.UserRating;
import com.diviso.graeshoppe.domain.Cart;
import com.diviso.graeshoppe.domain.ResultBucket;
import com.diviso.graeshoppe.service.StoreQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class StoreQueryServiceImpl implements StoreQueryService {


	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public StoreQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	
	
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
		return getResult(searchResponse, pageable,new Review());

	}


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
		return getResult(searchResponse, pageable,new UserRating());

	}

	
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
		Page<Store> storePage = getResult(searchResponse, pageable,new Store());

		storePage.forEach(store -> {
			List<UserRating> userRating = findUserRatingByRegNo(store.getRegNo(), pageable).getContent();
			store.setUserRatings(new HashSet<UserRating>(userRating));
		});

		return storePage;

	}

	
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
		return getResult(searchResponse, pageable,new Review());
	}

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
		return getResult(searchResponse, pageable,new UserRating());

	}
	
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
		Page<Store> storePage = getResult(searchResponse, pageable,new Store());
		return storePage;
	}
	
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

		Page<Store> storePage = getResult(searchResponse, pageable,new Store());

		for (StoreType storeType : storeTypeList) {
			storeSet.add(storeType.getStore());
		}

		return new PageImpl(new ArrayList<Store>(storeSet), pageable, searchResponse.getHits().getTotalHits());
	}


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
		return getResult(searchResponse, pageable,new Store());
	}
	
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
		Page<DeliveryInfo> deliveryinfos = getResult(searchResponse, pageable,new DeliveryInfo());
		List<Type> types = new ArrayList<Type>();

		deliveryinfos.forEach(deliveryInfo -> {
			types.add(deliveryInfo.getType());

		});
		return new PageImpl(types);

	}

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

		return getResult(searchResponse, pageable,new Store());
	}

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
		return getResult(searchResponse, pageable,new DeliveryInfo());

	}
	
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
	
/*	@Override
	public Page<Cart> findByLocationNear(
											 * Double lat,Double lon, Double distance ,
											  Pageable pageable) {
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

	
}*/
	
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

		return getResult(searchResponse, pageable,new Store());
	}

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
		return getResult(searchResponse, pageable,new Store());

	}
	
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
		return getResult(searchResponse, pageable,new StoreType());

	}
	
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
	/*private Page<Type> getTypeSearchResult(SearchResponse response, Pageable page) {

	SearchHit[] searchHit = response.getHits().getHits();

	List<Type> typeList = new ArrayList<>();

	for (SearchHit hit : searchHit) {
		typeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Type.class));
	}

	return new PageImpl(typeList, page, response.getHits().getTotalHits());

}
*/
/*private Page<StoreType> getStoreTypeSearchResult(SearchResponse response, Pageable page) {

	SearchHit[] searchHit = response.getHits().getHits();

	List<StoreType> storeTypeList = new ArrayList<>();

	for (SearchHit hit : searchHit) {
		storeTypeList.add(objectMapper.convertValue(hit.getSourceAsMap(), StoreType.class));
	}

	return new PageImpl(storeTypeList, page, response.getHits().getTotalHits());

}*/

/*private Page<DeliveryInfo> getDeliveryInfoSearchResult(SearchResponse response, Pageable page) {

	SearchHit[] searchHit = response.getHits().getHits();

	List<DeliveryInfo> deliveryInfoList = new ArrayList<>();

	for (SearchHit hit : searchHit) {
		deliveryInfoList.add(objectMapper.convertValue(hit.getSourceAsMap(), DeliveryInfo.class));
	}

	return new PageImpl(deliveryInfoList, page, response.getHits().getTotalHits());

}*/

	/*private Page<Review> getReviewSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Review> reviewList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			reviewList.add(objectMapper.convertValue(hit.getSourceAsMap(), Review.class));
		}

		return new PageImpl(reviewList, page, response.getHits().getTotalHits());

	}*/

	/*private Page<UserRating> getUserRatingSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<UserRating> userRatingList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			userRatingList.add(objectMapper.convertValue(hit.getSourceAsMap(), UserRating.class));
		}

		return new PageImpl(userRatingList, page, response.getHits().getTotalHits());

	}*/

	/*private Page<Store> getStoreSearchResult(SearchResponse response, Pageable page) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Store> storeList = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			storeList.add(objectMapper.convertValue(hit.getSourceAsMap(), Store.class));
		}

		return new PageImpl(storeList, page, response.getHits().getTotalHits());

	}*/

}	
