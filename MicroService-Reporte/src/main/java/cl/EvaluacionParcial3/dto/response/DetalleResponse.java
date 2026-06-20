package cl.EvaluacionParcial3.dto.response;

import lombok.Data;

@Data
public class DetalleResponse {
    private Long id;
    private String clave;
    private Double valor;
    private String descripcion;
}
