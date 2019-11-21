package com.diviso.graeshoppe.client.order.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OfferDTO
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-10-29T12:42:01.121660+05:30[Asia/Kolkata]")

public class OfferDTO {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("offerRef")
	private String offerRef = null;
	@JsonProperty("orderDiscountAmount")
	private Double orderDiscountAmount;

	@JsonProperty("description")
	private String description;

	/**
	 * @return the orderDiscountAmount
	 */
	public Double getOrderDiscountAmount() {
		return orderDiscountAmount;
	}

	/**
	 * @param orderDiscountAmount the orderDiscountAmount to set
	 */
	public void setOrderDiscountAmount(Double orderDiscountAmount) {
		this.orderDiscountAmount = orderDiscountAmount;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("orderId")
	private Long orderId = null;

	public OfferDTO id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "")

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OfferDTO offerRef(String offerRef) {
		this.offerRef = offerRef;
		return this;
	}

	/**
	 * Get offerRef
	 * 
	 * @return offerRef
	 **/
	@ApiModelProperty(value = "")

	public String getOfferRef() {
		return offerRef;
	}

	public void setOfferRef(String offerRef) {
		this.offerRef = offerRef;
	}

	public OfferDTO orderId(Long orderId) {
		this.orderId = orderId;
		return this;
	}

	/**
	 * Get orderId
	 * 
	 * @return orderId
	 **/
	@ApiModelProperty(value = "")

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return String.format(
				"OfferDTO [id=%s,\n offerRef=%s,\n orderDiscountAmount=%s,\n description=%s,\n orderId=%s]", id,
				offerRef, orderDiscountAmount, description, orderId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((offerRef == null) ? 0 : offerRef.hashCode());
		result = prime * result + ((orderDiscountAmount == null) ? 0 : orderDiscountAmount.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OfferDTO))
			return false;
		OfferDTO other = (OfferDTO) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (offerRef == null) {
			if (other.offerRef != null)
				return false;
		} else if (!offerRef.equals(other.offerRef))
			return false;
		if (orderDiscountAmount == null) {
			if (other.orderDiscountAmount != null)
				return false;
		} else if (!orderDiscountAmount.equals(other.orderDiscountAmount))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
