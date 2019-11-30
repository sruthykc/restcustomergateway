package com.diviso.graeshoppe.client.product.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ComboLineItemDTO
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-11-29T15:19:12.817+05:30[Asia/Kolkata]")

public class ComboLineItemDTO   {
  @JsonProperty("comboItemId")
  private Long comboItemId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("productId")
  private Long productId = null;

  @JsonProperty("quantity")
  private Double quantity = null;

  public ComboLineItemDTO comboItemId(Long comboItemId) {
    this.comboItemId = comboItemId;
    return this;
  }

  /**
   * Get comboItemId
   * @return comboItemId
  **/
  @ApiModelProperty(value = "")


  public Long getComboItemId() {
    return comboItemId;
  }

  public void setComboItemId(Long comboItemId) {
    this.comboItemId = comboItemId;
  }

  public ComboLineItemDTO description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ComboLineItemDTO id(Long id) {
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

  public ComboLineItemDTO productId(Long productId) {
    this.productId = productId;
    return this;
  }

  /**
   * Get productId
   * @return productId
  **/
  @ApiModelProperty(value = "")


  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public ComboLineItemDTO quantity(Double quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
  **/
  @ApiModelProperty(value = "")


  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComboLineItemDTO comboLineItemDTO = (ComboLineItemDTO) o;
    return Objects.equals(this.comboItemId, comboLineItemDTO.comboItemId) &&
        Objects.equals(this.description, comboLineItemDTO.description) &&
        Objects.equals(this.id, comboLineItemDTO.id) &&
        Objects.equals(this.productId, comboLineItemDTO.productId) &&
        Objects.equals(this.quantity, comboLineItemDTO.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comboItemId, description, id, productId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComboLineItemDTO {\n");
    
    sb.append("    comboItemId: ").append(toIndentedString(comboItemId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

