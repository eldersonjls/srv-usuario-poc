package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDocumentsDTOApi;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: BoatmanApiMapper")
class BoatmanApiMapperTest {

    @Test
    @DisplayName("toApp/toApi: deve mapear e converter rating/totalRevenue")
    void toAppAndToApiShouldMapAndConvertNumbers() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        BoatmanDTOApi api = new BoatmanDTOApi();
        api.setId(id);
        api.setUserId(userId);
        api.setCpf("123");
        api.setCnpj("456");
        api.setCompanyName("C");
        api.setRating(4.5);
        api.setTotalTrips(10);
        api.setTotalRevenue(99.9);
        api.setApprovedAt(now.minusDays(2));
        api.setCreatedAt(now.minusDays(3));
        api.setUpdatedAt(now.minusDays(1));

        BoatmanDTO app = BoatmanApiMapper.toApp(api);
        assertThat(app.getRating()).isEqualByComparingTo(BigDecimal.valueOf(4.5));
        assertThat(app.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(99.9));

        BoatmanDTOApi mappedBack = BoatmanApiMapper.toApi(app);
        assertThat(mappedBack.getRating()).isEqualTo(4.5);
        assertThat(mappedBack.getTotalRevenue()).isEqualTo(99.9);
    }

    @Test
    @DisplayName("BoatmanDocuments: deve mapear URLs")
    void documentsShouldMap() {
        BoatmanDocumentsDTOApi api = new BoatmanDocumentsDTOApi();
        api.setDocumentCpfUrl("cpf");
        api.setDocumentCnpjUrl("cnpj");
        api.setDocumentAddressProofUrl("addr");

        BoatmanDocumentsDTO app = BoatmanApiMapper.toApp(api);
        assertThat(app.getDocumentCpfUrl()).isEqualTo("cpf");

        BoatmanDocumentsDTOApi mappedBack = BoatmanApiMapper.toApi(app);
        assertThat(mappedBack.getDocumentCnpjUrl()).isEqualTo("cnpj");
        assertThat(mappedBack.getDocumentAddressProofUrl()).isEqualTo("addr");
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(BoatmanApiMapper.toApp((BoatmanDTOApi) null)).isNull();
        assertThat(BoatmanApiMapper.toApi((BoatmanDTO) null)).isNull();
        assertThat(BoatmanApiMapper.toApp((BoatmanDocumentsDTOApi) null)).isNull();
        assertThat(BoatmanApiMapper.toApi((BoatmanDocumentsDTO) null)).isNull();
    }
}
