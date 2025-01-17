package com.example.kadai_002.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.kadai_002.form.ReserveRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	
	@Value("${stripe.api-key}")
    private String stripeApiKey;
	
	 // セッションを作成し、Stripeに必要な情報を返す
    public String createStripeSession(String houseName, ReserveRegisterForm reserveRegisterForm, HttpServletRequest httpServletRequest) {
    	Stripe.apiKey = stripeApiKey;
        String requestUrl = new String(httpServletRequest.getRequestURL());
        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()   
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(houseName)
                                        .build())
                                .setUnitAmount((long)reserveRegisterForm.getNumberOfPeople())
                                .setCurrency("jpy")                                
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
                .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("houseId", reserveRegisterForm.getHouseId().toString())
                        .putMetadata("userId", reserveRegisterForm.getUserId().toString())
                        .putMetadata("checkinDate", reserveRegisterForm.getCheckinDate())
                        .putMetadata("checkoutDate", reserveRegisterForm.getCheckinTime())
                        .putMetadata("numberOfPeople", reserveRegisterForm.getNumberOfPeople().toString())
                       
                        .build())
                .build();
        try {
            Session session = Session.create(params);
            return session.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    } 
}