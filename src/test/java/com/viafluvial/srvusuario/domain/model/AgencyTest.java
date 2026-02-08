package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgencyTest {

    @Test
    void shouldBuildValidAgencyWithDefaults() {
        Agency agency = Agency.builder()
            .userId(UUID.randomUUID())
            .companyName("Empresa")
            .cnpj("00.000.000/0001-00")
            .build();

        assertThat(agency.getCompanyName()).isEqualTo("Empresa");
        assertThat(agency.getCnpj()).isEqualTo("00.000.000/0001-00");
        assertThat(agency.getCommissionPercent()).isEqualTo(new BigDecimal("10.00"));
        assertThat(agency.getTotalSales()).isEqualTo(0);
        assertThat(agency.getTotalRevenue()).isEqualTo(BigDecimal.ZERO);
        assertThat(agency.getTotalCommissionPaid()).isEqualTo(BigDecimal.ZERO);
        assertThat(agency.getCreatedAt()).isNotNull();
        assertThat(agency.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldValidateRequiredFields() {
        assertThatThrownBy(() -> Agency.builder().companyName("Empresa").cnpj("x").build())
            .isInstanceOf(Agency.InvalidAgencyException.class)
            .satisfies(ex -> {
                Agency.InvalidAgencyException dae = (Agency.InvalidAgencyException) ex;
                assertThat(dae.getErrorCode()).isEqualTo(ErrorCode.INVALID_INPUT);
            });

        assertThatThrownBy(() -> Agency.builder().userId(UUID.randomUUID()).cnpj("x").build())
            .isInstanceOf(Agency.InvalidAgencyException.class)
            .hasMessageContaining("companyName");

        assertThatThrownBy(() -> Agency.builder().userId(UUID.randomUUID()).companyName("Empresa").build())
            .isInstanceOf(Agency.InvalidAgencyException.class)
            .hasMessageContaining("cnpj");
    }
}
