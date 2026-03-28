package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.DashboardSummaryResponseDTO;
import br.com.brevus.commerce_api.dto.PaymentResponseDTO;
import br.com.brevus.commerce_api.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments Client")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List", description = "List all payments")
    public ResponseEntity<List<PaymentResponseDTO>> listAllPayments(){
        List<PaymentResponseDTO> responseDTOList = paymentService.listAllPayments();
        return ResponseEntity.ok().body(responseDTOList);
    }

    @GetMapping("/dashboard-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Dashboard Summary", description = "Get a summary of the dashboard with total revenue, total products, total sales and total customers")
    public ResponseEntity<DashboardSummaryResponseDTO> getDashBoardSummary() {
        DashboardSummaryResponseDTO summaryResponseDTO = paymentService.getDashboardSummary();
        return ResponseEntity.ok().body(summaryResponseDTO);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "List Recent", description = "List the last 10 payments")
    public ResponseEntity<List<PaymentResponseDTO>> listLast10Payments(){
        List<PaymentResponseDTO> responseDTOList = paymentService.getRecentPaidPayments();
        return ResponseEntity.ok().body(responseDTOList);
    }

    @GetMapping("/revenue/month")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete", description = "Get the total revenue for the current month")
    public ResponseEntity<BigDecimal> getMonthlyRevenue() {
        return ResponseEntity.ok(paymentService.getMonthlyRevenue());
    }
}
