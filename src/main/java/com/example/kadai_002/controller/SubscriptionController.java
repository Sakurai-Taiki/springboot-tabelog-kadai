package com.example.kadai_002.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.repository.RoleRepository;
import com.example.kadai_002.repository.UsersRepository;
import com.example.kadai_002.service.UsersService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;

    public SubscriptionController(UsersRepository usersRepository,
                                  UsersService usersService,
                                  RoleRepository roleRepository,
                                  UserDetailsService userDetailsService) {
        this.usersRepository = usersRepository;
        this.usersService = usersService;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("")
    public String showSubscriptionPage() {
        return "subscription/index";
    }

    /**
     * Checkout Session作成 (サブスクリプション)
     */
    @PostMapping("/create-checkout-session")
    @ResponseBody
    public Map<String, Object> createCheckoutSession() {
        Stripe.apiKey = stripeApiKey;
        Map<String, Object> response = new HashMap<>();
        try {
            // ログイン中ユーザー情報
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = auth.getName();

            Users user = usersRepository.findByMailAddress(userEmail);
            if (user == null) {
                response.put("error", "User not found for email: " + userEmail);
                return response;
            }

            Map<String, String> metadata = new HashMap<>();
            metadata.put("userEmail", userEmail);
            metadata.put("roleId", "3"); // ROLE_PRIME = 3

            SessionCreateParams.Builder paramBuilder = SessionCreateParams.builder()
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPrice("price_1QhocDFoKQ3torxM5OL3enTl")
                        .setQuantity(1L)
                        .build()
                )
                .putAllMetadata(metadata)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("https://nagoya-meshi-5b09ea50de07.herokuapp.com/subscription/success")
                .setCancelUrl("https://nagoya-meshi-5b09ea50de07.herokuapp.com/subscription/cancel");

            // 既に user.stripeCustomerId があればそれを使う
            if (user.getStripeCustomerId() != null) {
                paramBuilder.setCustomer(user.getStripeCustomerId());
            }

            Session session = Session.create(paramBuilder.build());

            response.put("sessionId", session.getId());
            response.put("sessionUrl", session.getUrl());

        } catch (StripeException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * [POST] キャンセル (自前実装)
     * キャンセル後はsubscription-cancelled画面へリダイレクトする
     */
    @PostMapping("/cancel-subscription")
    public String cancelSubscription(@RequestParam String userEmail) {
        Stripe.apiKey = stripeApiKey;

        // ユーザーを検索
        Users user = usersRepository.findByMailAddress(userEmail);
        if (user == null) {
            return "redirect:/subscription/cancelled?error=user_not_found";
        }

        try {
            String subscriptionId = user.getSubscriptionId();
            if (subscriptionId == null) {
                return "redirect:/subscription/cancelled?error=no_subscription";
            }

            // Stripe サブスクを取得 & キャンセル
            Subscription subscription = Subscription.retrieve(subscriptionId);
            Map<String, Object> cancelParams = new HashMap<>();
            cancelParams.put("cancel_at_period_end", false); // 今すぐ解約
            Subscription canceledSubscription = subscription.cancel(cancelParams);

            if ("canceled".equals(canceledSubscription.getStatus())) {
                usersService.updateUserToFreePlan(userEmail);
                return "redirect:/subscription/cancelled";
            } else {
                return "redirect:/subscription/cancelled?error=status_" + canceledSubscription.getStatus();
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return "redirect:/subscription/cancelled?error=stripeException";
        }
    }
    
    /**
     * サブスクリプション登録成功画面表示時に最新の認証情報を反映する
     */
    @GetMapping("/success")
    public String showSuccessPage(HttpServletRequest request) {
        // 現在の認証情報を取得
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = currentAuth.getName();
        // 最新のユーザー情報を取得
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userEmail);
        System.out.println("Updated user authorities: " + updatedUserDetails.getAuthorities());
        // SecurityContext の更新
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                currentAuth.getCredentials(),
                updatedUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return "subscription/success";
    }

    /**
     * 解約完了画面表示時に最新の認証情報を反映する
     */
    @GetMapping("/cancelled")
    public String showCancelled(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.isAuthenticated()) {
            String userEmail = currentAuth.getName();
            UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(userEmail);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    currentAuth.getCredentials(),
                    updatedUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        return "subscription/subscription-cancelled";
    }
}