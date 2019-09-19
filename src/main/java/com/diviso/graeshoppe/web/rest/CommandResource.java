package com.diviso.graeshoppe.web.rest;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diviso.graeshoppe.client.aggregators.CustomerAggregator;
import com.diviso.graeshoppe.client.customer.api.ContactResourceApi;
import com.diviso.graeshoppe.client.customer.api.CustomerResourceApi;
import com.diviso.graeshoppe.client.customer.model.ContactDTO;
import com.diviso.graeshoppe.client.customer.model.CustomerDTO;
import com.diviso.graeshoppe.client.product.api.CategoryResourceApi;
import com.diviso.graeshoppe.client.product.api.ProductResourceApi;
import com.diviso.graeshoppe.client.product.api.StockCurrentResourceApi;
import com.diviso.graeshoppe.client.product.api.UomResourceApi;
import com.diviso.graeshoppe.client.product.model.CategoryDTO;
import com.diviso.graeshoppe.client.product.model.ProductDTO;
import com.diviso.graeshoppe.client.product.model.StockCurrentDTO;
import com.diviso.graeshoppe.client.product.model.UOMDTO;
import com.diviso.graeshoppe.client.sale.api.SaleResourceApi;
import com.diviso.graeshoppe.client.sale.api.TicketLineResourceApi;
import com.diviso.graeshoppe.client.sale.model.SaleDTO;
import com.diviso.graeshoppe.client.sale.model.TicketLineDTO;
import com.diviso.graeshoppe.client.store.api.ReplyResourceApi;
import com.diviso.graeshoppe.client.store.api.ReviewResourceApi;
import com.diviso.graeshoppe.client.store.api.StoreResourceApi;
import com.diviso.graeshoppe.client.store.api.UserRatingResourceApi;
import com.diviso.graeshoppe.client.store.domain.RatingReview;
import com.diviso.graeshoppe.client.store.domain.Review;
import com.diviso.graeshoppe.client.store.domain.UserRating;
import com.diviso.graeshoppe.client.store.model.ReplyDTO;
import com.diviso.graeshoppe.client.store.model.ReviewDTO;
import com.diviso.graeshoppe.client.store.model.StoreDTO;
import com.diviso.graeshoppe.client.store.model.UserRatingDTO;
import com.diviso.graeshoppe.service.QueryService;

@RestController
@RequestMapping("/api/command")
public class CommandResource {

	@Autowired
	private UomResourceApi uomResourceApi;

	@Autowired
	private CategoryResourceApi categoryResourceApi;
	/*@Autowired
	private StockLineResourceApi stockLineResourceApi;*/
	@Autowired
	private ProductResourceApi productResourceApi;
	@Autowired
	private ContactResourceApi contactResourceApi;
	@Autowired
	private CustomerResourceApi customerResourceApi;
	@Autowired
	private SaleResourceApi saleResourceApi;
	@Autowired
	private StockCurrentResourceApi stockCurrentResourceApi;
	/*@Autowired
	private StockDiaryResourceApi stockDiaryResourceApi;
*/
	@Autowired
	private StoreResourceApi storeResourceApi;

	@Autowired
	private ReplyResourceApi replyResourceApi;

	@Autowired
	private UserRatingResourceApi userRatingResourceApi;

	@Autowired
	private ReviewResourceApi reviewResourceApi;

	@Autowired
	TicketLineResourceApi ticketLineResourceApi;

	@Autowired
	QueryService queryService;

	@Autowired
	QueryResource queryResource;
	
	private final Logger log = LoggerFactory.getLogger(CommandResource.class);

