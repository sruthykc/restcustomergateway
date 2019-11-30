/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.diviso.graeshoppe.client.product.api;

import com.diviso.graeshoppe.client.product.model.AuxilaryLineItem;
import com.diviso.graeshoppe.client.product.model.AuxilaryLineItemDTO;
import java.util.List;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-11-29T15:19:12.817+05:30[Asia/Kolkata]")

@Api(value = "AuxilaryLineItemResource", description = "the AuxilaryLineItemResource API")
public interface AuxilaryLineItemResourceApi {

    @ApiOperation(value = "createAuxilaryLineItem", nickname = "createAuxilaryLineItemUsingPOST", notes = "", response = AuxilaryLineItemDTO.class, tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxilaryLineItemDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/auxilary-line-items",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<AuxilaryLineItemDTO> createAuxilaryLineItemUsingPOST(@ApiParam(value = "auxilaryLineItemDTO" ,required=true )  @Valid @RequestBody AuxilaryLineItemDTO auxilaryLineItemDTO);


    @ApiOperation(value = "deleteAuxilaryLineItem", nickname = "deleteAuxilaryLineItemUsingDELETE", notes = "", tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden") })
    @RequestMapping(value = "/api/auxilary-line-items/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteAuxilaryLineItemUsingDELETE(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllAuxilaryLineItems", nickname = "getAllAuxilaryLineItemsUsingGET", notes = "", response = AuxilaryLineItemDTO.class, responseContainer = "List", tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxilaryLineItemDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/auxilary-line-items",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<AuxilaryLineItemDTO>> getAllAuxilaryLineItemsUsingGET(@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "getAuxilaryLineItem", nickname = "getAuxilaryLineItemUsingGET", notes = "", response = AuxilaryLineItemDTO.class, tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxilaryLineItemDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/auxilary-line-items/{id}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<AuxilaryLineItemDTO> getAuxilaryLineItemUsingGET(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "listToDto", nickname = "listToDtoUsingPOST", notes = "", response = AuxilaryLineItemDTO.class, responseContainer = "List", tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxilaryLineItemDTO.class, responseContainer = "List"),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/auxilary-line-items/toDto",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<List<AuxilaryLineItemDTO>> listToDtoUsingPOST(@ApiParam(value = "auxilaryLineItems" ,required=true )  @Valid @RequestBody List<AuxilaryLineItem> auxilaryLineItem);


    @ApiOperation(value = "searchAuxilaryLineItems", nickname = "searchAuxilaryLineItemsUsingGET", notes = "", response = AuxilaryLineItemDTO.class, responseContainer = "List", tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxilaryLineItemDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/_search/auxilary-line-items",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<AuxilaryLineItemDTO>> searchAuxilaryLineItemsUsingGET(@NotNull @ApiParam(value = "query", required = true) @Valid @RequestParam(value = "query", required = true) String query,@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "updateAuxilaryLineItem", nickname = "updateAuxilaryLineItemUsingPUT", notes = "", response = AuxilaryLineItemDTO.class, tags={ "auxilary-line-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxilaryLineItemDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/auxilary-line-items",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<AuxilaryLineItemDTO> updateAuxilaryLineItemUsingPUT(@ApiParam(value = "auxilaryLineItemDTO" ,required=true )  @Valid @RequestBody AuxilaryLineItemDTO auxilaryLineItemDTO);

}
