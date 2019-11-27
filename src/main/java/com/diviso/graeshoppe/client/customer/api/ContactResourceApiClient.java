package com.diviso.graeshoppe.client.customer.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.customer.ClientConfiguration;

@FeignClient(name="${customer.name:customer}", url="${customer.url:35.238.35.19:8088/}", configuration = ClientConfiguration.class)
public interface ContactResourceApiClient extends ContactResourceApi {
}