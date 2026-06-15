package cl.EvaluacionParcial3.MicroService_Usuario.Mapper;

import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioRequest;
import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioResponse;
import cl.EvaluacionParcial3.MicroService_Usuario.Model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario fromRequest(UsuarioRequest request){
        return Usuario.builder()
                .run(request.getRun())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .fechaNac(request.getFechaNac())
                .correo(request.getCorreo())
                .direccion(request.getDireccion())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .run(usuario.getRun())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .fechaNac(usuario.getFechaNac())
                .correo(usuario.getCorreo())
                .direccion(usuario.getDireccion())
                .build();
    }
}
