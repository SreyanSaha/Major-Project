package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.PaymentVerificationData;
import com.help.model.Post;
import com.help.service.RazorPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final RazorPayService razorPayService;

    @Autowired
    public PaymentController(RazorPayService razorPayService){
        this.razorPayService=razorPayService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(){
        String response = razorPayService.createOrder();
        if(response==null)return ResponseEntity.status(HttpStatus.ACCEPTED).body("Payment failed.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/verify-start-subscription")
    public ResponseEntity<?> paymentVerifyStartSubscription(@RequestBody PaymentVerificationData paymentVerificationData){
        boolean response=razorPayService.verifyPaymentSignature(paymentVerificationData.getRazorpay_order_id(),
                                                                paymentVerificationData.getRazorpay_payment_id(),
                                                                paymentVerificationData.getRazorpay_signature());
        if(!response)return ResponseEntity.status(HttpStatus.ACCEPTED).body(false);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
