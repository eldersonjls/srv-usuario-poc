package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.PaymentMethod;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.UserSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Persistence: CaseInsensitiveEnumConverters")
class CaseInsensitiveEnumConvertersTest {

    @Test
    @DisplayName("convertToDatabaseColumn: deve salvar em lowercase")
    void convertToDatabaseColumnShouldLowercase() {
        UserTypeConverter converter = new UserTypeConverter();
        assertThat(converter.convertToDatabaseColumn(User.UserType.PASSENGER)).isEqualTo("passenger");
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("UserTypeConverter: deve converter dbData case-insensitive")
    void userTypeConverterShouldConvertCaseInsensitive() {
        UserTypeConverter converter = new UserTypeConverter();
        assertThat(converter.convertToEntityAttribute("passenger")).isEqualTo(User.UserType.PASSENGER);
        assertThat(converter.convertToEntityAttribute(" PASSENGER ")).isEqualTo(User.UserType.PASSENGER);
        assertThat(converter.convertToEntityAttribute(null)).isNull();
        assertThat(converter.convertToEntityAttribute(" ")).isNull();
    }

    @Test
    @DisplayName("DeviceTypeConverter: deve converter dbData case-insensitive")
    void deviceTypeConverterShouldConvertCaseInsensitive() {
        DeviceTypeConverter converter = new DeviceTypeConverter();
        assertThat(converter.convertToEntityAttribute("mobile")).isEqualTo(UserSession.DeviceType.MOBILE);
        assertThat(converter.convertToDatabaseColumn(UserSession.DeviceType.TABLET)).isEqualTo("tablet");
    }

    @Test
    @DisplayName("CabinTypeConverter: deve converter dbData case-insensitive")
    void cabinTypeConverterShouldConvertCaseInsensitive() {
        CabinTypeConverter converter = new CabinTypeConverter();
        assertThat(converter.convertToEntityAttribute("vip")).isEqualTo(Passenger.CabinType.VIP);
        assertThat(converter.convertToDatabaseColumn(Passenger.CabinType.EXECUTIVE)).isEqualTo("executive");
    }

    @Test
    @DisplayName("AdminRoleConverter: deve converter dbData case-insensitive")
    void adminRoleConverterShouldConvertCaseInsensitive() {
        AdminRoleConverter converter = new AdminRoleConverter();
        assertThat(converter.convertToEntityAttribute("support")).isEqualTo(Admin.AdminRole.SUPPORT);
        assertThat(converter.convertToDatabaseColumn(Admin.AdminRole.SUPER_ADMIN)).isEqualTo("super_admin");
    }

    @Test
    @DisplayName("PaymentTypeConverter: deve converter dbData case-insensitive")
    void paymentTypeConverterShouldConvertCaseInsensitive() {
        PaymentTypeConverter converter = new PaymentTypeConverter();
        assertThat(converter.convertToEntityAttribute("pix")).isEqualTo(PaymentMethod.PaymentType.PIX);
        assertThat(converter.convertToDatabaseColumn(PaymentMethod.PaymentType.CREDIT_CARD)).isEqualTo("credit_card");
    }

    @Test
    @DisplayName("CardBrandConverter: deve converter dbData case-insensitive")
    void cardBrandConverterShouldConvertCaseInsensitive() {
        CardBrandConverter converter = new CardBrandConverter();
        assertThat(converter.convertToEntityAttribute("visa")).isEqualTo(PaymentMethod.CardBrand.VISA);
        assertThat(converter.convertToDatabaseColumn(PaymentMethod.CardBrand.MASTERCARD)).isEqualTo("mastercard");
    }

    @Test
    @DisplayName("PixKeyTypeConverter: deve converter dbData case-insensitive")
    void pixKeyTypeConverterShouldConvertCaseInsensitive() {
        PixKeyTypeConverter converter = new PixKeyTypeConverter();
        assertThat(converter.convertToEntityAttribute("email")).isEqualTo(PaymentMethod.PixKeyType.EMAIL);
        assertThat(converter.convertToDatabaseColumn(PaymentMethod.PixKeyType.RANDOM)).isEqualTo("random");
    }

    @Test
    @DisplayName("convertToEntityAttribute: deve falhar com valor inválido")
    void convertToEntityAttributeShouldThrowOnInvalidValue() {
        UserTypeConverter converter = new UserTypeConverter();
        assertThatThrownBy(() -> converter.convertToEntityAttribute("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
