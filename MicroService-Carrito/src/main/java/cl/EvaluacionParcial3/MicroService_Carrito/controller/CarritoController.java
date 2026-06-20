package cl.EvaluacionParcial3.MicroService_Carrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.EvaluacionParcial3.MicroService_Carrito.dto.CarritoRequest;
import cl.EvaluacionParcial3.MicroService_Carrito.dto.CarritoResponse;
import cl.EvaluacionParcial3.MicroService_Carrito.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@Tag(name = "Carrito", description = "Operaciones relacionadas con los carritos de compra online")
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    // agregar un producto al carrito
    @Operation(summary = "Agregar un producto al carrito", description = "Agrega distintos productos al carrito de compras.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponse> agregarProducto(@Valid @RequestBody CarritoRequest request) {
        CarritoResponse response = carritoService.agregarProducto(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // obtener el carrito activo de un usuario específico
    @Operation(summary = "Obtener carrito activo de un usuario en especifico", description = "Muestra el carrito de un usuario/cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CarritoResponse> obtenerCarritoActivo(@PathVariable Long idUsuario) {
        CarritoResponse response = carritoService.obtenerCarritoActivo(idUsuario);
        return ResponseEntity.ok(response);
    }

    // vaciar carrito
    @Operation(summary = "Vaciar carrito", description = "Elimina los productos del carrito de compras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{carritoId}/limpiar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long carritoId) {
        carritoService.vaciarCarrito(carritoId);
        return ResponseEntity.noContent().build();
    }

    // confirma la compra (Cambia estado a "COMPRADO")
    @Operation(summary = "Cambia el estado del carrito", description = "Confirma la compra o la cancelacion de la misma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{carritoId}/confirmar")
    public ResponseEntity<CarritoResponse> confirmarCompra(@PathVariable Long carritoId) {
        CarritoResponse response = carritoService.confirmarCompra(carritoId);
        return ResponseEntity.ok(response);
    }
}

