package com.viafluvial.srvusuario.adapters.out.persistence.entity;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.converter.CardBrandConverter;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.converter.PaymentTypeConverter;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.converter.PixKeyTypeConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_methods", indexes = {
    @Index(name = "idx_payment_methods_user_id", columnList = "user_id")
})
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "payment_type", nullable = false, length = 20)
    @Convert(converter = PaymentTypeConverter.class)
    private PaymentType paymentType;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", length = 20)
    @Convert(converter = CardBrandConverter.class)
    private CardBrand cardBrand;

    @Column(name = "card_expiry_month", length = 2)
    private String cardExpiryMonth;

    @Column(name = "card_expiry_year", length = 4)
    private String cardExpiryYear;

    @Column(name = "card_holder_name", length = 255)
    private String cardHolderName;

    @Column(name = "payment_gateway_token", length = 255)
    private String paymentGatewayToken;

    @Column(name = "pix_key", length = 100)
    private String pixKey;

    @Column(name = "pix_key_type", length = 20)
    @Convert(converter = PixKeyTypeConverter.class)
    private PixKeyType pixKeyType;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum PaymentType {
        CREDIT_CARD, DEBIT_CARD, PIX
    }

    public enum CardBrand {
        VISA, MASTERCARD, ELO, AMEX, HIPERCARD, DINERS
    }

    public enum PixKeyType {
        CPF, CNPJ, EMAIL, PHONE, RANDOM
    }
}
