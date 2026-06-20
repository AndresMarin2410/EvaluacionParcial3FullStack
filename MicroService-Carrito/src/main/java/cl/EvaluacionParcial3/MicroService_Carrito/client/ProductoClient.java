package cl.EvaluacionParcial3.MicroService_Carrito.client;


import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.EvaluacionParcial3.MicroService_Carrito.dto.ProductoResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(WebClient.Builder webClientBuilder,
                          @Value("${microservicios.productos.url}") String productosUrl) {
        this.webClient = webClientBuilder.baseUrl(productosUrl).build();
    }

    // consulta el precio real del producto al ms-productos
    public ProductoResponse obtenerProductoPorId(Long id) {

        log.info("Consultando precio del producto con ID: {}", id);

        try {

            return webClient.get()
                    .uri("/api/productos/{id}", id)
                    .retrieve()
                    .bodyToMono(ProductoResponse.class)
                    .block();

        } catch (WebClientResponseException ex) {

            log.error("Error al consultar producto con ID: {}", id, ex);

            switch (ex.getStatusCode().value()) {

                case 404 -> throw new NoSuchElementException(
                        "Producto no encontrado con ID: " + id);

                default -> throw new RuntimeException(
                        "No se pudo conectar con el servicio de productos");
            }
        }
    }
}
