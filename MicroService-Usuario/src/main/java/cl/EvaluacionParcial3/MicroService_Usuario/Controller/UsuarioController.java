package cl.EvaluacionParcial3.MicroService_Usuario.Controller;

import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioRequest;
import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioResponse;
import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioUpdateRequest;
import cl.EvaluacionParcial3.MicroService_Usuario.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/usuarios")
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener usuarios", description = "Obtiene una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<UsuarioResponse>> obtenerUsuarios() {
        log.info("Listando todos los usuarios registrados.");
        List<EntityModel<UsuarioResponse>> usuarios = usuarioService.buscarUsuarios().stream()
                .map(usuario -> {

                    UsuarioResponse dto = UsuarioResponse.builder()
                            .id(usuario.getId())
                            .run(usuario.getRun())
                            .nombre(usuario.getNombre())
                            .apellido(usuario.getApellido())
                            .fechaNac(usuario.getFechaNac())
                            .correo(usuario.getCorreo())
                            .direccion(usuario.getDireccion())
                            .build();
                    return EntityModel.of(dto, linkTo(methodOn(UsuarioController.class)
                            .buscarUsuarioPorId(dto.getId()))
                            .withSelfRel());
                })
                .toList();

        return CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class)
                .obtenerUsuarios())
                .withSelfRel()
        );
    }

    //BUSCAR USUARIO POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public EntityModel<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        log.info("Obtener usuario por ID: {}", id);

        UsuarioResponse dto = usuarioService.buscarPorId(id);

        return EntityModel.of( dto,
                linkTo(methodOn(UsuarioController.class)
                        .buscarUsuarioPorId(id))
                        .withSelfRel(),
                linkTo(methodOn(UsuarioController.class)
                        .obtenerUsuarios())
                        .withRel("Listar usuarios"),
                linkTo(methodOn(UsuarioController.class)
                        .buscarUsuarioPorRun(dto.getRun()))
                        .withRel("buscar-usuario-por-run")
        );
    }

    //BUSCAR USUARIO POR RUN
    @GetMapping("/run/{run}")
    @Operation(summary = "Obtener usuario por RUN", description = "Obtiene un usuario mediante su RUN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public UsuarioResponse buscarUsuarioPorRun(@PathVariable String run) {
        log.info("GET /api/usuarios/buscarUsuarioPorRun/{}", run);
        return usuarioService.buscarPorRun(run);
    }

    //CREAR NUEVO USUARIO
    @PostMapping()
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con exito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        log.info("POST /v1/usuarios");
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(request));
    }

    //ACTUALIZAR USUARIO
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        log.info("PUT /api/usuarios/actualizarUsuario/{}", id);
        return ResponseEntity.ok().body(usuarioService.actualizarUsuario(id, request));
    }

    //ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("DELETE /v1/usuarios/{}", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
