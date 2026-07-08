package cl.EvaluacionParcial3.MicroService_Envio.Controller;

import cl.EvaluacionParcial3.MicroService_Envio.Dto.EnvioRequest;
import cl.EvaluacionParcial3.MicroService_Envio.Dto.EnvioResponse;
import cl.EvaluacionParcial3.MicroService_Envio.Service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/v1/envios")
@Slf4j
public class EnvioController {

    @Autowired
    private EnvioService envioService;


    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener envio por usuario por ID", description = "Obtiene un envio mediante el ID del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Envio no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<EnvioResponse>> obtenerEnviosPorUsuarioId(
            @PathVariable Long usuarioId) {

        log.info("GET /v1/envios/usuario/{}", usuarioId);

        List<EntityModel<EnvioResponse>> envios = envioService.obtenerEnvioPorUsuarioId(usuarioId)
                .stream()
                .map(envio -> EntityModel.of(
                        envio,
                        linkTo(methodOn(EnvioController.class)
                                .obtenerEnvioPorNumeroSeguimiento(envio.getNumeroSeguimiento()))
                                .withSelfRel()
                ))
                .toList();

        return CollectionModel.of(envios,
                linkTo(methodOn(EnvioController.class)
                        .obtenerEnviosPorUsuarioId(usuarioId))
                        .withSelfRel()
        );
    }


    @GetMapping
    @Operation(summary = "Obtener envios", description = "Obtiene una lista de todos los envios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<EnvioResponse>>> obtenerEnvios() {

        log.info("GET /v1/envios");

        List<EntityModel<EnvioResponse>> envios = envioService.listarTodos()
                .stream()
                .map(envio -> EntityModel.of(
                        envio,
                        linkTo(methodOn(EnvioController.class)
                                .obtenerEnvioPorNumeroSeguimiento(envio.getNumeroSeguimiento()))
                                .withSelfRel()
                ))
                .toList();

        CollectionModel<EntityModel<EnvioResponse>> response =
                CollectionModel.of(envios);

        response.add(
                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvios())
                        .withSelfRel()
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/seguimiento/{numeroSeguimiento}")
    @Operation(summary = "Obtener envio por numero de seguimiento", description = "Obtiene un envio mediante el numero de seguimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Envio no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<EnvioResponse>> obtenerEnvioPorNumeroSeguimiento(
            @PathVariable String numeroSeguimiento) {

        log.info("GET /v1/envios/seguimiento/{}", numeroSeguimiento);

        EnvioResponse envio =
                envioService.obtenerEnvioPorNumeroSeguimiento(numeroSeguimiento);

        EntityModel<EnvioResponse> response = EntityModel.of(
                envio,
                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvioPorNumeroSeguimiento(numeroSeguimiento))
                        .withSelfRel(),

                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvios())
                        .withRel("listar-envios")
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(summary = "Crear envio", description = "Crea un nuevo envio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Envio creado con exito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnvioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El envio ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<EnvioResponse>> crearEnvio(
            @Valid @RequestBody EnvioRequest envioRequest) {

        log.info("POST /v1/envios");

        EnvioResponse envio =
                envioService.crearEnvio(envioRequest);

        EntityModel<EnvioResponse> response = EntityModel.of(
                envio,
                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvioPorNumeroSeguimiento(envio.getNumeroSeguimiento()))
                        .withSelfRel(),

                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvios())
                        .withRel("listar-envios")
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar envio", description = "Actualiza un envio mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<EnvioResponse>> actualizarEnvio(
            @PathVariable Long id,
            @Valid @RequestBody EnvioRequest envioRequest) {

        log.info("PUT /v1/envios/{}", id);

        EnvioResponse envio =
                envioService.actualizarEnvio(id, envioRequest);


        EntityModel<EnvioResponse> response = EntityModel.of(
                envio,
                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvioPorNumeroSeguimiento(envio.getNumeroSeguimiento()))
                        .withSelfRel(),

                linkTo(methodOn(EnvioController.class)
                        .obtenerEnvios())
                        .withRel("listar-envios")
        );

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar envio", description = "Elimina un envio mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Envio eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {

        log.info("DELETE /v1/envios/{}", id);

        envioService.eliminarEnvio(id);

        return ResponseEntity.noContent().build();
    }
}
