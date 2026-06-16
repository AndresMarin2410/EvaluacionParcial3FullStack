package cl.EvaluacionParcial3.MicroService_Carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarritoRequest {
    @NotNull(message="el id de usuario es obligatorio")
    private Long idUsuario;
    @NotNull(message="Debe existir un id de producto")
    private Long productoId;
    @Min(1)
    private Integer cantidad;
}
