package com.diviso.graeshoppe.client.sale.api;

import org.springframework.cloud.openfeign.FeignClient;

import com.diviso.graeshoppe.client.sale.SaleClientConfiguration;

@FeignClient(name="${sale.name:sale}", url="${sale.url}", configuration = SaleClientConfiguration.class)
public interface SaleResourceApiClient extends SaleResourceApi {
}