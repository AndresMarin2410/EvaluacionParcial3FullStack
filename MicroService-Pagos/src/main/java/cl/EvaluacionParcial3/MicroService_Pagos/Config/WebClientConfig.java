package cl.EvaluacionParcial3.MicroService_Pagos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${pedido.service.url}")
    private String pedidoUrl;

    @Bean
    public WebClient webClientPedido() {
        return WebClient.builder()
                .baseUrl(pedidoUrl)
                .build();
    }
}
