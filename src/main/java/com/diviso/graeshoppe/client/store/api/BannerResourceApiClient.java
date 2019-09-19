package com.diviso.graeshoppe.client.store.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.store.ClientConfiguration;

@FeignClient(name="${store.name:store}", url="${store.url:34.74.142.114:8071/}", configuration = ClientConfiguration.class)
public interface BannerResourceApiClient extends BannerResourceApi {
}