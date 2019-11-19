package com.diviso.graeshoppe.client.customer.model;

import java.util.Objects;
import com.diviso.graeshoppe.client.customer.model.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FavouriteStore
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-28T12:48:05.037111+05:30[Asia/Kolkata]")
@Document(indexName = "favouritestore")
public class FavouriteStore   {
  @JsonProperty("customer")
  private Customer customer = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("storeId")
  private Long storeId = null;

  public FavouriteStore customer(Customer customer) {
    this.customer = customer;
    return this;
  }

  /**
   * Get customer
   * @return customer
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public FavouriteStore id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FavouriteStore storeId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  /**
   * Get storeId
   * @return storeId
  **/
  @ApiModelProperty(value = "")


  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FavouriteStore favouriteStore = (FavouriteStore) o;
    return Objects.equals(this.customer, favouriteStore.customer) &&
        Objects.equals(this.id, favouriteStore.id) &&
        Objects.equals(this.storeId, favouriteStore.storeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer, id, storeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FavouriteStore {\n");
    
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
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

