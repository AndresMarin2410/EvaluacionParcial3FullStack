package cl.EvaluacionParcial3.MicroService_Pagos.Client;

import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PedidoResponse;
import cl.EvaluacionParcial3.MicroService_Pagos.Exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class PedidoClient {
    @Autowired
    private WebClient webClientPedido;

    public PedidoResponse obtenerPedidoPorId(Long id) {
        log.info("Iniciando obtencion del pedido por id {}", id);

        try{

            return webClientPedido.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(PedidoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener informacion del pedido con el ID: {}", id, ex);

            switch (ex.getStatusCode().value()) {

                case 403 -> throw new ForbiddenException(
                        "Acceso prohibido al servicio de usuarios");

                case 404 -> throw new NoSuchElementException(
                        "Pedido no encontrado con ID: " + id);

                default -> throw new RuntimeException(
                        "Error interno del servicio de pedidos");
            }
        }
    }
}
