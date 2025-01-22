package com.example.kadai_002.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.billingportal.Session;
import com.stripe.param.CustomerListParams;
import com.stripe.param.billingportal.SessionCreateParams;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SubscriptionController {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @GetMapping("/customer-portal")
    public void redirectToCustomerPortal(HttpServletResponse response) {
        Stripe.apiKey = stripeApiKey;

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            String customerId = findCustomerIdByUserId(userId);

            SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customerId)
                .setReturnUrl("http://localhost:8080/")
                .build();

            Session session = Session.create(params);
            response.sendRedirect(session.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendRedirect("/error?message=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8.toString()));
            } catch (Exception redirectException) {
                redirectException.printStackTrace();
            }
        }
    }

    private String findCustomerIdByUserId(String userId) throws Exception {
        String startingAfter = null;

        while (true) {
            CustomerListParams.Builder paramsBuilder = CustomerListParams.builder().setLimit((long) 100);
            if (startingAfter != null) {
                paramsBuilder.setStartingAfter(startingAfter);
            }

            List<Customer> customers = Customer.list(paramsBuilder.build()).getData();

            for (Customer customer : customers) {
                if (customer.getMetadata() != null && userId.equals(customer.getMetadata().get("userId"))) {
                    return customer.getId();
                }
            }

            if (customers.isEmpty()) {
                break;
            }

            startingAfter = customers.get(customers.size() - 1).getId();
        }

        return createNewStripeCustomer(userId);
    }

    private String createNewStripeCustomer(String userId) throws Exception {
        // メールアドレスを設定
        String email = userId.contains("@") ? userId : userId + "@example.com";

        // メタデータに userId を保存
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", userId);

        // Stripe 顧客作成用パラメータ
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("email", email); // 適切なメールアドレスを設定
        createParams.put("metadata", metadata);

        // Stripe 顧客を作成
        Customer newCustomer = Customer.create(createParams);

        System.out.println("新規 Stripe 顧客が作成されました: " + newCustomer.getId());
        return newCustomer.getId(); // 新しく作成した顧客 ID を返す
    }

    @GetMapping("/cancel-subscription")
    public void cancelSubscription(HttpServletResponse response) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            String customerId = findCustomerIdByUserId(userId);
            cancelAllSubscriptions(customerId);

            response.sendRedirect("/subscription-cancelled-successfully");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8.toString());
                response.sendRedirect("/error?message=" + errorMessage);
            } catch (Exception redirectException) {
                redirectException.printStackTrace();
            }
        }
    }

    private void cancelAllSubscriptions(String customerId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);

        List<Subscription> subscriptions = Subscription.list(params).getData();

        if (subscriptions.isEmpty()) {
            throw new Exception("顧客IDに対応するサブスクリプションが見つかりませんでした");
        }

        for (Subscription subscription : subscriptions) {
            subscription.cancel();
        }
    }
}

