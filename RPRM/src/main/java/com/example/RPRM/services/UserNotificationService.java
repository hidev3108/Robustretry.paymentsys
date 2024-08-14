package com.example.RPRM.services;

import com.example.RPRM.model.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService {

    @Autowired
    private JavaMailSender emailSender;

    // Notify user of payment failure
    public void notifyUserOfFailure(PaymentEntity paymentEntity) {
        String subject = "Payment Failed";
        String text = String.format(
                "Dear User,\n\nYour payment attempt for amount $%.2f has failed.\n\nError Type: %s\nPlease check your payment details and try again.\n\nThank you.",
                paymentEntity.getAmount(), paymentEntity.getErrorType().name()
        );

        sendEmail(paymentEntity.getUserId(), subject, text);
    }

    // Notify user before the final retry attempt
    public void notifyUserBeforeFinalRetry(PaymentEntity paymentEntity) {
        String subject = "Final Payment Retry Attempt";
        String text = String.format(
                "Dear User,\n\nWe are about to make a final attempt to process your payment of $%.2f. Please ensure that your payment details are correct.\n\nIf you need assistance, please update your payment information or contact our support team.\n\nThank you.",
                paymentEntity.getAmount()
        );

        PaymentEntity paymentEntitgitgitgy = null;
        sendEmail(paymentEntitgitgitgy.getUserId(), subject, text);
    }

    // Helper method to send email
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }























}
