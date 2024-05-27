package nl.sourcelabs.service.invoice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InvoiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoiceApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.api.version}") String appVersion) {
        return new OpenAPI().info(new Info()
            .title("Invoice Service API")
            .version(appVersion)
            .license(new License().name("Apache 2.0")));
    }
}