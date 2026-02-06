package com.viafluvial.srvusuario.domain.entity;

import com.viafluvial.srvusuario.domain.entity.converter.DeviceTypeConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions", indexes = {
    @Index(name = "idx_user_sessions_user_id", columnList = "user_id"),
    @Index(name = "idx_user_sessions_token", columnList = "token"),
    @Index(name = "idx_user_sessions_expires_at", columnList = "expires_at")
})
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "device_type", length = 50)
    @Convert(converter = DeviceTypeConverter.class)
    private DeviceType deviceType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_activity")
    private LocalDateTime lastActivity = LocalDateTime.now();

    public enum DeviceType {
        MOBILE, DESKTOP, TABLET
    }
}
