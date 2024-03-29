/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.diviso.graeshoppe.client.report.api;

import com.diviso.graeshoppe.client.report.model.AuxItemDTO;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-10-15T11:16:15.523+05:30[Asia/Kolkata]")

@Api(value = "AuxItemResource", description = "the AuxItemResource API")
public interface AuxItemResourceApi {

    @ApiOperation(value = "createAuxItem", nickname = "createAuxItemUsingPOST", notes = "", response = AuxItemDTO.class, tags={ "aux-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxItemDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/aux-items",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<AuxItemDTO> createAuxItemUsingPOST(@ApiParam(value = "auxItemDTO" ,required=true )  @Valid @RequestBody AuxItemDTO auxItemDTO);


    @ApiOperation(value = "deleteAuxItem", nickname = "deleteAuxItemUsingDELETE", notes = "", tags={ "aux-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden") })
    @RequestMapping(value = "/api/aux-items/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteAuxItemUsingDELETE(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllAuxItems", nickname = "getAllAuxItemsUsingGET", notes = "", response = AuxItemDTO.class, responseContainer = "List", tags={ "aux-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxItemDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/aux-items",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<AuxItemDTO>> getAllAuxItemsUsingGET(@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "getAuxItem", nickname = "getAuxItemUsingGET", notes = "", response = AuxItemDTO.class, tags={ "aux-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxItemDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/aux-items/{id}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<AuxItemDTO> getAuxItemUsingGET(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "searchAuxItems", nickname = "searchAuxItemsUsingGET", notes = "", response = AuxItemDTO.class, responseContainer = "List", tags={ "aux-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxItemDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/_search/aux-items",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<AuxItemDTO>> searchAuxItemsUsingGET(@NotNull @ApiParam(value = "query", required = true) @Valid @RequestParam(value = "query", required = true) String query,@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "updateAuxItem", nickname = "updateAuxItemUsingPUT", notes = "", response = AuxItemDTO.class, tags={ "aux-item-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuxItemDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/aux-items",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<AuxItemDTO> updateAuxItemUsingPUT(@ApiParam(value = "auxItemDTO" ,required=true )  @Valid @RequestBody AuxItemDTO auxItemDTO);

}
