package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import org.elasticsearch.index.query.QueryBuilder;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.diviso.graeshoppe.web.rest.util.ServiceUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StoreQueryServiceImpl implements StoreQueryService {
	@Autowired
	ServiceUtility serviceUtility;

	private final Logger log = LoggerFactory.getLogger(StoreQueryServiceImpl.class);

	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public StoreQueryServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public Page<Review> findAllReviews(Pageable pageable) {
		log.debug("Request to get all Reviews ");
		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" };
		 */

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * // searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 * 
		 * searchSourceBuilder.query(matchAllQuery());
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("review",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 */

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		SearchResponse searchResponse = searchResponseForPage("review", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new Review());

	}

	@Override
	public Page<UserRating> findAllUserRatings(Pageable pageable) {

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * searchSourceBuilder.query(matchAllQuery());
		 * 
		 * SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("userrating", pageable.getPageSize(),
		 * pageable.getPageNumber(), searchSourceBuilder); SearchResponse searchResponse
		 * = null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		SearchResponse searchResponse = searchResponseForPage("userrating", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new UserRating());

	}

	@Override
	public Page<Store> findAllStores(Pageable pageable) {

		/*
		 * String[] includeFields = new String[] { ""}; String[] excludeFields = new
		 * String[] { "storeaddress.*","reviews.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(matchAllQuery());
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("store",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 */

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		SearchResponse searchResponse = searchResponseForPage("store", searchSourceBuilder, pageable);

		Page<Store> storePage = serviceUtility.getPageResult(searchResponse, pageable, new Store());

		storePage.forEach(store -> {
			List<UserRating> userRating = findUserRatingByRegNo(store.getRegNo(), pageable).getContent();
			store.setUserRatings(new HashSet<UserRating>(userRating));
		});

		return storePage;

	}

	@Override
	public Store findStoreByRegNo(String regNo) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(termQuery("regNo.keyword", regNo));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("store");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = termQuery("regNo.keyword", regNo);
		SearchResponse searchResponse = searchResponseForObject("store", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new Store());
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

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(termQuery("store.regNo.keyword", storeId));
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("review",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(termQuery("store.regNo.keyword", storeId));
		SearchResponse searchResponse = searchResponseForPage("review", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new Review());
	}

	@Override
	public Page<UserRating> findUserRatingByRegNo(String regNo, Pageable pageable) {

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * searchSourceBuilder.query(termQuery("store.regNo.keyword", regNo));
		 * 
		 * SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("userrating", pageable.getPageSize(),
		 * pageable.getPageNumber(), searchSourceBuilder); SearchResponse searchResponse
		 * = null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(termQuery("store.regNo.keyword", regNo));
		SearchResponse searchResponse = searchResponseForPage("userrating", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new UserRating());

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

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.
		 * termQuery("store.regNo", storeId))
		 * .must(QueryBuilders.termQuery("userName.keyword", name)));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("userrating");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo.keyword", storeId))
				.must(QueryBuilders.termQuery("userName.keyword", name));
		SearchResponse searchResponse = searchResponseForObject("userrating", dslQuery);

		return serviceUtility.getObjectResult(searchResponse, new UserRating());

	}

	@Override
	public UserRating findRatingByStoreId(String storeId) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.
		 * termQuery("store.regNo", storeId)));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("userrating");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */
		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo.keyword", storeId));
		SearchResponse searchResponse = searchResponseForObject("userrating", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new UserRating());

	}

	@Override
	public UserRating findRatingByName(String name) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(QueryBuilders.termQuery("userName.keyword", name));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("userrating");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */
		QueryBuilder dslQuery = QueryBuilders.termQuery("userName.keyword", name);
		SearchResponse searchResponse = searchResponseForObject("userrating", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new UserRating());
	}

	@Override
	public Review findReviewByStoreIdAndCustomerName(String storeId, String name) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.
		 * termQuery("store.regNo", storeId)) .must(QueryBuilders.termQuery("userName",
		 * name)));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("review");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */
		QueryBuilder dslQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store.regNo.keyword", storeId))
				.must(QueryBuilders.termQuery("userName.keyword", name));
		SearchResponse searchResponse = searchResponseForObject("review", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new Review());
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

		/*
		 * String[] includeFields = new String[] { "iDPcode"}; String[] excludeFields =
		 * new String[] { "category.*" }; searchSourceBuilder.fetchSource(includeFields,
		 * excludeFields);
		 */
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * searchSourceBuilder.query(termQuery("deliveryInfos.type.name.keyword",
		 * deliveryType));
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("store",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(termQuery("deliveryInfos.type.name.keyword", deliveryType));
		SearchResponse searchResponse = searchResponseForPage("store", searchSourceBuilder, pageable);
		return serviceUtility.getPageResult(searchResponse, pageable, new Store());

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

		Page<Store> storePage = serviceUtility.getPageResult(searchResponse, pageable, new Store());

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
		SearchRequest searchRequest = serviceUtility.generateSearchRequest("deliveryinfo", pageable.getPageSize(),
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

		/*
		 * String[] includeFields = new String[] { "iDPcode", "image" }; String[]
		 * excludeFields = new String[] { "category.*" };
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 */
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * searchSourceBuilder.query(matchQuery("name", searchTerm).prefixLength(3));
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("store",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchQuery("name", searchTerm).prefixLength(3));
		SearchResponse searchResponse = searchResponseForPage("store", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new Store());
	}

	@Override
	public Page<Type> findAllDeliveryTypesByStoreId(Long storeId, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(termQuery("store.id", storeId));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("deliveryinfo", pageable.getPageSize(),
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
		Page<DeliveryInfo> deliveryinfos = serviceUtility.getPageResult(searchResponse, pageable, new DeliveryInfo());
		List<Type> types = new ArrayList<Type>();

		deliveryinfos.forEach(deliveryInfo -> {
			types.add(deliveryInfo.getType());

		});
		return new PageImpl(types);

	}

	@Override
	public DeliveryInfo findDeliveryInfoById(Long id) {
		// String[] includeFields = new String[] { "iDPcode", "image" };
		// String[] excludeFields = new String[] { "category.*" };
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * // searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 * 
		 * searchSourceBuilder.query(termQuery("id", id));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("deliveryinfo");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = termQuery("id", id);
		SearchResponse searchResponse = searchResponseForObject("deliveryinfo", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new DeliveryInfo());

	}

	@Override
	public Page<Store> findStoreByRating(Pageable pageable) {

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(rangeQuery("totalRating").gte(1).lte(5));
		 * searchSourceBuilder.sort(new
		 * FieldSortBuilder("totalRating").order(SortOrder.DESC));
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("store",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); } return }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(rangeQuery("totalRating").gte(1).lte(5));
		searchSourceBuilder.sort(new FieldSortBuilder("totalRating").order(SortOrder.DESC));
		SearchResponse searchResponse = searchResponseForPage("store", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new Store());
	}

	@Override
	public Page<DeliveryInfo> findDeliveryInfoByStoreId(String storeId, Pageable pageable) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(termQuery("store.regNo.keyword", storeId));
		 * 
		 * SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("deliveryinfo", pageable.getPageSize(),
		 * pageable.getPageNumber(), searchSourceBuilder); SearchResponse searchResponse
		 * = null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace();
		 * 
		 * }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(termQuery("store.regNo.keyword", storeId));
		SearchResponse searchResponse = searchResponseForPage("deliveryinfo", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new DeliveryInfo());

	}

	public Page<Store> headerSearch(String searchTerm, Pageable pageable) throws IOException {
		Set<Store> storeSet = new HashSet<Store>();
		Set<HeaderSearch> values = new HashSet<HeaderSearch>();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(matchQuery("name", searchTerm));

		SearchRequest searchRequest = generateSearchRequestForMultipleIndex(pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
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

	public Page<Store> findStoresByRegNoList(Set<HeaderSearch> values, Pageable pageable) throws IOException {
		System.out.println("%%%%Set<HeaderSearch> values%%%%%%%%%%%%%%%" + values);
		Set<Store> storeSet = new HashSet<Store>();

		for (HeaderSearch r : values) {
			storeSet.add(createHeaderQuery(r));

		}
		List<Store> storeList = new ArrayList<>();
		storeList.addAll(storeSet);
		System.out.println("%%%%EXIT%%%%%%%%%%%%%%%" + storeList);
		return new PageImpl(storeList);
		// return new PageImpl(storeList, pageable,
		// searchResponse.getHits().getTotalHits());

	}

	@Override
	public Page<Store> findStoreByLocationName(String locationName, Pageable pageable) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(matchQuery("locationName",
		 * locationName).prefixLength(3));
		 * 
		 * SearchRequest searchRequest = serviceUtility.generateSearchRequest("store",
		 * pageable.getPageSize(), pageable.getPageNumber(), searchSourceBuilder);
		 * SearchResponse searchResponse = null; try { searchResponse =
		 * restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT); } catch
		 * (IOException e) { // TODO Auto-generated e.printStackTrace(); }
		 */

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchQuery("locationName", locationName).prefixLength(3));
		SearchResponse searchResponse = searchResponseForPage("store", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new Store());
	}

	@Override
	public Page<Store> findAndSortStoreByMinAount(Pageable pageable) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(matchAllQuery()); searchSourceBuilder.sort(new
		 * ScoreSortBuilder().order(SortOrder.DESC)); SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("store", pageable.getPageSize(),
		 * pageable.getPageNumber(), searchSourceBuilder); SearchResponse searchResponse
		 * = null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchAllQuery());
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		SearchResponse searchResponse = searchResponseForPage("store", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new Store());

	}

	@Override
	public Page<StoreType> findStoreTypeByStoreId(String storeId, Pageable pageable) {
		String[] includeFields = new String[] { "id", "name" };
		String[] excludeFields = new String[] { "store.*" };
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.fetchSource(includeFields, excludeFields);
		 * 
		 * searchSourceBuilder.query(termQuery("store.regNo", storeId));
		 * 
		 * SearchRequest searchRequest =
		 * serviceUtility.generateSearchRequest("storetype", pageable.getPageSize(),
		 * pageable.getPageNumber(), searchSourceBuilder); SearchResponse searchResponse
		 * = null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		searchSourceBuilder.query(termQuery("store.regNo", storeId));

		SearchResponse searchResponse = searchResponseForPage("storetype", searchSourceBuilder, pageable);

		return serviceUtility.getPageResult(searchResponse, pageable, new StoreType());

	}

	public StoreSettings getStoreSettings(String IDPCode) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(termQuery("regNo.keyword", IDPCode));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("store");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = termQuery("regNo.keyword", IDPCode);
		SearchResponse searchResponse = searchResponseForObject("store", dslQuery);

		return serviceUtility.getObjectResult(searchResponse, new Store()).getStoreSettings();
	}

	public StoreAddress getStoreAddress(String iDPCode) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(termQuery("regNo.keyword", iDPCode));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("store");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = termQuery("regNo.keyword", iDPCode);
		SearchResponse searchResponse = searchResponseForObject("store", dslQuery);

		return serviceUtility.getObjectResult(searchResponse, new Store()).getStoreAddress();

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
				System.out.println("++++++++++++++++++++++++++++++++++" + storeTypeList);
				storeSet.add(objectMapper.convertValue(hit.getSourceAsMap(), StoreType.class).getStore());
				System.out.println("++++++++++++++++++++++++++++++++++" + storeSet);

			}

		}
		return new PageImpl(new ArrayList<Store>(storeSet));

	}

	public List<ResultBucket> findStoreTypeAndCount1(Pageable pageable) {
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
	public Page<Store> findByLocationNear(Double lat, Double lon, Double distance, String distanceUnit,
			Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.geoDistanceQuery("location").point(10.7795, 76.4708).distance(15,
				DistanceUnit.KILOMETERS));

		SearchRequest searchRequest = serviceUtility.generateSearchRequest("store", pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

		return serviceUtility.getPageResult(searchResponse, pageable, new Store());

	}

	private SearchRequest generateSearchRequestForMultipleIndex(Integer totalElement, Integer pageNumber,
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

	@Override
	public Store findStoreById(Long id) {
		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * 
		 * searchSourceBuilder.query(QueryBuilders.termQuery("id", id));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("store");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); }
		 */

		QueryBuilder dslQuery = termQuery("id", id);
		SearchResponse searchResponse = searchResponseForObject("store", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new Store());
	}

	private SearchResponse searchResponseForPage(String indexName, SearchSourceBuilder searchSourceBuilder,
			Pageable pageable) {

		SearchRequest searchRequest = serviceUtility.generateSearchRequest(indexName, pageable.getPageSize(),
				pageable.getPageNumber(), searchSourceBuilder);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}
		return searchResponse;
	}

	private SearchResponse searchResponseForObject(String indexName, QueryBuilder dslQuery) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(dslQuery);

		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) { // TODO Auto-generated e.printStackTrace(); } return
		}
		return searchResponse;
	}

	private Store createHeaderQuery(HeaderSearch r) {

		/*
		 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 * searchSourceBuilder.query(termQuery("regNo.keyword", r.getStoreNo()));
		 * 
		 * SearchRequest searchRequest = new SearchRequest("store");
		 * searchRequest.source(searchSourceBuilder); SearchResponse searchResponse =
		 * null; try { searchResponse = restHighLevelClient.search(searchRequest,
		 * RequestOptions.DEFAULT); } catch (IOException e) { // TODO Auto-generated
		 * e.printStackTrace(); } return }
		 */
		QueryBuilder dslQuery = termQuery("regNo.keyword", r.getStoreNo());
		SearchResponse searchResponse = searchResponseForObject("store", dslQuery);
		return serviceUtility.getObjectResult(searchResponse, new Store());
	}

	// **************************************************************************************************************************

}
