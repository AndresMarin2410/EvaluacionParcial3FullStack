package cl.EvaluacionParcial3.MicroService_Notificacion.controller;

import cl.EvaluacionParcial3.MicroService_Notificacion.dto.NotificacionRequestDTO;
import cl.EvaluacionParcial3.MicroService_Notificacion.dto.NotificacionResponseDTO;
import cl.EvaluacionParcial3.MicroService_Notificacion.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Notificacion", description = "Operaciones de notificaciones enviadas a los usuarios")
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

     // Obtiene todas las notificaciones registradas
     
    @Operation(summary = "Listar todas las notificaciones", description = "Obtiene todas las notificaciones registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listarTodas() {
        log.info("GET /api/notificaciones - Listar todas las notificaciones");
        List<NotificacionResponseDTO> notificaciones = notificacionService.listarTodas();
        return ResponseEntity.ok(notificaciones);
    }

     // Obtiene una notificación por su ID
     
    @Operation(summary = "Obtener notificación por ID", description = "Busca y retorna una notificación específica según su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/notificaciones/{} - Obtener notificación por ID", id);
        NotificacionResponseDTO notificacion = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(notificacion);
    }

     // Lista todas las notificaciones de un usuario
     
    @Operation(summary = "Listar notificaciones por usuario", description = "Obtiene todas las notificaciones asociadas a un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{} - Listar notificaciones por usuario", usuarioId);
        List<NotificacionResponseDTO> notificaciones = notificacionService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

     // Lista las notificaciones no leídas de un usuario
     
    @Operation(summary = "Listar notificaciones no leídas", description = "Obtiene las notificaciones pendientes de lectura de un usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> listarNoLeidas(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/no-leidas - Listar notificaciones no leídas", usuarioId);
        List<NotificacionResponseDTO> notificaciones = notificacionService.listarNoLeidas(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

     // Cuenta las notificaciones no leídas de un usuario
     
    @Operation(summary = "Contar notificaciones no leídas", description = "Retorna el total de notificaciones pendientes de lectura de un usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}/contador")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/contador - Contar notificaciones no leídas", usuarioId);
        long total = notificacionService.contarNoLeidas(usuarioId);
        return ResponseEntity.ok(total);
    }

     // Lista notificaciones filtradas por tipo
     
    @Operation(summary = "Listar notificaciones por tipo", description = "Obtiene las notificaciones filtradas según su tipo (ej: EMAIL, PUSH, SMS).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "400", description = "Tipo de notificación inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorTipo(@PathVariable String tipo) {
        log.info("GET /api/notificaciones/tipo/{} - Listar notificaciones por tipo", tipo);
        List<NotificacionResponseDTO> notificaciones = notificacionService.listarPorTipo(tipo);
        return ResponseEntity.ok(notificaciones);
    }
  
     // Crea una nueva notificación. Valida el body con @Valid (Bean Validation).
     
    @Operation(summary = "Crear una nueva notificación", description = "Registra y envía una nueva notificación para un usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada con exito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crear(@Valid @RequestBody NotificacionRequestDTO dto) {
        log.info("POST /api/notificaciones - Crear nueva notificación");
        NotificacionResponseDTO creada = notificacionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

     // Marca una notificación como leída
     
    @Operation(summary = "Marcar notificación como leída", description = "Actualiza el estado de una notificación específica a 'leída'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion realizada con exito"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(@PathVariable Long id) {
        log.info("PATCH /api/notificaciones/{}/leer - Marcar notificación como leída", id);
        NotificacionResponseDTO actualizada = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(actualizada);
    }

     // Marca todas las notificaciones de un usuario como leídas
     
    @Operation(summary = "Marcar todas las notificaciones como leídas", description = "Actualiza el estado de todas las notificaciones de un usuario a 'leída'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Operacion realizada con exito, sin contenido"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/usuario/{usuarioId}/leer-todas")
    public ResponseEntity<Void> marcarTodasComoLeidas(@PathVariable Long usuarioId) {
        log.info("PATCH /api/notificaciones/usuario/{}/leer-todas - Marcar todas como leídas", usuarioId);
        notificacionService.marcarTodasComoLeidas(usuarioId);
        return ResponseEntity.noContent().build();
    }

    
     // Elimina una notificación de forma permanente
     
    @Operation(summary = "Eliminar notificación", description = "Elimina de forma permanente una notificación según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{} - Eliminar notificación", id);
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}