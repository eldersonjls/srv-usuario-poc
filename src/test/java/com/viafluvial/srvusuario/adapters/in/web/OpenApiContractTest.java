package com.viafluvial.srvusuario.adapters.in.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OpenApiContractTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void openapiYamlShouldMatchPublishedPaths() throws IOException {
        String yaml = new String(
            getClass().getClassLoader().getResourceAsStream("openapi/openapi.yaml").readAllBytes(),
            StandardCharsets.UTF_8
        );

        SwaggerParseResult yamlResult = new OpenAPIV3Parser().readContents(yaml);
        OpenAPI yamlApi = yamlResult.getOpenAPI();
        assertThat(yamlApi).isNotNull();

        String json = restTemplate.getForObject("http://localhost:" + port + "/api/v1/v3/api-docs", String.class);
        SwaggerParseResult jsonResult = new OpenAPIV3Parser().readContents(json);
        OpenAPI runtimeApi = jsonResult.getOpenAPI();
        assertThat(runtimeApi).isNotNull();

        Set<String> yamlPaths = yamlApi.getPaths().keySet();
        Set<String> runtimePaths = runtimeApi.getPaths().keySet();

        assertThat(runtimePaths).containsAll(yamlPaths);

        Set<String> missingOps = yamlApi.getPaths().entrySet().stream()
            .flatMap(entry -> entry.getValue().readOperationsMap().keySet().stream()
                .map(method -> method.name() + " " + entry.getKey()))
            .filter(op -> {
                String[] parts = op.split(" ", 2);
                return runtimeApi.getPaths().get(parts[1]) == null
                    || !runtimeApi.getPaths().get(parts[1]).readOperationsMap().containsKey(
                        io.swagger.v3.oas.models.PathItem.HttpMethod.valueOf(parts[0])
                    );
            })
            .collect(Collectors.toSet());

        assertThat(missingOps).isEmpty();
    }
}
