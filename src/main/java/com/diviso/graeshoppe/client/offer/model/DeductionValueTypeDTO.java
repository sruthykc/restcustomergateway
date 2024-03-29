package com.diviso.graeshoppe.client.offer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

/**
 * DeductionValueTypeDTO
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-15T16:47:08.654128+05:30[Asia/Kolkata]")

public class DeductionValueTypeDTO   {
  @JsonProperty("deductionValueType")
  private String deductionValueType = null;

  @JsonProperty("id")
  private Long id = null;

  public DeductionValueTypeDTO deductionValueType(String deductionValueType) {
    this.deductionValueType = deductionValueType;
    return this;
  }

  /**
   * Get deductionValueType
   * @return deductionValueType
  **/
  @ApiModelProperty(value = "")


  public String getDeductionValueType() {
    return deductionValueType;
  }

  public void setDeductionValueType(String deductionValueType) {
    this.deductionValueType = deductionValueType;
  }

  public DeductionValueTypeDTO id(Long id) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeductionValueTypeDTO deductionValueTypeDTO = (DeductionValueTypeDTO) o;
    return Objects.equals(this.deductionValueType, deductionValueTypeDTO.deductionValueType) &&
        Objects.equals(this.id, deductionValueTypeDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deductionValueType, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeductionValueTypeDTO {\n");
    
    sb.append("    deductionValueType: ").append(toIndentedString(deductionValueType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

