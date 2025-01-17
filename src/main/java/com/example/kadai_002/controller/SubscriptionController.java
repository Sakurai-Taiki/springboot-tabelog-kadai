package com.example.kadai_002.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
public class SubscriptionController {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    /**
     * サブスクリプション登録ページの表示 (GETメソッド)
     */
    @GetMapping("/subscription")
    public String showSubscriptionPage() {
        return "subscription/index"; 
    }

    /**
     * サブスクリプションのCheckoutセッションを作成 (POSTメソッド)
     */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> requestBody) {
        Stripe.apiKey = stripeApiKey;

        try {
            // 顧客名を受け取る
            String customerName = (String) requestBody.get("name");

            // Stripeの顧客を作成
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setName(customerName)
                .build();

            Customer customer = Customer.create(customerParams);

            // Stripeの価格IDを設定
            String priceId = "price_1QgiLi7vXAaoTR5x3nY3tE0o";

            // Checkoutセッションの作成
            SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customer.getId()) // 顧客IDを指定
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("http://localhost:8081/subscription/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8081/subscription/cancel")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPrice(priceId) // 価格ID
                        .setQuantity(1L) // 数量
                        .build()
                )
                .build();

            Session session = Session.create(params);

            // セッションIDを返す
            Map<String, String> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            // エラーハンドリング
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Checkout session creation failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * サブスクリプションの解約処理 (POSTメソッド)
     */
    @PostMapping("/subscription/cancel")
    public ResponseEntity<Map<String, String>> cancelSubscription(@RequestBody Map<String, String> requestBody) {
        Stripe.apiKey = stripeApiKey;

        String subscriptionId = requestBody.get("subscriptionId");

        try {
            // Stripeサブスクリプションの解約
            Subscription subscription = Subscription.retrieve(subscriptionId);
            subscription.cancel();

            // 成功レスポンスを返す
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("redirectUrl", "/"); // トップページへのリダイレクトURLを含む
            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            // 解約失敗時のレスポンス
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "サブスクリプションの解約に失敗しました: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
   
    @GetMapping("/subscription/cancel")
    public String showCancelPage() {
        return "subscription/cancel"; // templates/subscription/cancel.html
    }
    
}