package com.diviso.graeshoppe.client.offer.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.offer.OfferClientConfiguration;

@FeignClient(name="${offer.name:offer}", url="${offer.url:dev.ci2.divisosofttech.com:8095/Offer}", configuration = OfferClientConfiguration.class)
public interface AggregateQueryResourceApiClient extends AggregateQueryResourceApi {
}