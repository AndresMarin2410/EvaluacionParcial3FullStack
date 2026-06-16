package cl.EvaluacionParcial3.MicroService_Categoria.Controller;

import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaRequest;
import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaResponse;
import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaUpdateRequest;
import cl.EvaluacionParcial3.MicroService_Categoria.Model.Categoria;
import cl.EvaluacionParcial3.MicroService_Categoria.Service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping
    @Operation(summary = "Obtener categorias", description = "Obtiene una lista de todos las categorias registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<Categoria> listarCategorias() {
        log.info("GET api/categorias/listarCategorias");
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoria por ID", description = "Obtiene una categoria mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CategoriaResponse buscarCategoriaPorId(@PathVariable Long id) {
        log.info("GET /api/categorias/buscarCategoriaPorId/{}", id);
        return categoriaService.buscarPorId(id);
    }

    @PostMapping()
    @Operation(summary = "Crear categoria", description = "Crea un nueva categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con exito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "La categoria ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CategoriaRequest request) {
        log.info("POST /api/categorias");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoria", description = "Actualiza una categoria mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CategoriaResponse> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaUpdateRequest request) {
        log.info("PUT /api/categorias/actualizarCategoria/{}", id);
        return ResponseEntity.ok().body(categoriaService.actualizarCategoria(id,request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        log.info("DELETE /api/categorias/eliminarCategoria/{}", id);
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

}
