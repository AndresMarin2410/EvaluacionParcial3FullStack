package cl.EvaluacionParcial3.MicroService_Producto.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con el catalago productos")
public class ProductoController {

    
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

     // Convierte un DTO plano en un EntityModel con sus enlaces HATEOAS
    private EntityModel<ProductoResponseDTO> toEntityModel(ProductoResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ProductoController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarTodos()).withRel("listar-productos"),
                linkTo(methodOn(ProductoController.class).listarPorCategoria(dto.getCategoriaId()))
                        .withRel("productos-misma-categoria")
        );
    }

    // Obtener todos los productos registrados
    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene la lista completa de productos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito")
    })
    public CollectionModel<EntityModel<ProductoResponseDTO>> listarTodos() {
        log.info("GET /api/productos - Listar todos los productos");
        List<EntityModel<ProductoResponseDTO>> productos = productoService.listarTodos().stream()
                .map(this::toEntityModel)
                .toList();

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarTodos()).withSelfRel());
    }

    // Obtener producto por su id
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico mediante su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class),
                            examples = @ExampleObject(name = "ProductoEjemplo",
                                    value = "{\"id\":1,\"nombre\":\"Mouse Inalámbrico\",\"descripcion\":\"Mouse ergonómico 2.4GHz\",\"precio\":12990.0,\"categoriaId\":3,\"activo\":true,\"fechaCreacion\":\"2026-03-10\"}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public EntityModel<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/productos/{} - Obtener producto por ID", id);
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        return toEntityModel(producto);
    }

    // Lista solo los productos activos
    @GetMapping("/activos")
    @Operation(summary = "Listar productos activos", description = "Obtiene únicamente los productos marcados como activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito")
    })
    public CollectionModel<EntityModel<ProductoResponseDTO>> listarActivos() {
        log.info("GET /api/productos/activos - Listar productos activos");
        List<EntityModel<ProductoResponseDTO>> productos = productoService.listarActivos().stream()
                .map(this::toEntityModel)
                .toList();

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarActivos()).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarTodos()).withRel("listar-todos"));
    }

    // Lista productos activos filtrados por categoria
    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar productos por categoría", description = "Obtiene los productos activos que pertenecen a una categoría específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada en ms-categorias")
    })
    public CollectionModel<EntityModel<ProductoResponseDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        log.info("GET /api/productos/categoria/{} - Listar productos por categoría", categoriaId);
        List<EntityModel<ProductoResponseDTO>> productos = productoService.listarPorCategoria(categoriaId).stream()
                .map(this::toEntityModel)
                .toList();

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarPorCategoria(categoriaId)).withSelfRel());
    }

    // Busca productos por nombre
    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por nombre", description = "Busca productos cuyo nombre contenga el texto indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada con éxito")
    })
    public CollectionModel<EntityModel<ProductoResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/productos/buscar?nombre={} - Buscar productos por nombre", nombre);
        List<EntityModel<ProductoResponseDTO>> productos = productoService.buscarPorNombre(nombre).stream()
                .map(this::toEntityModel)
                .toList();

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).buscarPorNombre(nombre)).withSelfRel());
    }

    // Crea nuevo producto

    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto, validando que la categoría exista en ms-categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        log.info("POST /api/productos - Crear nuevo producto");
        ProductoResponseDTO creado = productoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(creado));
    }

    // Actualiza todos los datos de un producto existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza todos los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrada")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {
        log.info("PUT /api/productos/{} - Actualizar producto", id);
        ProductoResponseDTO actualizado = productoService.actualizar(id, dto);
        return ResponseEntity.ok(toEntityModel(actualizado));
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/productos/{} - Eliminar producto", id);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Desactivar un producto
    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar producto", description = "Marca un producto como inactivo sin eliminarlo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto desactivado con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> desactivar(@PathVariable Long id) {
        log.info("PATCH /api/productos/{}/desactivar - Desactivar producto", id);
        ProductoResponseDTO desactivado = productoService.desactivar(id);
        return ResponseEntity.ok(toEntityModel(desactivado));
    }

    // Activar un producto desactivado
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar producto", description = "Reactiva un producto previamente desactivado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto activado con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> activar(@PathVariable Long id) {
        log.info("PATCH /api/productos/{}/activar - Activar producto", id);
        ProductoResponseDTO activado = productoService.activar(id);
        return ResponseEntity.ok(toEntityModel(activado));
    }
}
