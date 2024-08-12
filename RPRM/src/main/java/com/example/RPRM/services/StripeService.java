package com.example.RPRM.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.apikey}")
    private String stripeApiKey;

    public StripeService() {
        initializeStripe();
    }

    /**
     * Initialize the Stripe API with the provided API key.
     */
    private void initializeStripe() {
        Stripe.apiKey = stripeApiKey;
    }

    /**
     * Create a PaymentIntent using the Stripe API.
     *
     * @param amount      Amount to be charged in cents.
     * @param currency    Currency code (e.g., "usd").
     * @param description Description of the payment.
     * @return The created PaymentIntent.
     * @throws StripeException If an error occurs while creating the PaymentIntent.
     */
    public PaymentIntent createPaymentIntent(Long amount, String currency, String description) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setDescription(description)
                .build();

        return PaymentIntent.create(params);
    }
}
