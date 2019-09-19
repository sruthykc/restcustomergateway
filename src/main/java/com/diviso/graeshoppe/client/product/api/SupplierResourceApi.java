/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.diviso.graeshoppe.client.product.api;

import com.diviso.graeshoppe.client.product.model.SupplierDTO;
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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-07-22T12:40:29.255+05:30[Asia/Calcutta]")

@Api(value = "SupplierResource", description = "the SupplierResource API")
public interface SupplierResourceApi {

    @ApiOperation(value = "createSupplier", nickname = "createSupplierUsingPOST", notes = "", response = SupplierDTO.class, tags={ "supplier-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SupplierDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/suppliers",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<SupplierDTO> createSupplierUsingPOST(@ApiParam(value = "supplierDTO" ,required=true )  @Valid @RequestBody SupplierDTO supplierDTO);


    @ApiOperation(value = "deleteSupplier", nickname = "deleteSupplierUsingDELETE", notes = "", tags={ "supplier-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden") })
    @RequestMapping(value = "/api/suppliers/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteSupplierUsingDELETE(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllSuppliers", nickname = "getAllSuppliersUsingGET", notes = "", response = SupplierDTO.class, responseContainer = "List", tags={ "supplier-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SupplierDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/suppliers",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<SupplierDTO>> getAllSuppliersUsingGET(@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "getSupplier", nickname = "getSupplierUsingGET", notes = "", response = SupplierDTO.class, tags={ "supplier-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SupplierDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/suppliers/{id}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<SupplierDTO> getSupplierUsingGET(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "searchSuppliers", nickname = "searchSuppliersUsingGET", notes = "", response = SupplierDTO.class, responseContainer = "List", tags={ "supplier-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SupplierDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/_search/suppliers",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<SupplierDTO>> searchSuppliersUsingGET(@NotNull @ApiParam(value = "query", required = true) @Valid @RequestParam(value = "query", required = true) String query,@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "updateSupplier", nickname = "updateSupplierUsingPUT", notes = "", response = SupplierDTO.class, tags={ "supplier-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SupplierDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/suppliers",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<SupplierDTO> updateSupplierUsingPUT(@ApiParam(value = "supplierDTO" ,required=true )  @Valid @RequestBody SupplierDTO supplierDTO);

}
