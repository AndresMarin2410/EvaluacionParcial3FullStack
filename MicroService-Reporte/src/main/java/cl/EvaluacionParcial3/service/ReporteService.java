package cl.EvaluacionParcial3.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.EvaluacionParcial3.dto.request.ReporteRequest;
import cl.EvaluacionParcial3.dto.response.ReporteResponse;
import cl.EvaluacionParcial3.mapper.DetalleMapper;
import cl.EvaluacionParcial3.mapper.ReporteMapper;
import cl.EvaluacionParcial3.model.DetalleReporte;
import cl.EvaluacionParcial3.model.ReporteVenta;
import cl.EvaluacionParcial3.model.TipoReporte;
import cl.EvaluacionParcial3.repository.ReporteVentaRepository;




@Service
public class ReporteService {

    @Autowired
    private ReporteVentaRepository reporteVentaRepository;

    @Autowired
    private DetalleMapper detalleMapper;

    // crear un nuevo reporte con sus detalles
    @Transactional
    public ReporteResponse crearReporte(ReporteRequest request) {
        // construimos la entidad ReporteVenta desde el request
        ReporteVenta reporte = new ReporteVenta();
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporte.setFechaInicio(request.getFechaInicio());
        reporte.setFechaFin(request.getFechaFin());
        reporte.setContenido(request.getContenido());
        reporte.setTipo(TipoReporte.valueOf(request.getTipoReporte()));

        // mapeamos los detalles y los enlazamos al reporte
        List<DetalleReporte> detalles = request.getDetalles().stream()
                .map(detalleMapper::toEntidad)
                .collect(Collectors.toList());

        // enlazamos cada detalle a su reporte (relación bidireccional)
        detalles.forEach(detalle -> detalle.setReporte(reporte));
        reporte.setDetalles(detalles);

        // guardamos (gracias al CascadeType.ALL se guardan los detalles también)
        ReporteVenta reporteGuardado = reporteVentaRepository.save(reporte);

        // retornamos el DTO usando el mapper
        return ReporteMapper.toResponse(reporteGuardado);
    }

    // obtener un reporte por su ID
    public ReporteResponse obtenerReportePorId(Long id) {
        ReporteVenta reporte = reporteVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el reporte con ID: " + id));

        return ReporteMapper.toResponse(reporte);
    }

    // listar todos los reportes
    public List<ReporteResponse> listarReportes() {
        return reporteVentaRepository.findAll().stream()
                .map(ReporteMapper::toResponse)
                .collect(Collectors.toList());
    }

    // eliminar un reporte por ID
    @Transactional
    public void eliminarReporte(Long id) {
        // verificamos que exista antes de borrar
        ReporteVenta reporte = reporteVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar un reporte que no existe"));

        reporteVentaRepository.delete(reporte);
    }
}
