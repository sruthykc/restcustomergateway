package com.diviso.graeshoppe.client.store.model;

import java.util.Objects;
import com.diviso.graeshoppe.client.store.model.Store;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * UserRating
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-07-29T10:47:29.652+05:30[Asia/Calcutta]")

public class UserRating   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("ratedOn")
  private OffsetDateTime ratedOn = null;

  @JsonProperty("rating")
  private Double rating = null;

  @JsonProperty("store")
  private Store store = null;

  @JsonProperty("userName")
  private String userName = null;

  public UserRating id(Long id) {
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

  public UserRating ratedOn(OffsetDateTime ratedOn) {
    this.ratedOn = ratedOn;
    return this;
  }

  /**
   * Get ratedOn
   * @return ratedOn
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getRatedOn() {
    return ratedOn;
  }

  public void setRatedOn(OffsetDateTime ratedOn) {
    this.ratedOn = ratedOn;
  }

  public UserRating rating(Double rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
  **/
  @ApiModelProperty(value = "")


  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public UserRating store(Store store) {
    this.store = store;
    return this;
  }

  /**
   * Get store
   * @return store
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public UserRating userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
  **/
  @ApiModelProperty(value = "")


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRating userRating = (UserRating) o;
    return Objects.equals(this.id, userRating.id) &&
        Objects.equals(this.ratedOn, userRating.ratedOn) &&
        Objects.equals(this.rating, userRating.rating) &&
        Objects.equals(this.store, userRating.store) &&
        Objects.equals(this.userName, userRating.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, ratedOn, rating, store, userName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRating {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ratedOn: ").append(toIndentedString(ratedOn)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    store: ").append(toIndentedString(store)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
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

