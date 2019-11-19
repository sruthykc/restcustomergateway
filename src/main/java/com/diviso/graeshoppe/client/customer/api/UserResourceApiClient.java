package com.diviso.graeshoppe.client.customer.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.customer.CustomerClientConfiguration;

@FeignClient(name="${customer.name:customer}", url="${customer.url:dev.ci1.divisosofttech.com:8088/}", configuration = CustomerClientConfiguration.class)
public interface UserResourceApiClient extends UserResourceApi {
}