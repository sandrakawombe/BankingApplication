package src.main.com.banking.transactionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI transactionServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Service API")
                        .description("RESTful API for managing financial transactions in the Banking Application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Banking Application Team")
                                .email("support@banking.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}