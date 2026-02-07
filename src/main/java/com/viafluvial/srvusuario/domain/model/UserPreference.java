package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.DomainException;
import com.viafluvial.srvusuario.common.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserPreference {

    private UUID id;
    private UUID userId;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
    private Boolean receivePromotions;
    private Boolean receiveTripUpdates;
    private String language;
    private String theme;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserPreference() {
    }

    private UserPreference(UUID id, UUID userId, Boolean emailNotifications, Boolean smsNotifications,
                           Boolean pushNotifications, Boolean receivePromotions, Boolean receiveTripUpdates,
                           String language, String theme, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.emailNotifications = emailNotifications;
        this.smsNotifications = smsNotifications;
        this.pushNotifications = pushNotifications;
        this.receivePromotions = receivePromotions;
        this.receiveTripUpdates = receiveTripUpdates;
        this.language = language;
        this.theme = theme;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validate() {
        if (userId == null) {
            throw new InvalidUserPreferenceException("userId e obrigatorio");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public Boolean getSmsNotifications() {
        return smsNotifications;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public Boolean getReceivePromotions() {
        return receivePromotions;
    }

    public Boolean getReceiveTripUpdates() {
        return receiveTripUpdates;
    }

    public String getLanguage() {
        return language;
    }

    public String getTheme() {
        return theme;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID userId;
        private Boolean emailNotifications = true;
        private Boolean smsNotifications = false;
        private Boolean pushNotifications = true;
        private Boolean receivePromotions = true;
        private Boolean receiveTripUpdates = true;
        private String language = "pt-BR";
        private String theme = "light";
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder emailNotifications(Boolean emailNotifications) {
            this.emailNotifications = emailNotifications;
            return this;
        }

        public Builder smsNotifications(Boolean smsNotifications) {
            this.smsNotifications = smsNotifications;
            return this;
        }

        public Builder pushNotifications(Boolean pushNotifications) {
            this.pushNotifications = pushNotifications;
            return this;
        }

        public Builder receivePromotions(Boolean receivePromotions) {
            this.receivePromotions = receivePromotions;
            return this;
        }

        public Builder receiveTripUpdates(Boolean receiveTripUpdates) {
            this.receiveTripUpdates = receiveTripUpdates;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder theme(String theme) {
            this.theme = theme;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserPreference build() {
            UserPreference preference = new UserPreference(id, userId, emailNotifications, smsNotifications,
                pushNotifications, receivePromotions, receiveTripUpdates, language, theme, createdAt, updatedAt);
            preference.validate();
            return preference;
        }
    }

    public static class InvalidUserPreferenceException extends DomainException {
        public InvalidUserPreferenceException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
}
