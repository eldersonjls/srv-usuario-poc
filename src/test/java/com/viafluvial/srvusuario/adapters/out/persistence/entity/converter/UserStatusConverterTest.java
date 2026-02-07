package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserStatusConverterTest {

    @Test
    void shouldConvertLowercaseDbValueToEnum() {
        UserStatusConverter converter = new UserStatusConverter();
        assertEquals(User.UserStatus.ACTIVE, converter.convertToEntityAttribute("active"));
    }
}
