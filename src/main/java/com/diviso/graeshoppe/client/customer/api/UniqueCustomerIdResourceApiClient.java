package com.diviso.graeshoppe.client.customer.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.customer.CustomerClientConfiguration;

@FeignClient(name="${customer.name:customer}", url="${customer.url:35.238.35.19:8088/}", configuration = CustomerClientConfiguration.class)
public interface UniqueCustomerIdResourceApiClient extends UniqueCustomerIdResourceApi {
}