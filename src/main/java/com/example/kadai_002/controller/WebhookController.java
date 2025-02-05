package com.example.kadai_002.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.repository.UsersRepository;
import com.example.kadai_002.security.UsersDetailsImpl;
import com.example.kadai_002.service.UsersService;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final UserDetailsService userDetailsService;
    private final String stripeApiKey;
    private final String webhookSecret;

    public WebhookController(UsersService usersService,
                             UsersRepository usersRepository,
                             UserDetailsService userDetailsService,
                             @Value("${stripe.api-key}") String stripeApiKey,
                             @Value("${stripe.webhook-secret}") String webhookSecret) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.userDetailsService = userDetailsService;
        this.stripeApiKey = stripeApiKey;
        this.webhookSecret = webhookSecret;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader,
                                                      @AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl) {
        Stripe.apiKey = stripeApiKey;

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            logger.error("Webhook signature verification failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }

        String eventType = event.getType();
        logger.info("=== [WebhookController] Received Stripe event: {}", eventType);

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            return ResponseEntity.ok("Deserialization failed.");
        }

        // 1) サブスク完了の場合の処理
        if ("checkout.session.completed".equals(eventType)) {
            Session session = (Session) deserializer.getObject().get();
            logger.info(">>> checkout.session.completed => session ID: {}", session.getId());

            // メタデータから userEmail を取得
            String userEmail = null;
            if (session.getMetadata() != null) {
                userEmail = session.getMetadata().get("userEmail");
            }
            logger.info(">>> userEmail in metadata = {}", userEmail);

            if (userEmail != null) {
                Users user = usersRepository.findByMailAddress(userEmail);
                if (user == null) {
                    logger.warn(">>> No user found for email: {}", userEmail);
                } else {
                    // subscriptionId、stripeCustomerId の保存
                    String subscriptionId = session.getSubscription();
                    if (subscriptionId != null) {
                        user.setSubscriptionId(subscriptionId);
                    }
                    String stripeCustomerId = session.getCustomer();
                    if (stripeCustomerId != null) {
                        user.setStripeCustomerId(stripeCustomerId);
                    }
                    usersRepository.save(user);
                    logger.info(">>> subscriptionId & stripeCustomerId saved to user: {}, {}", subscriptionId, stripeCustomerId);

                    // updateUserRole の呼び出しと認証情報の更新
                    logger.info(">>> Before calling updateUserRole for user: {}", userEmail);
                    try {
                        usersService.updateUserRole(userEmail, 3);  // 3=ROLE_PRIME
                        // 認証情報を最新状態に更新する（有料会員の場合：ROLE_PRIME）
                        usersService.refreshAuthenticationByRole("ROLE_PRIME", userDetailsService);
                    } catch (Exception e) {
                        logger.error("Exception occurred while updating user role for {}: {}", userEmail, e.getMessage(), e);
                    }
                    logger.info(">>> After calling updateUserRole for user: {}", userEmail);
                    logger.info(">>> Called updateUserRole(userEmail, 3). Possibly ROLE_PRIME is set now.");
                }
            }
        }
        // 2) サブスク削除の場合の処理
        else if ("customer.subscription.deleted".equals(eventType)) {
            Subscription subscription = (Subscription) deserializer.getObject().get();
            String stripeCustomerId = subscription.getCustomer();
            logger.info(">>> Subscription canceled, customer ID: {}", stripeCustomerId);

            Users user = usersRepository.findByStripeCustomerId(stripeCustomerId);
            if (user != null) {
                usersService.updateUserToFreePlan(user.getMailAddress());
                user.setSubscriptionId(null);
                usersRepository.save(user);
                logger.info(">>> User downgraded to FREE (roleId=1). subscriptionId set to null for customer ID: {}", stripeCustomerId);
            } else {
                logger.warn(">>> No user found with stripeCustomerId: {}", stripeCustomerId);
            }
        }

        return ResponseEntity.ok("Webhook handled: " + eventType);
    }
}