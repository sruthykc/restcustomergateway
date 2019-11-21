package com.diviso.graeshoppe.client.payment.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.diviso.graeshoppe.client.payment.PaymentClientConfiguration;

@FeignClient(name="${payment.name:payment}", url="${payment.url:dev.ci2.divisosofttech.com:9090/}", configuration = PaymentClientConfiguration.class)
public interface RazorpayCommandResourceApiClient extends RazorpayCommandResourceApi {
}