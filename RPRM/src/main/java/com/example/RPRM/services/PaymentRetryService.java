package com.example.RPRM.services;

import com.example.RPRM.dto.PaymentRequest;
import com.example.RPRM.model.PaymentEntity;
import com.example.RPRM.model.PaymentErrorType;
import com.example.RPRM.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Service
public class PaymentRetryService {
    @Autowired
    private PaymentRepository paymentRepository;

    public void processPayment(PaymentRequest paymentRequest){
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setUserId(Long.valueOf(paymentRequest.getUserId()));
        paymentEntity.setAmount(paymentRequest.getAmount());
        paymentEntity.setPaymentMethod(paymentRequest.getPaymentMethod());
        paymentEntity.setAmount(Double.valueOf(paymentRequest.getRetryCount()+1));
        paymentEntity.setCreatedDate(LocalDateTime.now());

        boolean success = processPaymentLogic(paymentEntity);
        paymentEntity.setSuccessful(success);
        paymentEntity.setUpdatedDate(LocalDateTime.now());
        paymentRepository.save(paymentEntity);
    }


    private boolean processPaymentLogic(PaymentEntity paymentEntity) {
        boolean paymentSuccesful = new Random().nextBoolean();
        if(paymentSuccesful){
            System.out.println("Payment succeeded for user: " + paymentEntity.getUserId());
            return true;
        }else{
            System.out.println("Payment failed for user: " + paymentEntity.getUserId());
            paymentEntity.setErrorType(PaymentErrorType.TRANSIENT); // Set error type
            return false;
        }

    }


}
