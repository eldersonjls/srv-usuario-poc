package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.domain.model.Boatman;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: BoatmanMapper")
class BoatmanMapperTest {

    private final BoatmanMapper mapper = Mappers.getMapper(BoatmanMapper.class);

    @Test
    @DisplayName("toDomain: deve ignorar id e preencher timestamps")
    void toDomainShouldIgnoreIdAndSetTimestamps() {
        BoatmanDTO dto = BoatmanDTO.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .build();

        Boatman boatman = mapper.toDomain(dto);

        assertThat(boatman.getId()).isNull();
        assertThat(boatman.getUserId()).isEqualTo(dto.getUserId());
        assertThat(boatman.getCpf()).isEqualTo("123");
        assertThat(boatman.getCnpj()).isEqualTo("456");
        assertThat(boatman.getCompanyName()).isEqualTo("Empresa");
        assertThat(boatman.getCreatedAt()).isNotNull();
        assertThat(boatman.getUpdatedAt()).isNotNull();
    }
}
