package com.diviso.graeshoppe.client.payment.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

/**
 * Link
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-09-23T12:15:47.246272+05:30[Asia/Kolkata]")

public class Link   {
  @JsonProperty("cancel_url")
  private String cancelUrl = null;

  @JsonProperty("return_url")
  private String returnUrl = null;

  public Link cancelUrl(String cancelUrl) {
    this.cancelUrl = cancelUrl;
    return this;
  }

  /**
   * Get cancelUrl
   * @return cancelUrl
  **/
  @ApiModelProperty(value = "")


  public String getCancelUrl() {
    return cancelUrl;
  }

  public void setCancelUrl(String cancelUrl) {
    this.cancelUrl = cancelUrl;
  }

  public Link returnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

  /**
   * Get returnUrl
   * @return returnUrl
  **/
  @ApiModelProperty(value = "")


  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(this.cancelUrl, link.cancelUrl) &&
        Objects.equals(this.returnUrl, link.returnUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cancelUrl, returnUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");
    
    sb.append("    cancelUrl: ").append(toIndentedString(cancelUrl)).append("\n");
    sb.append("    returnUrl: ").append(toIndentedString(returnUrl)).append("\n");
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

