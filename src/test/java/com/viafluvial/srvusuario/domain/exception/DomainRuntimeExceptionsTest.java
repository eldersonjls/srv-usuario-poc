package com.viafluvial.srvusuario.domain.exception;

import com.viafluvial.srvusuario.domain.model.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DomainRuntimeExceptionsTest {

    @Test
    void boatmanNotFoundExceptionShouldExposeIdsAndMessage() {
        UUID id = UUID.randomUUID();

        BoatmanNotFoundException byBoatmanId = new BoatmanNotFoundException(id, false);
        assertThat(byBoatmanId.getBoatmanId()).isEqualTo(id);
        assertThat(byBoatmanId.getUserId()).isNull();
        assertThat(byBoatmanId.getMessage()).contains("ID").contains(id.toString());

        BoatmanNotFoundException byUserId = new BoatmanNotFoundException(id, true);
        assertThat(byUserId.getBoatmanId()).isNull();
        assertThat(byUserId.getUserId()).isEqualTo(id);
        assertThat(byUserId.getMessage()).contains("user_id").contains(id.toString());
    }

    @Test
    void passengerNotFoundExceptionShouldExposeIdsAndMessage() {
        UUID id = UUID.randomUUID();

        PassengerNotFoundException byPassengerId = new PassengerNotFoundException(id, false);
        assertThat(byPassengerId.getPassengerId()).isEqualTo(id);
        assertThat(byPassengerId.getUserId()).isNull();
        assertThat(byPassengerId.getMessage()).contains("ID").contains(id.toString());

        PassengerNotFoundException byUserId = new PassengerNotFoundException(id, true);
        assertThat(byUserId.getPassengerId()).isNull();
        assertThat(byUserId.getUserId()).isEqualTo(id);
        assertThat(byUserId.getMessage()).contains("user_id").contains(id.toString());
    }

    @Test
    void userNotFoundExceptionShouldSupportIdAndEmailConstructors() {
        UUID id = UUID.randomUUID();

        UserNotFoundException byId = new UserNotFoundException(id);
        assertThat(byId.getUserId()).isEqualTo(id);
        assertThat(byId.getEmail()).isNull();
        assertThat(byId.getMessage()).contains(id.toString());

        UserNotFoundException byEmail = new UserNotFoundException("a@b.com");
        assertThat(byEmail.getUserId()).isNull();
        assertThat(byEmail.getEmail()).isEqualTo("a@b.com");
        assertThat(byEmail.getMessage()).contains("a@b.com");
    }

    @Test
    void duplicateEmailExceptionShouldExposeEmailAndMessage() {
        DuplicateEmailException ex = new DuplicateEmailException("a@b.com");
        assertThat(ex.getEmail()).isEqualTo("a@b.com");
        assertThat(ex.getMessage()).contains("a@b.com");
    }

    @Test
    void invalidUserStateExceptionShouldExposeDetailsAndMessage() {
        UUID userId = UUID.randomUUID();
        InvalidUserStateException ex = new InvalidUserStateException(userId, UserStatus.PENDING, "activate");

        assertThat(ex.getUserId()).isEqualTo(userId);
        assertThat(ex.getCurrentStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(ex.getOperation()).isEqualTo("activate");
        assertThat(ex.getMessage()).contains(userId.toString()).contains("PENDING").contains("activate");
    }
}