	@PostMapping("/customers/register-customer")
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerAggregator customerAggregator) {

		CustomerDTO customerDTO = new CustomerDTO();
		ContactDTO contactDTO = new ContactDTO();
		customerDTO.setName(customerAggregator.getName());
		customerDTO.setReference(customerAggregator.getReference());
		contactDTO.setMobileNumber(customerAggregator.getMobileNumber());
		contactDTO.setEmail(customerAggregator.getEmail());
		ContactDTO resultDTO = contactResourceApi.createContactUsingPOST(contactDTO).getBody();
		customerDTO.setContactId(resultDTO.getId());
		return customerResourceApi.createCustomerUsingPOST(customerDTO);

	}

	@PutMapping("/customers")
	public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) {
		return customerResourceApi.updateCustomerUsingPUT(customerDTO);
	}

	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		Long contactid = customerResourceApi.getCustomerUsingGET(id).getBody().getContactId();
		customerResourceApi.deleteCustomerUsingDELETE(id);
		this.deleteContact(contactid);
	}

	@PutMapping("/contacts")
	public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contact) {
		return this.contactResourceApi.updateContactUsingPUT(contact);
	}

	@DeleteMapping("/contacts/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
		return this.contactResourceApi.deleteContactUsingDELETE(id);
	}

	@PutMapping("/categories")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO) {
		return categoryResourceApi.updateCategoryUsingPUT(categoryDTO);
	}

	@DeleteMapping("/categories/{id}")
	public void deleteCategory(@PathVariable Long id) {
		categoryResourceApi.deleteCategoryUsingDELETE(id);
	}

	@PostMapping("/unit-of-meassurement")
	public ResponseEntity<UOMDTO> createUOM(@RequestBody UOMDTO uomDTO) {
		return uomResourceApi.createUOMUsingPOST(uomDTO);
	}

	@PostMapping("/productCategory")
	public ResponseEntity<CategoryDTO> createProductCategory(@RequestBody CategoryDTO categoryDTO) {
		return categoryResourceApi.createCategoryUsingPOST(categoryDTO);
	}

	@PostMapping("/products")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
		return productResourceApi.createProductUsingPOST(productDTO);
	}

	@PutMapping("/products")
	public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
		return productResourceApi.updateProductUsingPUT(productDTO);
	}

	@DeleteMapping("/products/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productResourceApi.deleteProductUsingDELETE(id);
	}

	@PostMapping("/sales")
	public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDTO) {
		saleDTO.date(Instant.now());

		return saleResourceApi.createSaleUsingPOST(saleDTO);
	}

	@PutMapping("/sales")
	public ResponseEntity<SaleDTO> updateSale(@RequestBody SaleDTO saleDTO) {
		return this.saleResourceApi.updateSaleUsingPUT(saleDTO);
	}

	@DeleteMapping("/sales/{id}")
	public void deleteSale(@PathVariable Long id) {
		this.ticketLineResourceApi.findAllTicketLinesBySaleIdUsingGET(id).getBody().forEach(ticket -> {
			this.deleteTicketline(ticket.getId());
		});
		this.saleResourceApi.deleteSaleUsingDELETE(id);
	}

	@PostMapping("/ticket-lines")
	public ResponseEntity<TicketLineDTO> createTickerLine(@RequestBody TicketLineDTO ticketLineDTO) {
		return this.ticketLineResourceApi.createTicketLineUsingPOST(ticketLineDTO);
	}

	@PutMapping("/ticket-lines")
	public ResponseEntity<TicketLineDTO> updateTicketLine(@RequestBody TicketLineDTO ticketLineDTO) {
		return ticketLineResourceApi.updateTicketLineUsingPUT(ticketLineDTO);
	}

	@DeleteMapping("/ticket-lines/{id}")
	public void deleteTicketline(@PathVariable Long id) {
		ticketLineResourceApi.deleteTicketLineUsingDELETE(id);
	}

	@PutMapping("/uoms")
	public ResponseEntity<UOMDTO> updateUOM(@RequestBody UOMDTO uomDTO) {
		return uomResourceApi.updateUOMUsingPUT(uomDTO);
	}

	@DeleteMapping("/uoms/{id}")
	public void deleteUOM(@PathVariable Long id) {
		uomResourceApi.deleteUOMUsingDELETE(id);
	}

