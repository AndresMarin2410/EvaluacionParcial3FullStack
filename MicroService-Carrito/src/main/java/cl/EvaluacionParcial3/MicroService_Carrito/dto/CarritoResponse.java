package cl.EvaluacionParcial3.MicroService_Carrito.dto;

import java.time.LocalDateTime;
import java.util.List;

import cl.EvaluacionParcial3.MicroService_Carrito.model.EstadoCarrito;
import lombok.Data;

@Data
public class CarritoResponse {
    private Long carritoId;
    private Long idUsuario;
    private LocalDateTime fechaCreacion;
    private EstadoCarrito estado;
    private List<ItemResponse> items;
    private Double total;
}
