/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.diviso.graeshoppe.client.store.api;

import com.diviso.graeshoppe.client.store.model.Store;
import com.diviso.graeshoppe.client.store.model.StoreDTO;
import com.diviso.graeshoppe.client.store.model.StoreSettingsDTO;
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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-02T13:57:13.442+05:30[Asia/Kolkata]")

@Api(value = "StoreResource", description = "the StoreResource API")
public interface StoreResourceApi {

    @ApiOperation(value = "createDeNormalizedStore", nickname = "createDeNormalizedStoreUsingPOST", notes = "", response = Store.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Store.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/stores-denormalized",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<Store> createDeNormalizedStoreUsingPOST(@ApiParam(value = "store" ,required=true )  @Valid @RequestBody Store store);


    @ApiOperation(value = "createStore", nickname = "createStoreUsingPOST", notes = "", response = StoreDTO.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = StoreDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/stores",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.POST)
    ResponseEntity<StoreDTO> createStoreUsingPOST(@ApiParam(value = "storeDTO" ,required=true )  @Valid @RequestBody StoreDTO storeDTO);


    @ApiOperation(value = "create", nickname = "createUsingPOST", notes = "", tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/createStores",
        method = RequestMethod.POST)
    ResponseEntity<Void> createUsingPOST();


    @ApiOperation(value = "deleteStore", nickname = "deleteStoreUsingDELETE", notes = "", tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden") })
    @RequestMapping(value = "/api/stores/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteStoreUsingDELETE(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "findByRegNo", nickname = "findByRegNoUsingGET", notes = "", response = Store.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Store.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/findByRegNo/{regNo}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<Store> findByRegNoUsingGET(@ApiParam(value = "regNo",required=true) @PathVariable("regNo") String regNo);


    @ApiOperation(value = "findStoreSettingsByStoreId", nickname = "findStoreSettingsByStoreIdUsingGET", notes = "", response = StoreSettingsDTO.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = StoreSettingsDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/storesettings/{storeId}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<StoreSettingsDTO> findStoreSettingsByStoreIdUsingGET(@ApiParam(value = "storeId",required=true) @PathVariable("storeId") String storeId);


    @ApiOperation(value = "getAllStores", nickname = "getAllStoresUsingGET", notes = "", response = StoreDTO.class, responseContainer = "List", tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = StoreDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/stores",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<StoreDTO>> getAllStoresUsingGET(@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "getStore", nickname = "getStoreUsingGET", notes = "", response = StoreDTO.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = StoreDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/stores/{id}",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<StoreDTO> getStoreUsingGET(@ApiParam(value = "id",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "searchStores", nickname = "searchStoresUsingGET", notes = "", response = StoreDTO.class, responseContainer = "List", tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = StoreDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/_search/stores",
        produces = "*/*", 
        method = RequestMethod.GET)
    ResponseEntity<List<StoreDTO>> searchStoresUsingGET(@NotNull @ApiParam(value = "query", required = true) @Valid @RequestParam(value = "query", required = true) String query,@ApiParam(value = "Page number of the requested page") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "Size of a page") @Valid @RequestParam(value = "size", required = false) Integer size,@ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.") @Valid @RequestParam(value = "sort", required = false) List<String> sort);


    @ApiOperation(value = "updateStoreDeNormalized", nickname = "updateStoreDeNormalizedUsingPUT", notes = "", response = Store.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Store.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/stores-denormalized",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<Store> updateStoreDeNormalizedUsingPUT(@ApiParam(value = "store" ,required=true )  @Valid @RequestBody Store store);


    @ApiOperation(value = "updateStore", nickname = "updateStoreUsingPUT", notes = "", response = StoreDTO.class, tags={ "store-resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = StoreDTO.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/api/stores",
        produces = "*/*", 
        consumes = "application/json",
        method = RequestMethod.PUT)
    ResponseEntity<StoreDTO> updateStoreUsingPUT(@ApiParam(value = "storeDTO" ,required=true )  @Valid @RequestBody StoreDTO storeDTO);

}
