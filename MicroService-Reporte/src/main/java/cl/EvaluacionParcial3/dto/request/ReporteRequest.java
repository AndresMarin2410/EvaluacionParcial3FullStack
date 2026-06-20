package cl.EvaluacionParcial3.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteRequest {
    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String tipoReporte;

    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    private String contenido;

    @NotEmpty(message = "El reporte debe tener al menos un detalle")
    @Valid
    private List<DetalleRequest> detalles;
}
