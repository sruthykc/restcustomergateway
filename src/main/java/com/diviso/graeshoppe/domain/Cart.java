package com.diviso.graeshoppe.domain;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Store
 */
@Validated
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-08-02T11:19:44.906+05:30[Asia/Calcutta]")

public class Cart  {
 

  
  @JsonProperty("id")
  private Long id = null;



  @JsonProperty("location")
  private String location = null;



@Override
public String toString() {
	return "cart [id=" + id + ", location=" + location + "]";
}

    }

