package com.viafluvial.srvusuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@ComponentScan(basePackages = "com.viafluvial")
@OpenAPIDefinition(
    info = @Info(
        title = "ViáFluvial - API de Usuários",
        version = "1.0.0",
        description = "Microsserviço de gerenciamento de usuários da plataforma ViáFluvial",
        contact = @Contact(
            name = "ViáFluvial Support",
            email = "support@viafluvial.com.br"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "/api/v1",
            description = "Current Server (relative)"
        ),
        @Server(
            url = "https://api.viafluvial.com.br/api/v1",
            description = "Production Server"
        )
    }
)
public class SrvUsuarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrvUsuarioApplication.class, args);
    }

}