/*	@PostMapping("/stocklines")
	public ResponseEntity<StockLineDTO> createStockLine(@RequestBody StockLineDTO stockLine) {
		return this.stockLineResourceApi.createStockLineUsingPOST(stockLine);
	}

	@PutMapping("/stocklines")
	public ResponseEntity<StockLineDTO> updateStockLine(@RequestBody StockLineDTO stockLine) {
		return this.stockLineResourceApi.updateStockLineUsingPUT(stockLine);
	}

	@DeleteMapping("/stocklines/{id}")
	public ResponseEntity<Void> deleteStockLine(@PathVariable Long id) {
		return this.stockLineResourceApi.deleteStockLineUsingDELETE(id);
	}

	@PostMapping("/stock-diaries")
	public ResponseEntity<StockDiaryDTO> createStockCurrent(@RequestBody StockDiaryDTO stockDiary) {
		return this.stockDiaryResourceApi.createStockDiaryUsingPOST(stockDiary);
	}

	@PutMapping("/stock-diaries")
	public ResponseEntity<StockDiaryDTO> updateStockDiary(@RequestBody StockDiaryDTO stockDiary) {
		return this.stockDiaryResourceApi.updateStockDiaryUsingPUT(stockDiary);
	}*/

	@PostMapping("/stock-currents")
	public ResponseEntity<StockCurrentDTO> createStockCurrent(@RequestBody StockCurrentDTO stockCurrent) {
		return this.stockCurrentResourceApi.createStockCurrentUsingPOST(stockCurrent);
	}

	@PutMapping("/stock-currents")
	public ResponseEntity<StockCurrentDTO> updateStockCurrent(@RequestBody StockCurrentDTO StockCurrent) {
		return this.stockCurrentResourceApi.updateStockCurrentUsingPUT(StockCurrent);
	}

