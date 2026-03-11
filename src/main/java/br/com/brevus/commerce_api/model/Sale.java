package br.com.brevus.commerce_api.model;

import br.com.brevus.commerce_api.enums.DeliveryStatus;
import br.com.brevus.commerce_api.enums.SaleStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime saleDate;

    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "delivery_code", length = 6)
    private String deliveryCode;

    @Column(name = "delivery_code_confirmed")
    private Boolean deliveryCodeConfirmed = false;

    @Column(name = "delivery_code_confirmed_at")
    private LocalDateTime deliveryCodeConfirmedAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> items;

    @OneToOne(mappedBy = "sale", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        this.saleDate = LocalDateTime.now();
    }

    public Sale() {
    }

    public Sale(UUID id, LocalDateTime saleDate, SaleStatus status, BigDecimal totalAmount, DeliveryStatus deliveryStatus, String deliveryCode, Boolean deliveryCodeConfirmed, LocalDateTime deliveryCodeConfirmedAt, User client, User seller, Address deliveryAddress, List<SaleItem> items, Payment payment) {
        this.id = id;
        this.saleDate = saleDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.deliveryStatus = deliveryStatus;
        this.deliveryCode = deliveryCode;
        this.deliveryCodeConfirmed = deliveryCodeConfirmed;
        this.deliveryCodeConfirmedAt = deliveryCodeConfirmedAt;
        this.client = client;
        this.seller = seller;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.payment = payment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public Boolean getDeliveryCodeConfirmed() {
        return deliveryCodeConfirmed;
    }

    public void setDeliveryCodeConfirmed(Boolean deliveryCodeConfirmed) {
        this.deliveryCodeConfirmed = deliveryCodeConfirmed;
    }

    public LocalDateTime getDeliveryCodeConfirmedAt() {
        return deliveryCodeConfirmedAt;
    }

    public void setDeliveryCodeConfirmedAt(LocalDateTime deliveryCodeConfirmedAt) {
        this.deliveryCodeConfirmedAt = deliveryCodeConfirmedAt;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}



