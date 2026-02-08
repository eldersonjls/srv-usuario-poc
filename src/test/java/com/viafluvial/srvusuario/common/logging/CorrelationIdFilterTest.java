package com.viafluvial.srvusuario.common.logging;

import com.viafluvial.srvusuario.common.id.CorrelationIdGenerator;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Common: CorrelationIdFilter")
class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("deve propagar header existente e limpar MDC")
    void shouldPropagateExistingHeaderAndClearMdc() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, "corr-123");

        AtomicReference<String> inChain = new AtomicReference<>();
        FilterChain chain = (req, res) -> inChain.set(MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY));

        filter.doFilter(request, response, chain);

        assertThat(inChain.get()).isEqualTo("corr-123");
        assertThat(response.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).isEqualTo("corr-123");
        assertThat(MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY)).isNull();
    }

    @Test
    @DisplayName("deve gerar correlationId quando header ausente")
    void shouldGenerateCorrelationIdWhenMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<String> inChain = new AtomicReference<>();
        FilterChain chain = (req, res) -> inChain.set(MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY));

        filter.doFilter(request, response, chain);

        String header = response.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER);
        assertThat(header).isNotBlank();
        assertThat(CorrelationIdGenerator.isValid(header)).isTrue();
        assertThat(inChain.get()).isEqualTo(header);
        assertThat(MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY)).isNull();
    }
}
