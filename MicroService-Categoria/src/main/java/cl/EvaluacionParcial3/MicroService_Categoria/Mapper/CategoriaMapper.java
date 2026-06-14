package cl.EvaluacionParcial3.MicroService_Categoria.Mapper;

import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaRequest;
import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaResponse;
import cl.EvaluacionParcial3.MicroService_Categoria.Model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria fromRequest(CategoriaRequest request) {
        return Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .estado(request.getEstado())
                .fechaCreacion(request.getFechaCreacion())
                .build();

    }

    public CategoriaResponse toResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .estado(categoria.getEstado())
                .fechaCreacion(categoria.getFechaCreacion())
                .build();
    }
}
