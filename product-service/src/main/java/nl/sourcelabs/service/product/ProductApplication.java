package nl.sourcelabs.service.product;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductApplication {

  @Bean
  public OpenAPI customOpenAPI(@Value("${springdoc.api.version}") String appVersion) {
    return new OpenAPI()
        .info(new Info()
            .title("Product Service API")
            .version(appVersion)
            .license(new License().name("Apache 2.0")));
  }

  public static void main(String[] args) {
    SpringApplication.run(ProductApplication.class, args);
  }

}
