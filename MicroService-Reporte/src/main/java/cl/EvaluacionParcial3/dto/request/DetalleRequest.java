package cl.EvaluacionParcial3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DetalleRequest {
    @NotBlank(message = "La clave del detalle no puede estar vacía")
    private String clave;

    @NotNull(message="El valor es obligatorio")
    @PositiveOrZero(message = "El valor debe ser cero o superior")
    private Double valor;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;
}