/*	@PostMapping("/stock-of-product")
	public ResponseEntity<StockDiaryDTO> createStockOfProduct(@RequestBody StockDiaryDTO stockDiaryDTO) {
		return this.stockDiaryResourceApi.createStockOfProductUsingPOST(stockDiaryDTO);
	}*/

	@PostMapping("/stores")
	public ResponseEntity<StoreDTO> createStore(@RequestBody StoreDTO storeDTO) {

		return this.storeResourceApi.createStoreUsingPOST(storeDTO);
	}

	@PutMapping("/stores")
	public ResponseEntity<StoreDTO> updateStore(@RequestBody StoreDTO storeDTO) {
		return this.storeResourceApi.updateStoreUsingPUT(storeDTO);
	}

	@DeleteMapping("/stores/{id}")
	public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
		return this.storeResourceApi.deleteStoreUsingDELETE(id);
	}

	@PostMapping("/replies")
	public ResponseEntity<ReplyDTO> createReply(@RequestBody ReplyDTO replyDTO) {
		return this.replyResourceApi.createReplyUsingPOST(replyDTO);
	}

	@PutMapping("/replies")
	public ResponseEntity<ReplyDTO> updateReply(@RequestBody ReplyDTO replyDTO) {
		return this.replyResourceApi.updateReplyUsingPUT(replyDTO);
	}

	@DeleteMapping("/replies/{id}")
	public ResponseEntity<Void> deleteReply(@PathVariable Long id) {
		return this.replyResourceApi.deleteReplyUsingDELETE(id);
	}

	@PostMapping("/user-ratings")
	public ResponseEntity<UserRatingDTO> createUserRating(@RequestBody UserRatingDTO userRatingDTO) {
		return this.userRatingResourceApi.createUserRatingUsingPOST(userRatingDTO);
	}

	@PutMapping("/user-ratings")
	public ResponseEntity<UserRatingDTO> updateUserRating(@RequestBody UserRatingDTO userRatingDTO) {
		return this.userRatingResourceApi.updateUserRatingUsingPUT(userRatingDTO);
	}

	@DeleteMapping("/user-ratings/{id}")
	public ResponseEntity<Void> deleteUserRating(@PathVariable Long id) {
		return this.userRatingResourceApi.deleteUserRatingUsingDELETE(id);
	}

	@PostMapping("/reviews")
	public ResponseEntity<ReviewDTO> createUserReview(@RequestBody ReviewDTO reviewDTO) {
		return this.reviewResourceApi.createReviewUsingPOST(reviewDTO);
	}

	@PutMapping("/reviews")
	public ResponseEntity<ReviewDTO> updateReview(@RequestBody ReviewDTO reviewDTO) {
		return this.reviewResourceApi.updateReviewUsingPUT(reviewDTO);
	}

	@DeleteMapping("/reviews/{id}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
		return this.reviewResourceApi.deleteReviewUsingDELETE(id);
	}

	@PostMapping("/rating-review")
	public  ResponseEntity<Page<RatingReview>> createRatingAndReview(@RequestBody RatingReview ratingReview,Pageable pageable) {
		
		
		UserRatingDTO userRatingDTO = ratingReview.getRating();
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>ratingdto>>>>>>>>>>>>>>>>>>>>>>  "+userRatingDTO);
		ReviewDTO reviewDTO = ratingReview.getReview();
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>reviewdto>>>>>>>>>>>>>>>>>>>>>>  "+reviewDTO);
		StoreDTO store=null;
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + userRatingDTO + ">>>>>>>>>>>>>>>>>>>>>" + reviewDTO+"     condition...     "+(userRatingDTO.getRating() != null));
		if (userRatingDTO.getRating() != null) {
			
			 store = storeResourceApi.getStoreUsingGET(userRatingDTO.getStoreId()).getBody();
			 log.info(">>>>>>>>>>>>>>>>>>>>>>>>IF>>>>>>>>>>>>>>>>>>>>>>"+store.getRegNo()+"     "+userRatingDTO.getUserName());
			UserRating alreadyRatedUser = queryService.findRatingByStoreIdAndCustomerName(store.getRegNo(),
					userRatingDTO.getUserName());
			
			log.info(">>>>>>>>>>>>>>>>>>>>>>alreadyRatedUser: >>>>>>>>>>>>>>>>>>>>>>>>" + alreadyRatedUser);

			if (alreadyRatedUser == null) {
				log.info("............create................");

				ResponseEntity<ReviewDTO> review = reviewResourceApi.createReviewUsingPOST(reviewDTO);
				
				log.info("...........saved review............."+review.getBody());

				log.info("............check userrating................  "+userRatingDTO);
				
				ResponseEntity<UserRatingDTO> ratingDTO = userRatingResourceApi
						.createUserRatingUsingPOST(userRatingDTO);
				
				log.info("............saved rating.........."+ratingDTO.getBody());
				
				
				
				
				ratingReview.setRating(ratingDTO.getBody());
				ratingReview.setReview(review.getBody());
			} else {

				log.info("..............................ELSE>>>>>>>>>>>>>>>>>>>>>>>>>");
				if (alreadyRatedUser.getId() != null) {
					log.info("....................UPDATE..............");

					userRatingDTO.setId(alreadyRatedUser.getId());

					log.info("................username:............" + userRatingDTO.getUserName()
							+ "..........storeId..........." + store.getRegNo() + "...........");

					Review alreadyreviewed = queryService.findReviewByStoreIdAndCustomerName(store.getRegNo(),
							userRatingDTO.getUserName());

					log.info("...................   " + alreadyreviewed + "     ...............");

					reviewDTO.setId(alreadyreviewed.getId());

					ResponseEntity<ReviewDTO> review = reviewResourceApi.updateReviewUsingPUT(reviewDTO);

					ResponseEntity<UserRatingDTO> ratingDTO = userRatingResourceApi
							.updateUserRatingUsingPUT(userRatingDTO);

					ratingReview.setRating(ratingDTO.getBody());

					ratingReview.setReview(review.getBody());
				}
			}

		}
		 return queryResource.findRatingReviewByStoreidAndCustomerName(store.getRegNo(), pageable);
	}

}
