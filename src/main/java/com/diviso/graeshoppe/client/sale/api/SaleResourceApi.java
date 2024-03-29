/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.diviso.graeshoppe.client.sale.api;

import com.diviso.graeshoppe.client.sale.model.SaleDTO;
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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-05-18T09:48:16.188+05:30[Asia/Kolkata]")

@Api(value = "SaleResource", description = "the SaleResource API")
public interface SaleResourceApi {

    @ApiOperation(value = "createSale", nickname = "createSaleUsingPOST", notes = "", response = SaleDTO.class, tags={ "sale-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SaleDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/sales",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<SaleDTO> createSaleUsingPOST(@ApiParam(value = "saleDTO" ,required=true )  @Valid @RequestBody SaleDTO saleDTO);


    @ApiOperation(value = "deleteSale", nickname = "deleteSaleUsingDELETE", notes = "", tags={ "sale-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden") })
    @RequestMapping(value = "/api/sales/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteSaleUsingDELETE(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "getAllSales", nickname = "getAllSalesUsingGET", notes = "", response = SaleDTO.class, responseContainer = "List", tags={ "sale-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SaleDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/sales",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<SaleDTO>> getAllSalesUsingGET(@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "getSale", nickname = "getSaleUsingGET", notes = "", response = SaleDTO.class, tags={ "sale-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SaleDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/sales/{id}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<SaleDTO> getSaleUsingGET(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "searchSales", nickname = "searchSalesUsingGET", notes = "", response = SaleDTO.class, responseContainer = "List", tags={ "sale-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SaleDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/_search/sales",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<SaleDTO>> searchSalesUsingGET(@NotNull @ApiParam(value = "query", required = true) @Valid @RequestParam(value = "query", required = true) String query,@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "updateSale", nickname = "updateSaleUsingPUT", notes = "", response = SaleDTO.class, tags={ "sale-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SaleDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/sales",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<SaleDTO> updateSaleUsingPUT(@ApiParam(value = "saleDTO" ,required=true )  @Valid @RequestBody SaleDTO saleDTO);

}
