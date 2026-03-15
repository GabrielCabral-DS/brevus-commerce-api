package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.PaymentResponseDTO;
import br.com.brevus.commerce_api.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> listAllPayments(){
        List<PaymentResponseDTO> responseDTOList = paymentService.listAllPayments();
        return ResponseEntity.ok().body(responseDTOList);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PaymentResponseDTO>> listLast10Payments(){
        List<PaymentResponseDTO> responseDTOList = paymentService.getRecentPaidPayments();
        return ResponseEntity.ok().body(responseDTOList);
    }

    @GetMapping("/revenue/month")
    public ResponseEntity<BigDecimal> getMonthlyRevenue() {
        return ResponseEntity.ok(paymentService.getMonthlyRevenue());
    }
}
