package com.diviso.graeshoppe.client.order.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.order.OrderClientConfiguration;

@FeignClient(name="${order.name:order}", url="${order.url:35.238.35.19:8088/}", configuration = OrderClientConfiguration.class)
public interface ApprovalDetailsResourceApiClient extends ApprovalDetailsResourceApi {
}
