package cl.EvaluacionParcial3.MicroService_Carrito.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.productos.url}")
    private String productoUrl;


    @Bean
    public WebClient webClientUsuario() {
        return WebClient.builder()
                .baseUrl(productoUrl)
                .build();
    }
}
