package com.help.service;

import com.help.configuration.RazorPayConfig;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RazorPayService {
    private static  final int AMOUNT = 299;

    private final RazorpayClient client;

    @Autowired
    public RazorPayService(RazorPayConfig razorPayConfig) throws RazorpayException {
        this.client = new RazorpayClient(razorPayConfig.getKeyId(), razorPayConfig.getKeySecret());

    }

    public String createOrder(){
        try{
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", AMOUNT * 100); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rh_" + UUID.randomUUID().toString() + "_" +LocalDateTime.now().toString() + "_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1);

            Order order = client.orders.create(orderRequest);
            return order.toString(); // You can return specific fields if needed
        }catch (Exception e){e.fillInStackTrace();}
        return null;
    }
}
