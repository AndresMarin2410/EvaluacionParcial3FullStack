package cl.EvaluacionParcial3.mapper;

import java.util.stream.Collectors;

import cl.EvaluacionParcial3.dto.response.DetalleResponse;
import cl.EvaluacionParcial3.dto.response.ReporteResponse;
import cl.EvaluacionParcial3.model.DetalleReporte;
import cl.EvaluacionParcial3.model.ReporteVenta;



public class ReporteMapper {

    // convertir de Entidad a DTO para enviar al cliente
    public static ReporteResponse toResponse(ReporteVenta entidad) {
        if (entidad == null) return null;

        ReporteResponse dto = new ReporteResponse();
        dto.setId(entidad.getId());
        dto.setFechaGeneracion(entidad.getFechaGeneracion());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaFin(entidad.getFechaFin());
        dto.setContenido(entidad.getContenido());
        dto.setTipoReporte(entidad.getTipo().name());

        // mapeo de lista de detalles
        if (entidad.getDetalles() != null) {
            dto.setDetalles(entidad.getDetalles().stream()
                    .map(ReporteMapper::toDetalleResponse)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // convertir el detalle individual
    public static DetalleResponse toDetalleResponse(DetalleReporte entidad) {
        if (entidad == null) return null;

        DetalleResponse dto = new DetalleResponse();
        dto.setId(entidad.getId());
        dto.setClave(entidad.getClave());
        dto.setValor(entidad.getValor());
        dto.setDescripcion(entidad.getDescripcion());

        return dto;
    }
}

