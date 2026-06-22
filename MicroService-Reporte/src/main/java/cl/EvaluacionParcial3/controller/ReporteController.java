package cl.EvaluacionParcial3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.EvaluacionParcial3.dto.request.ReporteRequest;
import cl.EvaluacionParcial3.dto.response.ReporteResponse;
import cl.EvaluacionParcial3.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name="Reportes" ,description="operaciones relacionadas con los reportes")
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;
       

    // crear un nuevo reporte de ventas
    @Operation(summary="Creacion de nuevo reporte de ventas", description="Se crea un reporte detallado de venta")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ReporteResponse> crearReporte(@Valid @RequestBody ReporteRequest request) {
        ReporteResponse response = reporteService.crearReporte(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // obtener un reporte específico por ID
    @Operation(summary="Obtencion de reporte segun su id", description="Se accede a datos de un reporte en especifico, mediante su numero de identificacion")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponse> obtenerReporte(@PathVariable Long id) {
        ReporteResponse response = reporteService.obtenerReportePorId(id);
        return ResponseEntity.ok(response);
    }

    // listar todos los reportes
    @Operation(summary="Listado de reportes", description="Se obtiene una lista con todos los reportes")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ReporteResponse>> listarReportes() {
        List<ReporteResponse> reportes = reporteService.listarReportes();
        return ResponseEntity.ok(reportes);
    }

    // eliminar un reporte por ID
    @Operation(summary="Eliminacion de reporte segun su id", description="Se elimina un reporte en especifico en base a su numero de identifiacion")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}
