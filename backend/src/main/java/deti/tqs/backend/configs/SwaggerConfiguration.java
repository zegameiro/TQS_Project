package deti.tqs.backend.configs;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@Generated
public class SwaggerConfiguration {
  
  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
      .group("public-api")
      .pathsToMatch("/public/**")
      .build();
  }

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info().title("Beauty Plaza Salon API")
        .description("The complete description of the API for the Beauty Plaza Salon application.")
        .version("v1.0")
        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }
}
