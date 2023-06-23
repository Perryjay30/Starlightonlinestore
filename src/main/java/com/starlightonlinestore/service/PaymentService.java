package com.starlightonlinestore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import com.starlightonlinestore.data.dto.Request.PaymentRequest;
import com.starlightonlinestore.data.dto.Response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class PaymentService {

    private final String SECRET_KEY = System.getenv("PAY_STACK_KEY");

    public PaymentResponse makePaymentForGoods(PaymentRequest paymentRequest) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String urlEndpoint = "https://api.paystack.co/page";
        String objectMapping = objectMapper.writeValueAsString(paymentRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapping);
        Request request = new Request.Builder()
                .url(urlEndpoint)
                .post(requestBody)
                .addHeader("Authorization", "Bearer "+ SECRET_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        try(ResponseBody response = client.newCall(request).execute().body()) {
            Gson gson = new Gson();
            return gson.fromJson(response.string(),PaymentResponse.class);
        }
    }
}
