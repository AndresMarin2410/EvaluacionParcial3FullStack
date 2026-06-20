package cl.EvaluacionParcial3.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ReporteResponse {
    private Long id;
    private LocalDateTime fechaGeneracion;
    private String tipoReporte;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String contenido;
    private List<DetalleResponse> detalles;
}
