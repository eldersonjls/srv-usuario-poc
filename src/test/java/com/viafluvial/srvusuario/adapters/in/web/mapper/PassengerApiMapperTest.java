package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.CabinTypeApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PassengerDTOApi;
import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: PassengerApiMapper")
class PassengerApiMapperTest {

    @Test
    @DisplayName("toApp/toApi: deve mapear e converter totalSpent double<->BigDecimal e cabinType")
    void toAppAndToApiShouldMapAndConvert() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate birth = LocalDate.of(1990, 1, 1);
        LocalDateTime now = LocalDateTime.now();

        PassengerDTOApi api = new PassengerDTOApi();
        api.setId(id);
        api.setUserId(userId);
        api.setCpf("123");
        api.setRg("rg");
        api.setBirthDate(birth);
        api.setAddress("addr");
        api.setCity("city");
        api.setState("ST");
        api.setZipCode("000");
        api.setPreferredCabinType(CabinTypeApi.VIP);
        api.setTotalTrips(7);
        api.setTotalSpent(12.5);
        api.setCreatedAt(now.minusDays(1));
        api.setUpdatedAt(now);

        PassengerDTO app = PassengerApiMapper.toApp(api);
        assertThat(app.getId()).isEqualTo(id);
        assertThat(app.getPreferredCabinType()).isEqualTo("VIP");
        assertThat(app.getTotalSpent()).isEqualByComparingTo(BigDecimal.valueOf(12.5));

        PassengerDTOApi mappedBack = PassengerApiMapper.toApi(app);
        assertThat(mappedBack.getPreferredCabinType()).isEqualTo(CabinTypeApi.VIP);
        assertThat(mappedBack.getTotalSpent()).isEqualTo(12.5);
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(PassengerApiMapper.toApp(null)).isNull();
        assertThat(PassengerApiMapper.toApi(null)).isNull();
    }
}
