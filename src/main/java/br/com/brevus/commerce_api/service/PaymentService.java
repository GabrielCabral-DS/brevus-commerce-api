package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.PaymentResponseDTO;
import br.com.brevus.commerce_api.enums.DeliveryStatus;
import br.com.brevus.commerce_api.enums.PaymentMethod;
import br.com.brevus.commerce_api.enums.PaymentStatus;
import br.com.brevus.commerce_api.enums.SaleStatus;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.PaymentMapper;
import br.com.brevus.commerce_api.model.Payment;
import br.com.brevus.commerce_api.model.Sale;
import br.com.brevus.commerce_api.repository.PaymentRepository;
import br.com.brevus.commerce_api.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, SaleRepository saleRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.saleRepository = saleRepository;
        this.paymentMapper = paymentMapper;
    }


    public void savePayment(UUID saleId, BigDecimal amount, String txid){

        if(paymentRepository.existsBySaleId(saleId)){
            throw new IllegalStateException("Já existe pagamento cadastrado para esta venda");
        }

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not found"));

        Payment payment = new Payment();

        payment.setSale(sale);
        payment.setMethod(PaymentMethod.PIX);
        payment.setStatus(PaymentStatus.PAID);
        payment.setAmount(amount);
        payment.setGatewayTransactionId(txid);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setConfirmedAt(LocalDateTime.now());

        sale.setStatus(SaleStatus.PAID);
        sale.setDeliveryStatus(DeliveryStatus.PREPARANDO_PEDIDO);

        paymentRepository.save(payment);
        saleRepository.save(sale);

    }

    public List<PaymentResponseDTO> listAllPayments(){
        List<Payment> paymentList = paymentRepository.findAll();
        return paymentMapper.toDtoList(paymentList);
    }

    public List<PaymentResponseDTO> getRecentPaidPayments(){
        List<Payment> paymentList = paymentRepository.findTop10ByStatusOrderByCreatedAtDesc(PaymentStatus.PAID);
        return paymentMapper.toDtoList(paymentList);
    }

    public BigDecimal getMonthlyRevenue() {

        LocalDate now = LocalDate.now();

        LocalDateTime startOfMonth = now
                .withDayOfMonth(1)
                .atStartOfDay();

        LocalDateTime endOfMonth = now
                .withDayOfMonth(now.lengthOfMonth())
                .atTime(23, 59, 59);

        return paymentRepository.sumPaymentsByStatusAndPeriod(
                PaymentStatus.PAID,
                startOfMonth,
                endOfMonth
        );
    }

}
