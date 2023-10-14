package my.edu.sunway.wbrms.wbrmsreservationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://wbrms-reservations-service.fly.dev");
        return new OpenAPI().servers(List.of(server));
    }
}
