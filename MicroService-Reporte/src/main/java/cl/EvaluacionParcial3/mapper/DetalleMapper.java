package cl.EvaluacionParcial3.mapper;

import org.springframework.stereotype.Component;

import cl.EvaluacionParcial3.dto.request.DetalleRequest;
import cl.EvaluacionParcial3.dto.response.DetalleResponse;
import cl.EvaluacionParcial3.model.DetalleReporte;

@Component
public class DetalleMapper {
    public DetalleReporte toEntidad(DetalleRequest dto){
        if(dto==null) return null;
        DetalleReporte detalle=new DetalleReporte();
        detalle.setClave(dto.getClave());
        detalle.setValor(dto.getValor());
        detalle.setDescripcion(dto.getDescripcion());
        return detalle;
    }

    public DetalleResponse toResponse(DetalleReporte entidad){
        if(entidad==null) return null;

        DetalleResponse dto=new DetalleResponse();
        dto.setId(entidad.getId());
        dto.setClave(entidad.getClave());
        dto.setValor(entidad.getValor());
        dto.setDescripcion(entidad.getDescripcion());

        return dto;
    }
}
