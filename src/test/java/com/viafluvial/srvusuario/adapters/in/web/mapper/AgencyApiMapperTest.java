package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.AgencyDTOApi;
import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: AgencyApiMapper")
class AgencyApiMapperTest {

    @Test
    @DisplayName("toApp/toApi: deve mapear e converter campos numéricos")
    void toAppAndToApiShouldMapAndConvertNumbers() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        AgencyDTOApi api = new AgencyDTOApi();
        api.setId(id);
        api.setUserId(userId);
        api.setCompanyName("Comp");
        api.setCnpj("00");
        api.setCommissionPercent(1.5);
        api.setTotalRevenue(10.0);
        api.setTotalCommissionPaid(2.0);
        api.setTotalSales(3);
        api.setApprovedAt(now.minusDays(1));
        api.setCreatedAt(now.minusDays(2));
        api.setUpdatedAt(now.minusDays(1));

        AgencyDTO app = AgencyApiMapper.toApp(api);
        assertThat(app.getCommissionPercent()).isEqualByComparingTo(BigDecimal.valueOf(1.5));
        assertThat(app.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(10.0));

        AgencyDTOApi mappedBack = AgencyApiMapper.toApi(app);
        assertThat(mappedBack.getCommissionPercent()).isEqualTo(1.5);
        assertThat(mappedBack.getTotalRevenue()).isEqualTo(10.0);
        assertThat(mappedBack.getTotalCommissionPaid()).isEqualTo(2.0);
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(AgencyApiMapper.toApp(null)).isNull();
        assertThat(AgencyApiMapper.toApi(null)).isNull();
    }
}
