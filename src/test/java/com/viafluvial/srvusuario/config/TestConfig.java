package com.viafluvial.srvusuario.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuração de teste para carregar componentes necessários.
 * Habilita scan de mappers e outros componentes Spring.
 */
@TestConfiguration
@ComponentScan(basePackages = {
    "com.viafluvial.srvusuario.adapters.out.persistence",
    "com.viafluvial.srvusuario.adapters.out.persistence.mapper",
    "com.viafluvial.srvusuario.adapters.in.web.mapper",
    "com.viafluvial.srvusuario.application.mapper"
})
public class TestConfig {
    // Configuração de teste para scan de componentes
}
