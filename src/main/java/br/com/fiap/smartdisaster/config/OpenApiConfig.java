package br.com.fiap.smartdisaster.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Insira o token JWT obtido em POST /auth/login no formato: Bearer {token}"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI smartDisasterOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("https://smartdisasterjava-production.up.railway.app").description("Produção"),
                        new Server().url("http://localhost:8080").description("Local")
                ))
                .info(new Info()
                        .title("SmartDisaster API")
                        .description("API para gestão inteligente de abrigos, vítimas, voluntários, doações e sensores em situações de emergência.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FIAP — SmartDisaster Team")
                                .email("smartdisaster@fiap.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
