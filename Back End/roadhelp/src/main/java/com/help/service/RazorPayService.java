package com.help.service;

import com.help.configuration.RazorPayConfig;
import com.help.model.User;
import com.help.model.UserSubscriptionLog;
import com.help.repository.UserRepository;
import com.help.repository.UserSubscriptionLogRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RazorPayService {
    private static  final int AMOUNT = 299;
    private final RazorPayConfig razorPayConfig;
    private final RazorpayClient client;
    private final UserRepository userRepository;
    private final UserSubscriptionLogRepository userSubscriptionLogRepository;

    @Autowired
    public RazorPayService(RazorPayConfig razorPayConfig, UserSubscriptionLogRepository userSubscriptionLogRepository, UserRepository userRepository) throws RazorpayException {
        this.client = new RazorpayClient(razorPayConfig.getKeyId(), razorPayConfig.getKeySecret());
        this.razorPayConfig=razorPayConfig;
        this.userRepository=userRepository;
        this.userSubscriptionLogRepository=userSubscriptionLogRepository;
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            String secret = razorPayConfig.getKeySecret();
            String data = orderId + "|" + paymentId;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKey);

            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String generatedSignature = Hex.encodeHexString(hash);

            System.out.println("Generated: " + generatedSignature);
            System.out.println("Provided:  " + signature);
            System.out.println(generatedSignature.equals(signature.trim()));
            return generatedSignature.equals(signature.trim()) && startSubscription(paymentId, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            initiateRefund(paymentId);
        }
        System.out.println("FALSE FALSE");
        return false;
    }

    private boolean startSubscription(String paymentId, String orderId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<UserSubscriptionLog> userSubscriptionLog = userSubscriptionLogRepository.findByUserId(user.get().getUserId());
        if(userSubscriptionLog.isEmpty()){
            UserSubscriptionLog log=new UserSubscriptionLog(user.get().getUserId(), (short) 1, paymentId, orderId);
            userSubscriptionLogRepository.save(log);
            return true;
        }
        else if(userSubscriptionLog.get().getEndDate().isBefore(LocalDateTime.now())){
            UserSubscriptionLog log=new UserSubscriptionLog(user.get().getUserId(), (short) 1, paymentId, orderId);
            userSubscriptionLogRepository.save(log);
            return true;
        }
        else if(userSubscriptionLog.get().getEndDate().isAfter(LocalDateTime.now()))
            throw new IllegalStateException("Subscription is not expired.");
        return false;
    }

    private void initiateRefund(String paymentID) {
        try{
            RazorpayClient razorpay = new RazorpayClient(razorPayConfig.getKeyId(), razorPayConfig.getKeySecret());
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("payment_id", paymentID);
            refundRequest.put("amount", (AMOUNT*100));
            Refund refund = razorpay.payments.refund(refundRequest);
            System.out.println(refund.toString());
        }catch (Exception e){e.printStackTrace();}
    }

    public String createOrder(){
        try{
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", AMOUNT * 100); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rh_" + UUID.randomUUID().toString());
            orderRequest.put("payment_capture", 1);

            Order order = client.orders.create(orderRequest);
            return order.get("id");
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
}
