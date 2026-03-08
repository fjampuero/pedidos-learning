package com.codigo.pedido.payments.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payments API")
                        .description("Microservicio de pagos del sistema de pedidos de comida")
                        .version("v1")
                        .contact(new Contact()
                                .name("Equipo de Arquitectura")
                                .email("arquitectura@codigo.local"))
                        .license(new License()
                                .name("Uso academico")));
    }
}
