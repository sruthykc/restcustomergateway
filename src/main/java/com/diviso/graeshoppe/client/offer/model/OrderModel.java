package com.diviso.graeshoppe.client.offer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OrderModel
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-15T16:47:08.654128+05:30[Asia/Kolkata]")

public class OrderModel   {
  @JsonProperty("claimedDate")
  private OffsetDateTime claimedDate = null;

  @JsonProperty("orderDiscountAmount")
  private Long orderDiscountAmount = null;

  @JsonProperty("orderDiscountTotal")
  private Long orderDiscountTotal = null;

  @JsonProperty("orderNumber")
  private Long orderNumber = null;

  @JsonProperty("orderTotal")
  private Long orderTotal = null;

  @JsonProperty("promoCode")
  private String promoCode = null;

  public OrderModel claimedDate(OffsetDateTime claimedDate) {
    this.claimedDate = claimedDate;
    return this;
  }

  /**
   * Get claimedDate
   * @return claimedDate
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getClaimedDate() {
    return claimedDate;
  }

  public void setClaimedDate(OffsetDateTime claimedDate) {
    this.claimedDate = claimedDate;
  }

  public OrderModel orderDiscountAmount(Long orderDiscountAmount) {
    this.orderDiscountAmount = orderDiscountAmount;
    return this;
  }

  /**
   * Get orderDiscountAmount
   * @return orderDiscountAmount
  **/
  @ApiModelProperty(value = "")


  public Long getOrderDiscountAmount() {
    return orderDiscountAmount;
  }

  public void setOrderDiscountAmount(Long orderDiscountAmount) {
    this.orderDiscountAmount = orderDiscountAmount;
  }

  public OrderModel orderDiscountTotal(Long orderDiscountTotal) {
    this.orderDiscountTotal = orderDiscountTotal;
    return this;
  }

  /**
   * Get orderDiscountTotal
   * @return orderDiscountTotal
  **/
  @ApiModelProperty(value = "")


  public Long getOrderDiscountTotal() {
    return orderDiscountTotal;
  }

  public void setOrderDiscountTotal(Long orderDiscountTotal) {
    this.orderDiscountTotal = orderDiscountTotal;
  }

  public OrderModel orderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

  /**
   * Get orderNumber
   * @return orderNumber
  **/
  @ApiModelProperty(value = "")


  public Long getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
  }

  public OrderModel orderTotal(Long orderTotal) {
    this.orderTotal = orderTotal;
    return this;
  }

  /**
   * Get orderTotal
   * @return orderTotal
  **/
  @ApiModelProperty(value = "")


  public Long getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(Long orderTotal) {
    this.orderTotal = orderTotal;
  }

  public OrderModel promoCode(String promoCode) {
    this.promoCode = promoCode;
    return this;
  }

  /**
   * Get promoCode
   * @return promoCode
  **/
  @ApiModelProperty(value = "")


  public String getPromoCode() {
    return promoCode;
  }

  public void setPromoCode(String promoCode) {
    this.promoCode = promoCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderModel orderModel = (OrderModel) o;
    return Objects.equals(this.claimedDate, orderModel.claimedDate) &&
        Objects.equals(this.orderDiscountAmount, orderModel.orderDiscountAmount) &&
        Objects.equals(this.orderDiscountTotal, orderModel.orderDiscountTotal) &&
        Objects.equals(this.orderNumber, orderModel.orderNumber) &&
        Objects.equals(this.orderTotal, orderModel.orderTotal) &&
        Objects.equals(this.promoCode, orderModel.promoCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(claimedDate, orderDiscountAmount, orderDiscountTotal, orderNumber, orderTotal, promoCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderModel {\n");
    
    sb.append("    claimedDate: ").append(toIndentedString(claimedDate)).append("\n");
    sb.append("    orderDiscountAmount: ").append(toIndentedString(orderDiscountAmount)).append("\n");
    sb.append("    orderDiscountTotal: ").append(toIndentedString(orderDiscountTotal)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    orderTotal: ").append(toIndentedString(orderTotal)).append("\n");
    sb.append("    promoCode: ").append(toIndentedString(promoCode)).append("\n");
    sb.append("}");
    return sb.toString();
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

