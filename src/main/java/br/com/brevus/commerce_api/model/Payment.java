package br.com.brevus.commerce_api.model;

import br.com.brevus.commerce_api.enums.PaymentMethod;
import br.com.brevus.commerce_api.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String txid;
    private BigDecimal amount;

    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}
