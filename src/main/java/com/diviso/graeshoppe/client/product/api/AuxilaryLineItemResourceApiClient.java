package com.diviso.graeshoppe.client.product.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.product.ClientConfiguration;

@FeignClient(name="${product.name:product}", url="${product.url:dev.ci1.divisosofttech.com:8084/}", configuration = ClientConfiguration.class)
public interface AuxilaryLineItemResourceApiClient extends AuxilaryLineItemResourceApi {
}