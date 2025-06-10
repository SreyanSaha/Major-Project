package com.help.controller;

import com.help.service.RazorPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
