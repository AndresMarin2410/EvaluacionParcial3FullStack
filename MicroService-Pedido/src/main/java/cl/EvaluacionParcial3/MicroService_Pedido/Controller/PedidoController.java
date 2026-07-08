package cl.EvaluacionParcial3.MicroService_Pedido.Controller;

import cl.EvaluacionParcial3.MicroService_Pedido.Dto.PedidoRequest;
import cl.EvaluacionParcial3.MicroService_Pedido.Dto.PedidoResponse;
import cl.EvaluacionParcial3.MicroService_Pedido.Service.PedidoService;
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
@RequestMapping("/v1/pedidos")
@Slf4j
@Tag(name = "pedidos", description = "Operaciones relacionadas con los pedidos")
class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Obtener pedidos", description = "Obtiene una lista de todos los pedidos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<PedidoResponse>> obtenerPedidos() {
        log.info("Probando HATEOAS");
        log.info("GET /v1/pedidos");

        List<EntityModel<PedidoResponse>> pedidos = pedidoService.obtenerTodosLosPedidos()
                .stream()
                .map(pedido -> EntityModel.of(
                        pedido,
                        linkTo(methodOn(PedidoController.class)
                                .buscarPedidoPorId(pedido.getId()))
                                .withSelfRel(),

                        linkTo(methodOn(PedidoController.class)
                                .obtenerPedidos())
                                .withRel("listar-pedidos"),

                        linkTo(methodOn(PedidoController.class)
                                .obtenerPedidosPorUsuarioId(pedido.getUsuarioId()))
                                .withRel("pedidos-usuario")
                ))
                .toList();

        return CollectionModel.of(
                pedidos,
                linkTo(methodOn(PedidoController.class)
                        .obtenerPedidos())
                        .withSelfRel()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene un pedido mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public EntityModel<PedidoResponse> buscarPedidoPorId(@PathVariable Long id) {

        log.info("GET /v1/pedidos/{}", id);

        PedidoResponse pedido = pedidoService.obtenerPedidoPorId(id);

        return EntityModel.of(
                pedido,

                linkTo(methodOn(PedidoController.class)
                        .buscarPedidoPorId(id))
                        .withSelfRel(),

                linkTo(methodOn(PedidoController.class)
                        .obtenerPedidos())
                        .withRel("listar_pedidos"),

                linkTo(methodOn(PedidoController.class)
                        .obtenerPedidosPorUsuarioId(pedido.getUsuarioId()))
                        .withRel("pedidos_usuario")
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pedido por usuarioId", description = "Obtiene un pedido mediante el ID del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<PedidoResponse>> obtenerPedidosPorUsuarioId(@PathVariable Long usuarioId) {

        log.info("GET /v1/pedidos/usuario/{}", usuarioId);

        List<EntityModel<PedidoResponse>> pedidos = pedidoService.obtenerPedidosPorUsuarioId(usuarioId)
                .stream()
                .map(pedido -> EntityModel.of(
                        pedido,

                        linkTo(methodOn(PedidoController.class)
                                .buscarPedidoPorId(pedido.getId()))
                                .withSelfRel(),

                        linkTo(methodOn(PedidoController.class)
                                .obtenerPedidos())
                                .withRel("listar_pedidos")
                ))
                .toList();

        return CollectionModel.of(
                pedidos,

                linkTo(methodOn(PedidoController.class)
                        .obtenerPedidosPorUsuarioId(usuarioId))
                        .withSelfRel(),

                linkTo(methodOn(PedidoController.class)
                        .obtenerPedidos())
                        .withRel("listar_pedidos")
        );
    }

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado con exito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El pedido ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody PedidoRequest pedidoRequest) {

        log.info("POST /v1/pedidos");

        PedidoResponse pedido = pedidoService.crearPedido(pedidoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido", description = "Actualiza un pedido mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PedidoResponse> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequest pedidoRequest) {

        log.info("PUT /v1/pedidos/{}", id);

        PedidoResponse pedido = pedidoService.actualizarPedido(id, pedidoRequest);

        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {

        log.info("DELETE /v1/pedidos/{}", id);

        pedidoService.eliminarPedido(id);

        return ResponseEntity.noContent().build();
    }

}