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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    // agregar un producto al carrito
    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponse> agregarProducto(@Valid @RequestBody CarritoRequest request) {
        CarritoResponse response = carritoService.agregarProducto(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // obtener el carrito activo de un usuario específico
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CarritoResponse> obtenerCarritoActivo(@PathVariable Long idUsuario) {
        CarritoResponse response = carritoService.obtenerCarritoActivo(idUsuario);
        return ResponseEntity.ok(response);
    }

    // vaciar carrito
    @DeleteMapping("/{carritoId}/limpiar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long carritoId) {
        carritoService.vaciarCarrito(carritoId);
        return ResponseEntity.noContent().build();
    }

    // confirma la compra (Cambia estado a "COMPRADO")
    @PutMapping("/{carritoId}/confirmar")
    public ResponseEntity<CarritoResponse> confirmarCompra(@PathVariable Long carritoId) {
        CarritoResponse response = carritoService.confirmarCompra(carritoId);
        return ResponseEntity.ok(response);
    }
}

