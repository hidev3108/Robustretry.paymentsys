package com.example.RPRM.controller;

import com.example.RPRM.dto.PaymentRequest;
import com.example.RPRM.model.PaymentEntity;
import com.example.RPRM.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentControllerr {

    @Autowired
    private PaymentService paymentService;
    private SimpleJpaRepository paymentRepository;

    @PostMapping("/process")
    public String processPayment(@RequestBody PaymentRequest paymentRequestDTO) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setUserId(Long.valueOf(String.valueOf(paymentRequestDTO.getClass())));
        paymentEntity.setAmount(paymentRequestDTO.getAmount());

        return paymentService.processPayment(paymentEntity);
    }

    @PostMapping("/retry")
    public String retryPayment(@RequestParam Long paymentId) throws Throwable {
        PaymentEntity paymentEntity = (PaymentEntity) paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        return paymentService.retryPayment(paymentEntity);
    }
}
