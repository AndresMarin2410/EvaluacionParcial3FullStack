package cl.EvaluacionParcial3.MicroService_Producto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.EvaluacionParcial3.MicroService_Producto.dto.ProductoRequestDTO;
import cl.EvaluacionParcial3.MicroService_Producto.dto.ProductoResponseDTO;
import cl.EvaluacionParcial3.MicroService_Producto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/productos")
public class ProductoController {

    
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Obtener todos los productos registrados
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        log.info("GET /api/productos - Listar todos los productos");
        List<ProductoResponseDTO> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    // Obtener producto por su id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/productos/{} - Obtener producto por ID", id);
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    // Lista solo los productos activos
    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponseDTO>> listarActivos() {
        log.info("GET /api/productos/activos - Listar productos activos");
        List<ProductoResponseDTO> productos = productoService.listarActivos();
        return ResponseEntity.ok(productos);
    }

    // Lista productos activos filtrados por categoria
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        log.info("GET /api/productos/categoria/{} - Listar productos por categoría", categoriaId);
        List<ProductoResponseDTO> productos = productoService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    // Busca productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/productos/buscar?nombre={} - Buscar productos por nombre", nombre);
        List<ProductoResponseDTO> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    // Crea nuevo producto

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        log.info("POST /api/productos - Crear nuevo producto");
        ProductoResponseDTO creado = productoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Actualiza todos los datos de un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {
        log.info("PUT /api/productos/{} - Actualizar producto", id);
        ProductoResponseDTO actualizado = productoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/productos/{} - Eliminar producto", id);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Desactivar un producto
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ProductoResponseDTO> desactivar(@PathVariable Long id) {
        log.info("PATCH /api/productos/{}/desactivar - Desactivar producto", id);
        ProductoResponseDTO desactivado = productoService.desactivar(id);
        return ResponseEntity.ok(desactivado);
    }

    // Activar un producto desactivado
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ProductoResponseDTO> activar(@PathVariable Long id) {
        log.info("PATCH /api/productos/{}/activar - Activar producto", id);
        ProductoResponseDTO activado = productoService.activar(id);
        return ResponseEntity.ok(activado);
    }
}
