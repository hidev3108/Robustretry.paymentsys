package com.example.RPRM.services;

import com.example.RPRM.model.PaymentEntity;
import com.example.RPRM.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserNotificationService userNotificationService;

    /**
     * Process a payment using the Stripe API.
     *
     * @param paymentEntity The payment entity containing payment details.
     * @return The status of the payment.
     */
    @Transactional
    public String processPayment(PaymentEntity paymentEntity) {
        try {
            // Convert amount to cents
            Long amountInCents = (long) (paymentEntity.getAmount() * 100);

            // Create a payment intent
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                    amountInCents, "usd", "Payment for Order #" + paymentEntity.getId()
            );
            // Update payment entity with status
            paymentEntity.setStatus(paymentIntent.getStatus());
            paymentEntity.setPaymentIntentId(paymentIntent.getId());
            paymentRepository.save(paymentEntity);

            return paymentIntent.getStatus();

        } catch (StripeException e) {
            // Handle Stripe exception and notify the user
            paymentEntity.setStatus("failed");
            paymentRepository.save(paymentEntity);
            userNotificationService.notifyUserOfFailure(paymentEntity);

            return "failed";
        }
    }

    /**
     * Retry a failed payment.
     *
     * @param paymentEntity The payment entity containing payment details.
     * @return The status of the payment after retrying.
     */
    @Transactional
    public String retryPayment(PaymentEntity paymentEntity) {
        try {
            // Notify user before the final retry
            userNotificationService.notifyUserBeforeFinalRetry(paymentEntity);

            // Retry the payment
            return processPayment(paymentEntity);

        } catch (Exception e) {
            // Handle any exceptions during retry and notify the user
            paymentEntity.setStatus("failed");
            paymentRepository.save(paymentEntity);
            userNotificationService.notifyUserOfFailure(paymentEntity);

            return "failed";
        }
    }

}
