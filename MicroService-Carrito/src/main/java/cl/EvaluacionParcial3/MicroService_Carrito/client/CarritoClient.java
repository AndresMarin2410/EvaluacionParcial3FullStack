package cl.EvaluacionParcial3.MicroService_Carrito.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.EvaluacionParcial3.MicroService_Carrito.dto.CarritoResponse;
import cl.EvaluacionParcial3.MicroService_Carrito.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CarritoClient {

    @Autowired
    private WebClient webClient;

    public CarritoResponse obtenerCarritoPorId(Long id) {

        log.info("Obteniendo informacion del carrito con ID: {}", id);

        try {

            return webClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(CarritoResponse.class)
                    .block();

        } catch (WebClientResponseException ex) {

            log.error("Error al obtener informacion del carrito con ID: {}", id, ex);

            switch (ex.getStatusCode().value()) {

                case 403 -> throw new ForbiddenException(
                        "Acceso prohibido al servicio de carrito de compras");

                case 404 -> throw new NoSuchElementException(
                        "Carrito no encontrado con ID: " + id);

                default -> throw new RuntimeException(
                        "Error interno del servicio de carrito de compras");
            }
        }
    }
}

