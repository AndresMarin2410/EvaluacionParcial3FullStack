package cl.EvaluacionParcial3.MicroService_Pagos.Controller;

import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoRequest;
import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoResponse;
import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoUpdateRequest;
import cl.EvaluacionParcial3.MicroService_Pagos.Model.Pago;
import cl.EvaluacionParcial3.MicroService_Pagos.Service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pagos")
@Slf4j
@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    @Operation(summary = "Obtener pago", description = "Obtiene una lista de todos los pagos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<Pago> listarPagos() {
        log.info("GET /api/pagos");
        return pagoService.listarPagos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Obtiene un pago mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public PagoResponse buscarPagoPorId(@PathVariable Long id) {
        log.info("GET /api/BuscarPagoPorID: {}", id);
        return pagoService.buscarPagoPorId(id);
    }

    @GetMapping("/idPedido/{idPedido}")
    @Operation(summary = "Obtener pago por idPedido", description = "Obtiene un pago mediante el id del pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public PagoResponse buscarPagoPorIdPedido(@PathVariable Long idPedido) {
        log.info("GET /api/BuscarPagoPorIdPedido: {}", idPedido);
        return pagoService.buscarPorPedidoId(idPedido);
    }

    @PostMapping()
    @Operation(summary = "Crear pago", description = "Crea un nuevo pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado con exito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El pago ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PagoResponse> crearPago(@Valid @RequestBody PagoRequest request) {
        log.info("POST /api/pagos");
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crearPago(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago", description = "Actualiza un pago mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<PagoResponse> actualizarPago(@PathVariable Long id,@Valid @RequestBody PagoUpdateRequest request) {
        log.info("PUT /api/pagos");
        return ResponseEntity.ok(pagoService.actualizarPago(id,request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un pago mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("DELETE /api/pagos");
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}
