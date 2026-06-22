package cl.EvaluacionParcial3.MicroService_Notificacion.service;

import cl.EvaluacionParcial3.MicroService_Notificacion.dto.NotificacionRequestDTO;
import cl.EvaluacionParcial3.MicroService_Notificacion.dto.NotificacionResponseDTO;
import cl.EvaluacionParcial3.MicroService_Notificacion.exceptions.NotificacionNotFoundException;
import cl.EvaluacionParcial3.MicroService_Notificacion.exceptions.UsuarioNotFoundException;
import cl.EvaluacionParcial3.MicroService_Notificacion.model.Notificacion;
import cl.EvaluacionParcial3.MicroService_Notificacion.model.TipoNotificacion;
import cl.EvaluacionParcial3.MicroService_Notificacion.repository.NotificacionRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final WebClient webClient;

    @Value("${microservicios.usuarios.url}")
    private String usuariosUrl;

    public NotificacionService(NotificacionRepository notificacionRepository,
            WebClient.Builder webClientBuilder) {
        this.notificacionRepository = notificacionRepository;
        this.webClient = webClientBuilder.build();
    }

    // Obtiene todas las notificaciones registradas

    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarTodas() {
        log.info("Listando todas las notificaciones");
        List<Notificacion> notificaciones = notificacionRepository.findAll();
        log.debug("Se encontraron {} notificaciones en total", notificaciones.size());
        return notificaciones.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtiene una notificación por su ID

    @Transactional(readOnly = true)
    public NotificacionResponseDTO obtenerPorId(Long id) {
        log.info("Buscando notificación con ID: {}", id);
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró notificación con ID: {}", id);
                    return new NotificacionNotFoundException(id);
                });
        log.debug("Notificación encontrada: ID {}, tipo {}", notificacion.getId(), notificacion.getTipo());
        return convertirAResponseDTO(notificacion);
    }

    // Lista todas las notificaciones de un usuario

    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarPorUsuario(Long usuarioId) {
        log.info("Listando notificaciones del usuario ID: {}", usuarioId);
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuarioId);
        log.debug("Se encontraron {} notificaciones para el usuario {}", notificaciones.size(), usuarioId);
        return notificaciones.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista las notificaciones no leídas de un usuario

    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarNoLeidas(Long usuarioId) {
        log.info("Listando notificaciones no leídas del usuario ID: {}", usuarioId);
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId);
        log.debug("Se encontraron {} notificaciones no leídas para el usuario {}", notificaciones.size(), usuarioId);
        return notificaciones.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Cuenta las notificaciones no leídas de un usuario

    @Transactional(readOnly = true)
    public long contarNoLeidas(Long usuarioId) {
        log.info("Contando notificaciones no leídas del usuario ID: {}", usuarioId);
        long total = notificacionRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
        log.debug("El usuario {} tiene {} notificaciones no leídas", usuarioId, total);
        return total;
    }

    // Lista notificaciones filtradas por tipo

    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarPorTipo(String tipo) {
        log.info("Listando notificaciones de tipo: {}", tipo);
        TipoNotificacion tipoEnum = validarTipo(tipo);
        List<Notificacion> notificaciones = notificacionRepository.findByTipo(tipoEnum);
        log.debug("Se encontraron {} notificaciones de tipo {}", notificaciones.size(), tipo);
        return notificaciones.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Crea una nueva notificación.

    @Transactional
    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        log.info("Creando notificación para usuario ID: {}", dto.getUsuarioId());

        validarUsuarioExiste(dto.getUsuarioId());

        TipoNotificacion tipoEnum = validarTipo(dto.getTipo());

        Notificacion notificacion = Notificacion.builder()
                .usuarioId(dto.getUsuarioId())
                .tipo(tipoEnum)
                .mensaje(dto.getMensaje())
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificación creada exitosamente con ID: {}", guardada.getId());
        return convertirAResponseDTO(guardada);
    }

    // Marca una notificación como leída

    @Transactional
    public NotificacionResponseDTO marcarComoLeida(Long id) {
        log.info("Marcando notificación ID: {} como leída", id);

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede marcar como leída: notificación con ID {} no encontrada", id);
                    return new NotificacionNotFoundException(id);
                });

        notificacion.setLeida(true);
        Notificacion actualizada = notificacionRepository.save(notificacion);
        log.info("Notificación ID: {} marcada como leída", actualizada.getId());
        return convertirAResponseDTO(actualizada);
    }

    // Marca todas las notificaciones de un usuario como leídas

    @Transactional
    public void marcarTodasComoLeidas(Long usuarioId) {
        log.info("Marcando todas las notificaciones del usuario ID: {} como leídas", usuarioId);

        List<Notificacion> noLeidas = notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId);
        noLeidas.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(noLeidas);

        log.info("Se marcaron {} notificaciones como leídas para el usuario {}", noLeidas.size(), usuarioId);
    }

    // Elimina una notificación de forma permanente

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando notificación con ID: {}", id);

        if (!notificacionRepository.existsById(id)) {
            log.warn("No se puede eliminar: notificación con ID {} no encontrada", id);
            throw new NotificacionNotFoundException(id);
        }

        notificacionRepository.deleteById(id);
        log.info("Notificación eliminada exitosamente: ID {}", id);
    }

    // Valida que un usuario exista en ms-usuarios mediante WebClient

    private void validarUsuarioExiste(Long usuarioId) {
        log.info("Validando existencia de usuario ID: {} en ms-usuarios", usuarioId);
        try {
            webClient.get()
                    .uri(usuariosUrl + "/v1/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.debug("Usuario ID {} validado correctamente", usuarioId);
        } catch (WebClientResponseException.NotFound e) {
            log.warn("Usuario ID {} no encontrado en ms-usuarios", usuarioId);
            throw new UsuarioNotFoundException(usuarioId);
        } catch (Exception e) {
            log.error("Error al conectar con ms-usuarios: {}", e.getMessage());
            throw new RuntimeException("No se pudo conectar con el servicio de usuarios. Intente más tarde.");
        }
    }

    // Valida que el string del tipo sea un valor válido del enum TipoNotificacion

    private TipoNotificacion validarTipo(String tipo) {
        try {
            return TipoNotificacion.valueOf(tipo.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de notificación no válido: {}", tipo);
            throw new IllegalArgumentException(
                    "Tipo no válido: '" + tipo + "'. Los valores permitidos son: "
                            + "PEDIDO_CONFIRMADO, PAGO_APROBADO, ENVIO_DESPACHADO, ENVIO_ENTREGADO, PEDIDO_CANCELADO");
        }
    }

    // Convierte una entidad Notificacion a su DTO de respuesta

    private NotificacionResponseDTO convertirAResponseDTO(Notificacion notificacion) {
        return NotificacionResponseDTO.builder()
                .id(notificacion.getId())
                .usuarioId(notificacion.getUsuarioId())
                .tipo(notificacion.getTipo().name())
                .mensaje(notificacion.getMensaje())
                .leida(notificacion.getLeida())
                .fechaCreacion(notificacion.getFechaCreacion())
                .build();
    }
}