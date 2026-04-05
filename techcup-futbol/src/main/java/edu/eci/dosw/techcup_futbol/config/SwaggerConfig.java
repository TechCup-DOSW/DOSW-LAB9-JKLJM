package edu.eci.dosw.techcup_futbol.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI techCupOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechCup Futbol API")
                        .version("1.0")
                        .description("REST API for tournament management in TechCup Futbol")
                        .contact(new Contact()
                                .name("TechCup DOSW Team")
                                .email("support@techcup.local")));
    }
}
