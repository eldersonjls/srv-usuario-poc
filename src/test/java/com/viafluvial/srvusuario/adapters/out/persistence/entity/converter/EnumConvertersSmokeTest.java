package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.PaymentMethod;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.UserSession;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnumConvertersSmokeTest {

    @Test
    void shouldRoundTripAllConcreteConverters() {
        AdminRoleConverter adminRoleConverter = new AdminRoleConverter();
        assertThat(adminRoleConverter.convertToEntityAttribute("admin")).isEqualTo(Admin.AdminRole.ADMIN);
        assertThat(adminRoleConverter.convertToDatabaseColumn(Admin.AdminRole.SUPPORT)).isEqualTo("support");

        CabinTypeConverter cabinTypeConverter = new CabinTypeConverter();
        assertThat(cabinTypeConverter.convertToEntityAttribute("vip")).isEqualTo(Passenger.CabinType.VIP);
        assertThat(cabinTypeConverter.convertToDatabaseColumn(Passenger.CabinType.STANDARD)).isEqualTo("standard");

        CardBrandConverter cardBrandConverter = new CardBrandConverter();
        assertThat(cardBrandConverter.convertToEntityAttribute("visa")).isEqualTo(PaymentMethod.CardBrand.VISA);
        assertThat(cardBrandConverter.convertToDatabaseColumn(PaymentMethod.CardBrand.ELO)).isEqualTo("elo");

        DeviceTypeConverter deviceTypeConverter = new DeviceTypeConverter();
        assertThat(deviceTypeConverter.convertToEntityAttribute("mobile")).isEqualTo(UserSession.DeviceType.MOBILE);
        assertThat(deviceTypeConverter.convertToDatabaseColumn(UserSession.DeviceType.DESKTOP)).isEqualTo("desktop");

        PaymentTypeConverter paymentTypeConverter = new PaymentTypeConverter();
        assertThat(paymentTypeConverter.convertToEntityAttribute("pix")).isEqualTo(PaymentMethod.PaymentType.PIX);
        assertThat(paymentTypeConverter.convertToDatabaseColumn(PaymentMethod.PaymentType.CREDIT_CARD))
            .isEqualTo("credit_card");

        PixKeyTypeConverter pixKeyTypeConverter = new PixKeyTypeConverter();
        assertThat(pixKeyTypeConverter.convertToEntityAttribute("email")).isEqualTo(PaymentMethod.PixKeyType.EMAIL);
        assertThat(pixKeyTypeConverter.convertToDatabaseColumn(PaymentMethod.PixKeyType.RANDOM)).isEqualTo("random");

        UserTypeConverter userTypeConverter = new UserTypeConverter();
        assertThat(userTypeConverter.convertToEntityAttribute("passenger")).isEqualTo(User.UserType.PASSENGER);
        assertThat(userTypeConverter.convertToDatabaseColumn(User.UserType.ADMIN)).isEqualTo("admin");
    }
}
